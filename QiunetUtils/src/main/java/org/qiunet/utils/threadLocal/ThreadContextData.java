package org.qiunet.utils.threadLocal;

import java.util.HashMap;
import java.util.Map;
/***
 * 利用线程变量存储些东西
 * @author qiunet
 */
public class ThreadContextData {
	private ThreadContextData(){}

	/**servlet 请求 上下文*/
	private static final ThreadLocal<Map<String,Object>> servletRequestContext =  new MyThreadLocal();
	/**
	 * 得到本地线程变量里面的数据
	 * @return
	 */
	private static Map<String,Object> getThreadParamMap(){
		return servletRequestContext.get();
	}

	/**
	 * 很多时候联调使用.
	 * 查看线程是否为null
	 * @return
	 */
	public boolean isEmpty(){
		return getThreadParamMap().isEmpty();
	}
	/**
	 * 往线程变量存放一个对象.
	 * @param key
	 * @param val
	 */
	public static void put(String key ,Object val){
		if(val == null) val = "NULL";
		getThreadParamMap().put(key, val);
	}
	/**
	 * 从线程变量获取一个已有对象.
	 * @param key
	 * @return
	 */
	public static <T> T get(String key){
		Object val = getThreadParamMap().get(key);
		if("NULL".equals(val)) return null;
		return (T) val;
	}

	/**
	 * 移除指定key
	 * @param key
	 */
	public static void removeKey(String key) {
		getThreadParamMap().remove(key);
	}
	/**
	 * 清除线程变量
	 */
	public static void removeAll(){
		servletRequestContext.remove();
	}


	private static class MyThreadLocal extends ThreadLocal<Map<String, Object>> {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap(32);
		}
	}
}
