package org.qiunet.data.redis;

import org.junit.Assert;
import org.junit.Test;
import org.qiunet.data.redis.util.RedisDataUtil;
import org.qiunet.data.support.RedisDataListSupport;
import org.qiunet.utils.threadLocal.ThreadContextData;

import java.util.Map;

public class TestRedisDataListSupport {
	private static RedisDataListSupport<Long, Integer, EquipDo, EquipBo> dataListSupport = new RedisDataListSupport<>(RedisDataUtil.getInstance(), EquipDo.class, EquipBo::new);

	private long uid = 100000;
	@Test
	public void testNormalList(){
		EquipDo equipDo1 = new EquipDo();
		equipDo1.setUid(uid);
		equipDo1.setEquip_id(1);
		equipDo1.setLevel(1);
		equipDo1.insert();

		EquipDo equipDo2 = new EquipDo();
		equipDo2.setUid(uid);
		equipDo2.setEquip_id(2);
		equipDo2.setLevel(2);
		equipDo2.insert();

		dataListSupport.syncToDatabase();

		equipDo2.setLevel(3);
		equipDo2.update();
		dataListSupport.syncToDatabase();
		this.expire(uid);

		Map<Integer, EquipBo> map = dataListSupport.getBoMap(uid);
		Assert.assertEquals(2, map.size());
		Assert.assertEquals(3, map.get(2).getDo().getLevel());

		map.values().forEach(EquipBo::delete);
		dataListSupport.syncToDatabase();


		map = dataListSupport.getBoMap(uid);
		Assert.assertTrue( map.isEmpty());
	}

	@Test
	public void testEntityListHit(){
		long uid = 100000000;
		Map<Integer, EquipBo> map = dataListSupport.getBoMap(uid);
		Assert.assertTrue(map.isEmpty());
		for (int i = 0; i < 3; i++) {
			ThreadContextData.removeAll();
			map = dataListSupport.getBoMap(uid);
			Assert.assertTrue(map.isEmpty());
		}

		EquipDo equipDo1 = new EquipDo();
		equipDo1.setUid(uid);
		equipDo1.setEquip_id(1);
		equipDo1.setLevel(1);
		equipDo1.insert();

		ThreadContextData.removeAll();
		map = dataListSupport.getBoMap(uid);
		Assert.assertFalse(map.isEmpty());

		this.expire(uid);
	}

	public void expire(long uid) {
		String redisKey = "EquipDo#"+uid;
		RedisDataUtil.jedis().expire(redisKey, 0);
		ThreadContextData.removeKey(redisKey);
	}
}
