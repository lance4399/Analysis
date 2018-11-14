package utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: xiliang
 * @Create: 2018/8/10 19:10
 */
public class ConfigurationManager {

	private static Properties prop = new Properties();
	
	/**
	 * Java中，每一个类第一次使用的时候，就会被Java虚拟机（JVM）中的类加载器去从磁盘上的.class文件中
	 * 加载出来，然后为每个类都会构建一个Class对象，就代表了这个类
	 * 类第一次使用的时候，就会加载，加载的时候，就会初始化类，初始化类的时候就会执行类的静态代码块就是类的初始化在整个JVM生命周期内
	 * 配置文件只会加载一次，然后以后就是重复使用，效率比较高；不用反复加载多次
	 */
	static {
		try {
			InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream("my.properties");
			prop.load(in);  
		} catch (Exception e) {
			e.printStackTrace();  
		}
	}

	public static String getProperty(String key) {
		return prop.getProperty(key);
	}

	public static Integer getInteger(String key) {
		String value = getProperty(key);
		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Boolean getBoolean(String key) {
		String value = getProperty(key);
		try {
			return Boolean.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Long getLong(String key) {
		String value = getProperty(key);
		try {
			return Long.valueOf(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}
	
}
