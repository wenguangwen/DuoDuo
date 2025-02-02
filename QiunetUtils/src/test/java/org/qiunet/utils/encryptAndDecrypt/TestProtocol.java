package org.qiunet.utils.encryptAndDecrypt;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by qiunet.
 * 17/10/11
 */
public class TestProtocol {
	@Test
	public void testProtocol() throws UnsupportedEncodingException {
		String str = "我们都是中国人, 热爱祖国, 热爱人民. ";
		byte [] srcBytes = str.getBytes(Charset.forName("Utf-8"));
		short chunkSize = 10;
		byte [] secritBytes = ProtocolUtil.encryptData(srcBytes, chunkSize);
		byte [] originBytes = ProtocolUtil.decryptData(secritBytes, chunkSize);
		String originStr = new String(originBytes, "Utf-8");
		Assert.assertEquals(originStr, str);
	}
}
