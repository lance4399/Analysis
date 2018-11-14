package utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * @Author: xiliang
 * @Create: 2018/8/1 16:24
 */
public class Connect2Mongo {
    private static final String host = "xxx.com";
    private static final int port = 10000;
    private static final String user = "username";
    private static final String database = "database";
    private static final String password = "root";
    private static final String collectionName = "operation_record";

    private static MongoClient mongoClient;
    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH) ;

    public  static void main(String[]args) throws ParseException {
//        String dateString  = "20180801";
//        formatTime(dateString);
        long startTime = System.currentTimeMillis();
        JSONArray list = getCollectionData();
        long queryCostTime = System.currentTimeMillis() - startTime;
        System.out.println("It took "+queryCostTime/1000+" s");
        System.out.println("total records today:"+list.size());
//        System.out.println("each record:"+list.get(0));
//        String tmp  ="{\"create_time\":\"Thu Aug 02 09:24:39 CST 2018\",\"mp_media_id\":\"789A05F209BC5F8FC8E645C41565EA0F@qq.sohu.com\",\"new_level\":\"2\",\"old_mp_channel_id\":\"null\",\"mp_profile_id\":\"219889\",\"type\":\"3\",\"operator_name\":\"yifeiwang213692\",\"sms_id\":\"null\",\"new_mp_channel_id\":\"null\",\"old_level\":\"3\",\"email_template_id\":\"null\",\"operator_passport\":\"yifeiwang213692@sohu-inc.com\",\"profile_reaction\":\"null\"}";
//        System.out.println(tmp.getBytes().length);
    }

    public static JSONArray getCollectionData(){
        JSONArray array = new JSONArray();
        try {
            MongoCredential credential = MongoCredential.createCredential(user, database, password.toCharArray());
            mongoClient = new MongoClient( new ServerAddress(host, port),  Arrays.asList(credential) );
            MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
            MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
            String fielName = "create_time";
            String dt = new SimpleDateFormat("yyyyMMdd").format(new Date());
//            FindIterable<Document> findIterable = collection.find(Filters.lt(fielName, formatTime( dt )));
//            FindIterable<Document> findIterable = collection.find();
            FindIterable<Document> findIterable = collection.find(Filters.gt(fielName, formatTime(dt)));
            MongoCursor<Document> mongoCursor = findIterable.iterator();

            while (mongoCursor.hasNext()) {
                Document d = mongoCursor.next();
//                System.out.println(d);
                Bonus b = new Bonus();
                if(null !=  d.getInteger("type") )
                    b.setType(String.valueOf(d.getInteger("type")));
                else b.setType("null");
                if(null != d.getInteger("sms_id") )
                    b.setSms_id(String.valueOf(d.getInteger("sms_id")));
                else
                    b.setSms_id("null");
                if(null != d.getInteger("email_template_id"))
                    b.setEmail_template_id(String.valueOf(d.getInteger("email_template_id")));
                else
                    b.setEmail_template_id("null");
                if(null != d.getString("mp_media_id"))
                    b.setMp_media_id(d.getString("mp_media_id"));
                else
                    b.setMp_media_id("null");
                if(null != d.getString("operator_passport"))
                    b.setOperator_passport(d.getString("operator_passport"));
                else
                    b.setOperator_passport("null");
                if(null != d.getString("operator_name") )
                    b.setOperator_name( d.getString("operator_name") );
                else
                    b.setOperator_name("null");
                if(null != d.getInteger("old_level"))
                    b.setOld_level(String.valueOf(d.getInteger("old_level")) );
                else
                    b.setOld_level("null");
                if(null != d.getInteger("new_level"))
                    b.setNew_level(String.valueOf(d.getInteger("new_level") ));
                else
                    b.setNew_level("null");
                if(null != d.getInteger("old_mp_channel_id") )
                    b.setOld_mp_channel_id(String.valueOf(d.getInteger("old_mp_channel_id")));
                else
                    b.setOld_mp_channel_id("null");
                if(null != d.getInteger("new_mp_channel_id"))
                    b.setNew_mp_channel_id(String.valueOf(d.getInteger("new_mp_channel_id")));
                else
                    b.setNew_mp_channel_id("null");
                if(null != d.getInteger("mp_profile_id"))
                    b.setMp_profile_id(String.valueOf(d.getInteger("mp_profile_id")));
                else
                    b.setMp_profile_id("null");
                if(null != d.getString("profile_reaction"))
                    b.setProfile_reaction(d.getString("profile_reaction"));
                else
                    b.setProfile_reaction("null");
                if(null != d.getDate("create_time")) {
                    String ds = sdf.format(d.getDate("create_time").getTime());
                    Date date = sdf.parse(ds);
                    b.setCreate_time(date.toString());
                }else b.setCreate_time("null");
//                System.out.println(b.toString());
                array.add(JSON.parse(b.toString()));
            }
            mongoClient.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            mongoClient.close();
        }
        return array;

        }

    public static Date formatTime(String dateString){
        SimpleDateFormat origin = new SimpleDateFormat("yyyyMMdd");
        Date dt = null;
        try {
            dt = origin.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }
}
