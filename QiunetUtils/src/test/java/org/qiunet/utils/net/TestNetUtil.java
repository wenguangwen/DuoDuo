package org.qiunet.utils.net;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.utils.base.BaseTest;

/***
 *
 * @Author qiunet
 * @Date Create in 2018/6/22 12:05
 **/
public class TestNetUtil  extends BaseTest {

	/**
	 *
	 */
	@Test
	public void testInnerIp(){
		String ip = "192.168.1.200";
		Assert.assertTrue(NetUtil.isInnerIp(ip));

		ip = "172.21.0.9";
		Assert.assertTrue(NetUtil.isInnerIp(ip));

		ip = "123.196.125.13";
		Assert.assertFalse(NetUtil.isInnerIp(ip));

		ip = "10.154.197.234";
		Assert.assertTrue(NetUtil.isInnerIp(ip));

		ip = "127.0.0.1";
		Assert.assertTrue(NetUtil.isLocalIp(ip));
	}

	/**
	 * 得到内网ip
	 */
	@Test
	public void testGetInnerIp(){
		logger.info("内网IP: "+NetUtil.getInnerIp());
	}
}
