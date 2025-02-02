package org.qiunet.flash.handler.context.request.attribute;

/**
 * 可以附带存储  get  set 的对象接口
 * Created by qiunet.
 * 17/11/20
 */
public interface IAttributeData {
	/**
	 * 得到对象
	 * @param key
	 * @return
	 */
	public Object getAttribute(String key);
	/**
	 * 得到对象
	 * @param key
	 * @return
	 */
	public void setAttribute(String key, Object val);
}
