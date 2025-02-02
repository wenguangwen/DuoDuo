package org.qiunet.cfg.base;

/**
 * 简单的一层map的接口
 * Map<key, Cfg>
 * Created by qiunet.
 * 17/6/3
 */
public interface ISimpleMapConfig<ID> {
	/**
	 * 得到key
	 * @return
	 */
	public ID getId();
}
