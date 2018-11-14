package spark.advertisement_streaming;
//import com.google.common.base.Optional;
import constants.Constants;
import dao.*;
import dao.factory.DAOFactory;
import entity.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.hive.HiveContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Tuple2;
import utils.ConfigurationManager;
import utils.DateUtils;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * 广告点击流量实时统计Spark作业
 * @Author: xiliang
 * @Create: 2018/8/10 21:37
 */
public class AdClickRealTimeStat {
    private  static SparkConf conf;
    private static JavaStreamingContext jssc;
    private  static Map<String, Object> kafkaParams = new HashMap<>();
    private  static Collection<String> topics = new HashSet<String>();

    public static void init(){
        conf = new SparkConf().setMaster("local[2]").setAppName("AdClickRealTimeStatSpark");
//                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
//				.set("spark.default.parallelism", "1000");
        jssc = new JavaStreamingContext(conf, Durations.seconds(10));
        kafkaParams.put("metadata.broker.list",  ConfigurationManager.getProperty(Constants.KAFKA_METADATA_BROKER_LIST));
        String kafkaTopics = ConfigurationManager.getProperty(Constants.KAFKA_TOPICS);
        String[] kafkaTopicsSplited = kafkaTopics.split(",");
        if(kafkaTopicsSplited.length > 1) {
            for (String kafkaTopic : kafkaTopicsSplited) {
                topics.add(kafkaTopic);
            }
        }else {
            topics.add(kafkaTopics);
        }
    }

    public static void main(String[] args) throws  Exception{
        init();
        JavaInputDStream<ConsumerRecord<String,String>> recordJavaInputDStream= KafkaUtils.createDirectStream(
                jssc, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(topics, kafkaParams));

        // 根据动态黑名单进行数据过滤
        JavaDStream<String> filteredAdRealTimeLogDStream = filterByBlacklist(recordJavaInputDStream);
        //生成黑名单
        generateDynamicBlacklist(filteredAdRealTimeLogDStream);

        // 业务功能一：计算广告点击流量实时统计结果（yyyyMMdd_province_city_adid,clickCount）
        JavaPairDStream<String, Long> adRealTimeStatDStream = calculateRealTimeStat(
                filteredAdRealTimeLogDStream);

        // 业务功能二：实时统计每天每个省份top3热门广告
        calculateProvinceTop3Ad(adRealTimeStatDStream);

        // 业务功能三：实时统计每天每个广告在最近1小时的滑动窗口内的点击趋势（每分钟的点击量）
        // 每次都可看到每个广告，最近一小时内，每分钟的点击量 -> 每支广告的点击趋势
        calculateAdClickCountByWindow(recordJavaInputDStream);

        jssc.start();
        jssc.awaitTermination();
        jssc.close();

    }




    //filtering the raw-log through blacklist
    private static JavaDStream<String> filterByBlacklist(JavaInputDStream<ConsumerRecord<String,String>> adRealTimeLogDStream) {
        // 刚刚接受到原始的用户点击行为日志之后
        // 根据mysql中的动态黑名单，进行实时的黑名单过滤（黑名单用户的点击行为，直接过滤掉，不要了）
        // 使用transform算子（将dstream中的每个batch RDD进行处理，转换为任意的其他RDD，功能很强大）
        JavaDStream<String> filteredAdRealTimeLogDStream = adRealTimeLogDStream
                .transform((JavaRDD<ConsumerRecord<String, String>> rdd) -> {
            // 首先，从mysql中查询所有黑名单用户，将其转换为一个rdd
            IAdBlacklistDAO adBlacklistDAO = DAOFactory.getAdBlacklistDAO();
            List<AdBlacklist> adBlacklists = adBlacklistDAO.findAll();
            List<Tuple2<Long, Boolean>> tuples = new ArrayList<>();
            for (AdBlacklist adBlacklist : adBlacklists) {
                tuples.add(new Tuple2<>(adBlacklist.getUserid(), true));
            }

            JavaSparkContext sc = new JavaSparkContext(rdd.context());
            JavaPairRDD<Long, Boolean> blacklistRDD = sc.parallelizePairs(tuples);

            // 将原始数据rdd映射成<userid, string>
            JavaPairRDD<Long, String> mappedRDD = rdd.mapToPair((ConsumerRecord<String, String> record) -> {
                String log = record.value();
                String[] logSplited = log.split(" ");
                long user_id = Long.valueOf(logSplited[3]);
                return new Tuple2<>(user_id, log);
            });


            JavaPairRDD<Long,Tuple2<String, Optional<Boolean> >> joinedRDD = mappedRDD.mapToPair(tuple ->{
                Optional<Boolean> b = null;
                Tuple2<String,Optional<Boolean>> t = new  Tuple2<>(tuple._2,b);
                return new Tuple2<>(tuple._1,t);
            });

//             JavaRDD<String> resultRDD = mappedRDD.leftOuterJoin(blacklistRDD).filter(tuple ->{
//                Optional<Boolean>  optional = tuple._2._2;
//                        if(optional.isPresent() && optional.get()){
//                            return false;
//                        }
//                        return true;
//            }).map(tuple -> ( tuple._2._1));

            JavaPairRDD<Long,Tuple2<String, Optional<Boolean> >> filteredRDD = joinedRDD.filter((Tuple2<Long, Tuple2<String, Optional<Boolean>>> tuple) ->{
                Optional<Boolean>  optional = tuple._2._2;
                if(optional.isPresent() && optional.get()){
                    return false;
                }
                return true;
            });

            JavaRDD<String> resultRDD = filteredRDD.map((Tuple2<Long, Tuple2<String, Optional<Boolean>>> tuple) ->(tuple._2._1));
            return resultRDD;
        });

        return filteredAdRealTimeLogDStream;
    }

    //genate blacklist
    private static void generateDynamicBlacklist(JavaDStream<String> logDstream) {
        // 实时日志:timestamp|province|city|userid|adid
        JavaPairDStream<String, Long> dailyUserAdClickCountDStream = logDstream.mapToPair((String record) ->{
            String[] splitedValue= record.split("\\|");
            String date = new SimpleDateFormat("yyyyMMdd").format(splitedValue[0]);
            String key = date+"|"+splitedValue[1]+"|"+splitedValue[2];
            return new Tuple2<String,Long>(key,1l);
        }).reduceByKey(( Long v1,Long v2 )->( v1 + v2) );
        // 每个batch中，当天每个用户对每支广告的点击次数 <yyyyMMdd|userid|adid, clickCount>
        dailyUserAdClickCountDStream.foreachRDD(( rdd) ->{
            rdd.foreachPartition((partition_iterator)-> {
                        List<AdUserClickCount> adUserClickCounts = new ArrayList<AdUserClickCount>();
                        while(partition_iterator.hasNext()) {
                            Tuple2<String, Long> tuple = partition_iterator.next();
                            String[] keySplited = tuple._1.split("_");
                            String date = DateUtils.formatDate(DateUtils.parseDateKey(keySplited[0]));
                            // yyyy-MM-dd
                            long userid = Long.valueOf(keySplited[1]);
                            long adid = Long.valueOf(keySplited[2]);
                            long clickCount = tuple._2;
                            AdUserClickCount adUserClickCount = new AdUserClickCount();
                            adUserClickCount.setDate(date);
                            adUserClickCount.setUserid(userid);
                            adUserClickCount.setAdid(adid);
                            adUserClickCount.setClickCount(clickCount);
                            adUserClickCounts.add(adUserClickCount);
                        }
                        IAdUserClickCountDAO adUserClickCountDAO = DAOFactory.getAdUserClickCountDAO();
                        adUserClickCountDAO.updateBatch(adUserClickCounts);
            });
        });

        // 遍历每个batch中对每条记录都要去查询现某个用户某天对某个广告的点击量
        // clickCount>=100该用户就是黑名单用户，就写入mysql
        JavaPairDStream<String, Long> blacklistDStream = dailyUserAdClickCountDStream.filter(tuple->{
            String key = tuple._1;
            String[] keySplited = key.split("\\|");
            // yyyyMMdd -> yyyy-MM-dd
            String date = DateUtils.formatDate(DateUtils.parseDateKey(keySplited[0]));
            long userid = Long.valueOf(keySplited[1]);
            long adid = Long.valueOf(keySplited[2]);
            // 从mysql中查询指定日期指定用户对指定广告的点击量
            IAdUserClickCountDAO adUserClickCountDAO = DAOFactory.getAdUserClickCountDAO();
            int clickCount = adUserClickCountDAO.findClickCountByMultiKey(date, userid, adid);
            // 点击量>=100 ->黑名单用户
            if(clickCount >= 100) return true;
            // 反之不管
            else return false;
        });

        // 抽取rdd的userid
        JavaDStream<Long> blacklistUseridDStream = blacklistDStream.map(tuple -> {
            String key = tuple._1;
            String[] keySplited = key.split("_");
            Long userid = Long.valueOf(keySplited[1]);
            return userid;
        });

        //distinct进行全局的去重
        JavaDStream<Long> distinctBlacklistUseridDStream = blacklistUseridDStream.transform( rdd -> ( rdd.distinct() ) );
        distinctBlacklistUseridDStream.foreachRDD(rdd ->{
            rdd.foreachPartition(partition_iterator -> {
                List<AdBlacklist> adBlacklists = new ArrayList<AdBlacklist>();
                while(partition_iterator.hasNext()) {
                    long userid = partition_iterator.next();
                    AdBlacklist adBlacklist = new AdBlacklist();
                    adBlacklist.setUserid(userid);
                    adBlacklists.add(adBlacklist);
                }
                IAdBlacklistDAO adBlacklistDAO = DAOFactory.getAdBlacklistDAO();
                adBlacklistDAO.insertBatch(adBlacklists);
            });
        });
    }

    //业务功能一：计算广告点击流量实时统计结果（yyyyMMdd_province_city_adid,clickCount）
    private static JavaPairDStream<String,Long> calculateRealTimeStat(JavaDStream<String> filteredAdRealTimeLogDStream) {
        // 2015-12-01，当天，可以看到当天所有的实时数据（动态改变），比如江苏省南京市
        // 广告可以进行选择（广告主、广告名称、广告类型来筛选一个出来）
        // 拿着date、province、city、adid，去mysql中查询最新的数据
        // 等等，基于这几个维度，以及这份动态改变的数据，是可以实现比较灵活的广告点击流量查看的功能的

        // date province city userid adid
        // date_province_city_adid，作为key；1作为value
        // 通过spark，直接统计出来全局的点击次数，在spark集群中保留一份；在mysql中，也保留一份
        // 我们要对原始数据进行map，映射成<date_province_city_adid,1>格式
        // 然后呢，对上述格式的数据，执行updateStateByKey算子
        // spark streaming特有的一种算子，在spark集群内存中，维护一份key的全局状态
        JavaPairDStream<String, Long> mappedDStream = filteredAdRealTimeLogDStream.mapToPair(rawlog ->{
            String log = rawlog;
            String[] logSplited = log.split(" ");
            String timestamp = logSplited[0];
            Date date = new Date(Long.valueOf(timestamp));
            String datekey = DateUtils.formatDateKey(date);	// yyyyMMdd
            String province = logSplited[1];
            String city = logSplited[2];
            long adid = Long.valueOf(logSplited[4]);
            String key = datekey + "_" + province + "_" + city + "_" + adid;
            return new Tuple2<>(key, 1L);
        });


//        // 在这个dstream中，就相当于，有每个batch rdd累加的各个key（各天各省份各城市各广告的点击次数）
//        // 每次计算出最新的值，就在aggregatedDStream中的每个batch rdd中反应出来
//        JavaPairDStream<String, Long> aggregatedDStream = mappedDStream.updateStateByKey(
//                (( values,  optional) ->{
//                    Long clickCount = 0L;
//                    if(optional.isPresent()) {
//                        clickCount = (Long)optional.get();
//                    }
//                    // values，代表了，batch rdd中，每个key对应的所有的值
//                    for(Long value : values) {
//                        clickCount += value;
//                    }
//                    return Optional.of(clickCount);
//                }),Durations.seconds(30), Durations.seconds(10));

        JavaPairDStream<String, Long> aggregatedDStream =null;

                // 将计算出来的最新结果，同步一份到mysql中，以便于j2ee系统使用
        aggregatedDStream.foreachRDD( (JavaPairRDD<String, Long> rdd) -> {
            rdd.foreachPartition((Iterator<Tuple2<String, Long>> iterator) ->{
                List<AdStat> adStats = new ArrayList<>();
                while(iterator.hasNext()) {
                    Tuple2<String, Long> tuple = iterator.next();
                    String[] keySplited = tuple._1.split("_");
                    String date = keySplited[0];
                    String province = keySplited[1];
                    String city = keySplited[2];
                    long adid = Long.valueOf(keySplited[3]);
                    long clickCount = tuple._2;
                    AdStat adStat = new AdStat();
                    adStat.setDate(date);
                    adStat.setProvince(province);
                    adStat.setCity(city);
                    adStat.setAdid(adid);
                    adStat.setClickCount(clickCount);
                    adStats.add(adStat);
                }
                IAdStatDAO adStatDAO = DAOFactory.getAdStatDAO();
                adStatDAO.updateBatch(adStats);
            });
        });
        return aggregatedDStream;
    }

    //业务功能二：实时统计每天每个省份top3热门广告
    private static void calculateProvinceTop3Ad(JavaPairDStream<String, Long> adRealTimeStatDStream) {
        // <yyyyMMdd_province_city_adid, clickCount>
        JavaDStream<Row> rowsDStream = adRealTimeStatDStream.transform(  (JavaPairRDD<String, Long> rdd) ->{
                // <yyyyMMdd_province_adid, clickCount>
                JavaPairRDD<String, Long> mappedRDD = rdd.mapToPair( (Tuple2<String, Long> tuple) ->{
                    String[] keySplited = tuple._1.split("_");
                    String date = keySplited[0];
                    String province = keySplited[1];
                    long adid = Long.valueOf(keySplited[3]);
                    long clickCount = tuple._2;
                    String key = date + "_" + province + "_" + adid;
                    return new Tuple2<>(key, clickCount);
                });

                JavaPairRDD<String, Long> dailyAdClickCountByProvinceRDD = mappedRDD.reduceByKey( (Long v1, Long v2) ->(v1+v2));

                JavaRDD<Row> rowsRDD = dailyAdClickCountByProvinceRDD.map( (Tuple2<String, Long> tuple) ->{
                    String[] keySplited = tuple._1.split("_");
                    String datekey = keySplited[0];
                    String province = keySplited[1];
                    long adid = Long.valueOf(keySplited[2]);
                    long clickCount = tuple._2;
                    String date = DateUtils.formatDate(DateUtils.parseDateKey(datekey));
                    return RowFactory.create(date, province, adid, clickCount);
                } );
                StructType schema = DataTypes.createStructType(Arrays.asList(
                        DataTypes.createStructField("date", DataTypes.StringType, true),
                        DataTypes.createStructField("province", DataTypes.StringType, true),
                        DataTypes.createStructField("ad_id", DataTypes.LongType, true),
                        DataTypes.createStructField("click_count", DataTypes.LongType, true)));
                HiveContext sqlContext = new HiveContext(rdd.context());
                sqlContext.createDataFrame(rowsRDD, schema).createOrReplaceTempView("tmp_daily_ad_click_count_by_prov");
                JavaRDD<Row> provinceTop3Ad =  sqlContext.sql(
                        "SELECT date, province, ad_id, click_count "
                                + "FROM ( "
                                + "SELECT date, province,ad_id,click_count,"
                                + "ROW_NUMBER() OVER(PARTITION BY province ORDER BY click_count DESC) rank "
                                + "FROM tmp_daily_ad_click_count_by_prov "
                                + ") t "
                                + "WHERE rank>=3"
                ).javaRDD();
                return provinceTop3Ad;
        });

        // 每次都是刷新出来各个省份最热门的top3广告
        // 将其中的数据批量更新到MySQL中
        rowsDStream.foreachRDD( (JavaRDD<Row> rdd) -> {
            rdd.foreachPartition( (Iterator<Row> iterator) -> {
                List<AdProvinceTop3> adProvinceTop3s = new ArrayList<>();
                while(iterator.hasNext()) {
                    Row row = iterator.next();
                    String date = row.getString(0);
                    String province = row.getString(1);
                    long adid = row.getLong(2);
                    long clickCount = row.getLong(3);
                    AdProvinceTop3 adProvinceTop3 = new AdProvinceTop3();
                    adProvinceTop3.setDate(date);
                    adProvinceTop3.setProvince(province);
                    adProvinceTop3.setAdid(adid);
                    adProvinceTop3.setClickCount(clickCount);
                    adProvinceTop3s.add(adProvinceTop3);
                }
                IAdProvinceTop3DAO adProvinceTop3DAO = DAOFactory.getAdProvinceTop3DAO();
                adProvinceTop3DAO.updateBatch(adProvinceTop3s);
            });
        });
    }

    // 业务功能三：实时统计每天每个广告在最近1小时的滑动窗口内的点击趋势（每分钟的点击量）
    // 每次都可看到每个广告，最近一小时内，每分钟的点击量 -> 每支广告的点击趋势
    private static void calculateAdClickCountByWindow(JavaInputDStream<ConsumerRecord<String,String>> adRealTimeStatDStream) {
        // 映射成<yyyyMMddHHMM_adid,1L>格式
        JavaPairDStream<String, Long> pairDStream = adRealTimeStatDStream.mapToPair( record ->{
            // timestamp province city userid adid
            String[] logSplited = record.value().split(" ");
            String timeMinute = DateUtils.formatTimeMinute(
                    new Date(Long.valueOf(logSplited[0])));
            long adid = Long.valueOf(logSplited[4]);
            return new Tuple2<>(timeMinute + "_" + adid, 1L);
        });

        // 根据key进行reduceByKey操作，统计出来最近一小时内的各分钟各广告的点击次数   点图 / 折线图
        JavaPairDStream<String, Long> aggrRDD = pairDStream.reduceByKeyAndWindow((Long v1, Long v2) ->(v1+v2), Durations.minutes(60), Durations.seconds(10) );

        // aggrRDD 每次都可以拿到，最近1小时内，各分钟（yyyyMMddHHMM）各广告的点击量
        aggrRDD.foreachRDD(rdd->{
            rdd.foreachPartition( iterator ->{
                List<AdClickTrend> adClickTrends = new ArrayList<AdClickTrend>();
                while(iterator.hasNext()) {
                    Tuple2<String, Long> tuple = iterator.next();
                    String[] keySplited = tuple._1.split("_");
                    // yyyyMMddHHmm
                    String dateMinute = keySplited[0];
                    long adid = Long.valueOf(keySplited[1]);
                    long clickCount = tuple._2;
                    String date = DateUtils.formatDate(DateUtils.parseDateKey(
                            dateMinute.substring(0, 8)));
                    String hour = dateMinute.substring(8, 10);
                    String minute = dateMinute.substring(10);
                    AdClickTrend adClickTrend = new AdClickTrend();
                    adClickTrend.setDate(date);
                    adClickTrend.setHour(hour);
                    adClickTrend.setMinute(minute);
                    adClickTrend.setAdid(adid);
                    adClickTrend.setClickCount(clickCount);
                    adClickTrends.add(adClickTrend);
                }
                IAdClickTrendDAO adClickTrendDAO = DAOFactory.getAdClickTrendDAO();
                adClickTrendDAO.updateBatch(adClickTrends);
            });
        });
    }
}
