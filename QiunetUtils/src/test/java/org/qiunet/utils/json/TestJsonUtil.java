package org.qiunet.utils.json;
import org.qiunet.utils.base.BaseTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qiunet
 *         Created on 16/11/5 20:45.
 */
public class TestJsonUtil extends BaseTest{
	@Test
	public void testGetGeneralObject(){
		Map<String,Object> map = new HashMap();
		map.put("qiunet", "qiuyang");

		String json = JsonUtil.toJsonString(map);
		logger.info(json);

		Map<String,Object> ret = JsonUtil.getGeneralObject(json, Map.class);
		Assert.assertEquals("qiuyang", ret.get("qiunet"));
	}
	@Test
	public void testGetGeneralList(){
		List<Integer> ls = new ArrayList();
		ls.add(1);
		ls.add(2);
		ls.add(3);

		String json = JsonUtil.toJsonString(ls);
		logger.info(json);
		List<Integer> ret = JsonUtil.getGeneralList(json, Integer.class);
		Assert.assertTrue(ret.size() == 3);
		Assert.assertTrue(ret.contains(3));
		Assert.assertFalse(ret.contains(5));
	}
	@Test
	public void testRefJson(){
		Map<Integer, Map<String, String>> map = new HashMap<>();
		Map<String, String> subMap = new HashMap<>();
		subMap.put("ATK", "aa");
		subMap.put("DEF", "bb");
		map.put(1, subMap);
		map.put(2, subMap);
		map.put(3, subMap);

		String json = JsonUtil.toJsonString(map);
		Assert.assertEquals("{1:{\"DEF\":\"bb\",\"ATK\":\"aa\"},2:{\"DEF\":\"bb\",\"ATK\":\"aa\"},3:{\"DEF\":\"bb\",\"ATK\":\"aa\"}}", json);
		map = JsonUtil.getGeneralObject(json, Map.class);
		subMap = map.get(2);
		Assert.assertNotNull(subMap);
		Assert.assertEquals("bb", subMap.get("DEF"));
	}
}
