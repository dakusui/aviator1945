package example;

import java.util.Iterator;
import java.util.Properties;

public class Test {
	protected static Class<? extends Test> runner = Test.class;
	public static void main(String[] args) {
		Properties props = System.getProperties();
		Iterator<Object> keys = props.keySet().iterator();
		while (keys.hasNext()) { 
			String cur = (String) keys.next();
			System.out.println(cur + ":" + props.getProperty(cur));
		}
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			System.out.println(trace[i].getClassName());
		}
		
		System.out.println("<==" + runner + "==>");
		
	}
}
