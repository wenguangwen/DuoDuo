package org.qiunet.flash.handler.handler.string;

import io.netty.util.CharsetUtil;
import org.qiunet.flash.handler.common.enums.DataType;
import org.qiunet.flash.handler.handler.BaseHttpHandler;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by qiunet.
 * 17/11/21
 */
public abstract class HttpStringHandler extends BaseHttpHandler<String, String> {

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}

	@Override
	public String parseRequestData(byte[] bytes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		return new String(bytes, CharsetUtil.UTF_8);
	}
}