package org.qiunet.flash.handler.gamecfg;

/**
 * 嵌套map的接口
 * Created by qiunet.
 * 17/6/3
 */
public interface INestMapConfig<Key, SubKey> {

	public Key getKey();

	public SubKey getSubKey();
}