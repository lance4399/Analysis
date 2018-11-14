package spark;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import entity.Student;

/**
 * @Author: xiliang
 * @Date: 2018/8/24 14:24
 **/

public class test {
    public static void main(String[] args){
        Student student =new Student(1,"lance","male",26);
        String stuStr = student.toString();
        JSONObject object = JSON.parseObject(stuStr);
        System.out.println(object.getInteger(0));
    }
}
