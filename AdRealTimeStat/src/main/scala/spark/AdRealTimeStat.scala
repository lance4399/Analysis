package spark

import java.util.Date

import org.apache.spark.{SparkConf, SparkContext}
import sohu.mptc.data.handlers.OdsShmmWebEvDiHandler
import sohu.mptc.data.handlers.rawlog.newlog.NewOriginalEvLogHandler
import sohu.mptc.data.models.rawlog.OriginalEvLogModel

import scala.collection.JavaConverters._
import scala.util.Try

object AdRealTimeStat {
  def main(args: Array[String]): Unit = {
    val ev_log="2" + //logversion
      "\u0003" +
      "1533291799221" + //logtime
      "\u0003" +
      "1.0.1_pre" + //setJsVersion
      "\u0003" +
      "1527490947158856" + //setVstCookie
      "\u0003" +
      "223.89.150.147" + //setVstIp
      "\u0003" +  //setVstUserId
      "\u0003" + //setVstUserAgent
      "Mozilla/5.0 (Linux; Android 7.0; SLA-TL10 Build/HUAWEISLA-TL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.0 lite baiduboxapp/3.4.0.10 (Baidu; P1 7.0)" +
      "\u0003" +
      "720*1280" +   //setDeviceResolution
      "\u0003" +
      "http://m.sohu.com/a/241279690_100195216" +   //setPageUrl
      "\u0003" +   //setPageReferUrl
      "https://m.baidu.com/from=1021238x/bd_page_type=1/ssid=0/uid=0/baiduid=A143D7B0787D4F9F819184D5F0C9F665/w=0_10_/t=iphone/l=1/tc?ref=www_iphone&pu=cuid%40_i-HfY8kH8_ou283guB_agifH8lcPSaM_82baY82vai_aB8fgP-q8_uz2i_Ca2fHA%2Ccua%40_avLC_aE2f4qywoUfpw1z4aGXiz0as8DgN2w8AZoC%2Ccut%40JfS_NJfK284iaXiDyavdC5kcSMzWt-OdB%2Cosname%40baiduboxapp%2Cosbranch%40a2%2Cctv%402%2Ccfrom%401014517h%2Ccen%40cuid_cua_cut%2Ccsrc%40home_box_txt%2Csz%40320_1004%2Cta%40iphone_2_7.0_12_3.4%2Cusm%402&lid=7913127216679347389&order=1&fm=alop&waplogo=1&h5ad=0&tj=www_normal_1_0_10_title&vit=osres&waput=2&cltj=normal_title&asres=1&nt=wnor&title=%E4%BD%8F%E5%BB%BA%E9%83%A8%E8%A1%A8%E6%80%81%3A%E6%A3%9A%E6%94%B9%E4%B8%8D%E4%BC%9A%E5%81%9C%21%E6%9C%AA%E6%9D%A5%E4%B8%89%E5%B9%B4%E9%A9%BB%E9%A9%AC%E5%BA%97%E8%BF%98%E6%9C%89153%E4%B8%AA%E9%A1%B9%E7%9B%AE...&hwj=1576035302470711&dict=-1&wd=&eqid=6dd113472ca5e400100000015b642ce9&w_qd=IlPT2AEptyoA_ykw-Rko9eyx2TJVcZgovkIYiPPS-eA8PQ9vPVJawWm&tcplug=1&sec=31714&di=c61533da0fed84fb&bdenc=1&tch=124.0.85.114.0.0&nsrc=IlPT2AEptyoA_yixCFOxXnANedT62v3IGtiCKiZFLDm6oIjpnPGaUbAsFm_hBy0ESpXGdT0OsBYIwnWh&clk_info=%7B%22srcid%22%3A1599%2C%22tplname%22%3A%22www_normal%22%2C%22t%22%3A1533291773363%2C%22xpath%22%3A%22div-div-div-a-div-img%22%7D" +
      "\u0003" +  // setPageYyId
      "\u0003" +
      "smwp.content-bd.nav-che.1.1533291775216DzLKA3s,,;smwp.content-bd.hdn.5,,;smwp.content-bd.content.6,,;smwp.content-bd.fxe.8,,;smwp.content-bd.1.9,," + //setSpmCntAndPathAndScmCnt
      "\u0003" +  //setSpmPre
      "\u0003" + //setScmCnt
      "\u0003host:10.18.4.40;"; //setExtMap
    val start = new Date().getTime
    sparkTest(ev_log)
    println("It took "+(new Date().getTime -start) + "ms")
  }
  def sparkTest(ev_log:String): Unit ={
    val sparkConf = new SparkConf().setAppName("EvDwdTest").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("ERROR")
    val inputRDD = sc.parallelize( Seq(ev_log) )
    inputRDD.foreachPartition( partition => {
      val kafkaProducer = KafkaSink[String, String](Constants.tc_kafka_cluster)
      val originalEvLogModel_broadcast =  OriginalEvLogModel.getOriginalEvLogModel()
      val newOriginalEvLogHandler_broadcast  = NewOriginalEvLogHandler.getNewOriginalPvLogHandler()
      val odsShmmWebEvDiHandler_broadcast = OdsShmmWebEvDiHandler.getOdsShmmWebEvDiHandler()
      partition.filter(_!=null)
        .flatMap( record => {
          originalEvLogModel_broadcast.setData( filterCheck(record) )
          val odsShmmWebEvDiModel = newOriginalEvLogHandler_broadcast.parsing(originalEvLogModel_broadcast) //rawlog->ods
          val dwdShmmWebEvDiModel = odsShmmWebEvDiHandler_broadcast.parsing(odsShmmWebEvDiModel) //ods->dwd
          val dwdShmmWebEvDiList = dwdShmmWebEvDiModel.getList_data
            dwdShmmWebEvDiList.asScala
        })
        .foreach(dwdShmmWebEvDi =>{
          //..more
          //            kafkaProducer.send(Constants.test_topic, dwdShmmWebEvDi.toString)
          println("[Dwd]="+dwdShmmWebEvDi.toString)
        })
    })

  }


  def filterCheck(record:String): String = {
    val evSplit = record.split(Constants.ORIGINAL_SEPARATOR)
    evSplit(11) = evSplit(11).split("\\;")
      .filter(each =>{
        val c = each.split(",")(0).split("\\.")(2)
        Try(!c.equals("nav-ch") &&
          !c.equals("hdn") &&
          !c.equals("fx") &&
          !c.equals("0"))
          .getOrElse(false)
      }
      ).mkString(";")
    evSplit.mkString(Constants.ORIGINAL_SEPARATOR.toString)
  }

}
