package org.qiunet.flash.handler.handler.mapping;

import org.qiunet.flash.handler.handler.IHandler;
import org.qiunet.flash.handler.handler.http.IHttpHandler;
import org.qiunet.utils.exceptions.SingletonException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局的RequestHandler 一个对应Mapping 类
 * 单例模式
 * @author qiunet
 *         Created on 17/3/3 16:46.
 */
public class RequestHandlerMapping {
	private volatile static RequestHandlerMapping instance;
	/**所有游戏的 handler*/
	private Map<Integer, IHandler> gameHandlers = new HashMap<>();
	/**所有非游戏的 http handler*/
	private Map<String, IHttpHandler> otherHandlers = new HashMap<>();

	private RequestHandlerMapping() {
		synchronized (RequestHandlerMapping.class) {
			if (instance != null ){
				throw new SingletonException("RequestHandlerMapping instance was duplicate");
			}
			instance = this;
		}
	}

	public static RequestHandlerMapping getInstance() {
		if (instance == null) {
			synchronized (RequestHandlerMapping.class) {
				if (instance == null)
				{
					new RequestHandlerMapping();
				}
			}
		}
		return instance;
	}

	/**
	 * 存一个handler对应mapping
	 * @param protocolId
	 * @param handler
	 */
	public void addHandler(int protocolId, IHandler handler) {
		if (this.gameHandlers.containsKey(protocolId)) {
			throw new RuntimeException("protocolId ["+protocolId+"] is already exist!");
		}

		handlerSetRequestDataClass(handler);
		setHandlerField(handler, "protocolId", protocolId);
		this.gameHandlers.put(protocolId, handler);
	}


	/**
	 * 存一个handler对应mapping
	 * @param uriPath
	 * @param handler
	 */
	public void addHandler(String uriPath, IHttpHandler handler) {
		if (this.otherHandlers.containsKey(uriPath)) {
			throw new RuntimeException("uriPath ["+uriPath+"] is already exist!");
		}

		handlerSetRequestDataClass(handler);
		this.otherHandlers.put(uriPath, handler);
	}

	/***
	 * 给handler 设置 requestDataClass 属性
	 * @param handler
	 */
	private void handlerSetRequestDataClass(IHandler handler){
		Class clazz = handler.getClass();
		do {
			if (! (clazz.getGenericSuperclass() instanceof ParameterizedType)) {
				clazz = clazz.getSuperclass();
				continue;
			}

			Class requestDataClass = (Class) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
			setHandlerField(handler, "requestDataClass", requestDataClass);
			break;
		}while (clazz != Object.class);
	}

	/**
	 * 找到 Handler 的requestId field
	 *
	 * @param handler
	 */
	private void setHandlerField(IHandler handler, String fieldName, Object value) {
		Field field = null;
		Class clazz = handler.getClass();
		do {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
			if (field != null) break;

		}while (clazz != Object.class);
		field.setAccessible(true);

		try {
			field.set(handler, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 得到一个游戏的 handler
	 * @param requestId
	 * @return
	 */
	public IHandler getGameHandler(int requestId) {
		return gameHandlers.get(requestId);
	}

	/***
	 * 得到一个非游戏的handler
	 * @return
	 */
	public IHttpHandler getOtherRequestHandler(String uriPath){
		return otherHandlers.get(uriPath);
	}
}