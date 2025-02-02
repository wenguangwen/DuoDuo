package org.qiunet.entity2table.service.impl;

import org.qiunet.entity2table.command.Columns;
import org.qiunet.entity2table.service.CreateTableService;
import org.qiunet.data.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data.core.support.db.MoreDbSourceDatabaseSupport;
import org.qiunet.data.redis.constants.RedisDbConstants;
import org.qiunet.data.util.DbProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CreateTableServiceImpl implements CreateTableService {

	private static final String sqlPath = "org.qiunet.entity2table.service.CreateTableService.";

	@Override
	public void createTable(Map<String, List<Object>> tableMap, String tableComment) {

		/*List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableComment", tableComment);
			map.put("tableMap", tableMap);

			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "createTable", map);
		}*/

		//判断当前项目的数据源是单一数据源还是多个数据源,根据数据源不同,执行不同的处理, 暂不考虑分表的情况
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);

				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("tableComment", tableComment);
				paramMap.put("tableMap", tableMap);
				paramMap.put("dbName", RedisDbConstants.DB_NAME_PREFIX + dbIndex);

				MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "createTable", paramMap);
			}
		} else {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("tableComment", tableComment);
			paramMap.put("tableMap", tableMap);
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "createTable", paramMap);
		}
	}

	@Override
	public int findTableCountByTableName(String tableName) {

		/*String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(0);

		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(0));

		return DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "findTableCountByTableName", map);*/

		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String dbSource = String.valueOf(0 / RedisDbConstants.DB_SIZE_PER_INSTANCE);
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + 0);

			return MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "findTableCountByTableName", map);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			return DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "findTableCountByTableName", map);
		}

	}

	@Override
	public List<Columns> findTableEnsembleByTableName(String tableName) {

		/*String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(0);

		Map<String, Object> map = new HashMap<>();
		map.put("tableName", tableName);
		map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(0));
		return DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectList(sqlPath + "findTableEnsembleByTableName", map);*/

		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String dbSource = String.valueOf(0 / RedisDbConstants.DB_SIZE_PER_INSTANCE);

			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + 0);
			return MoreDbSourceDatabaseSupport.getInstance(dbSource).selectList(sqlPath + "findTableEnsembleByTableName", map);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			return DefaultDatabaseSupport.getInstance().selectList(sqlPath + "findTableEnsembleByTableName", map);
		}
	}

	@Override
	public void addTableField(Map<String, Object> tableMap) {
		/*List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));

			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "addTableField", map);
		}*/
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				Map<String, Object> map = new HashMap<>();
				map.put("tableMap", tableMap);
				map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + dbIndex);

				MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "addTableField", map);
			}
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "addTableField", map);
		}
	}

	@Override
	public void removeTableField(Map<String, Object> tableMap) {
		/*List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));

			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "removeTableField", map);
		}*/
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				Map<String, Object> map = new HashMap<>();
				map.put("tableMap", tableMap);
				map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + dbIndex);

				MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "removeTableField", map);
			}
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);

			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "removeTableField", map);
		}
	}

	@Override
	public void modifyTableField(Map<String, Object> tableMap) {
		/*List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "modifyTableField", map);
		}*/
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				Map<String, Object> map = new HashMap<>();
				map.put("tableMap", tableMap);
				map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "modifyTableField", map);
			}
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "modifyTableField", map);
		}
	}

	@Override
	public void dropKeyTableField(Map<String, Object> tableMap) {
		/*List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "dropKeyTableField", map);
		}*/
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);

				Map<String, Object> map = new HashMap<>();
				map.put("tableMap", tableMap);
				map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "dropKeyTableField", map);
			}
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "dropKeyTableField", map);
		}
	}

	@Override
	public void dropUniqueTableField(Map<String, Object> tableMap) {
		/*List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);
			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "dropUniqueTableField", map);
		}*/
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				Map<String, Object> map = new HashMap<>();
				map.put("tableMap", tableMap);
				map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "dropUniqueTableField", map);
			}
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableMap", tableMap);
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "dropUniqueTableField", map);
		}

	}

	@Override
	public void dorpTableByName(String tableName) {
		/*List<Integer> dbIndexList = DbProperties.getInstance().getDbIndexList();
		for (Integer dbIndex : dbIndexList) {
			String dbSourceKey = DbProperties.getInstance().getDataSourceKeyByDbIndex(dbIndex);

			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(dbIndex));
			DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "dorpTableByName", map);
		}*/
		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			for (int dbIndex = 0; dbIndex < RedisDbConstants.MAX_DB_COUNT; dbIndex++) {
				String dbSource = String.valueOf(dbIndex / RedisDbConstants.DB_SIZE_PER_INSTANCE);
				Map<String, Object> map = new HashMap<>();
				map.put("tableName", tableName);
				map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + dbIndex);
				MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "dorpTableByName", map);
			}
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("tableName", tableName);
			DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "dorpTableByName", map);
		}

	}

	@Override
	public Integer getTableCount() {
		/*Map<String, Object> map = new HashMap<>();
		map.put("dbName", DbProperties.getInstance().getDbNameByDbIndex(0));
		return DatabaseSupport.getInstance().getSqlSession(dbSourceKey).selectOne(sqlPath + "getTableCount", map);*/

		if (DbProperties.getInstance().containKey(RedisDbConstants.DB_SIZE_PER_INSTANCE_KEY)) {
			String dbSource = String.valueOf(0 / RedisDbConstants.DB_SIZE_PER_INSTANCE);
			Map<String, Object> map = new HashMap<>();
			map.put("dbName", RedisDbConstants.DB_NAME_PREFIX + 0);
			return MoreDbSourceDatabaseSupport.getInstance(dbSource).selectOne(sqlPath + "getTableCount", map);
		} else {
			Map<String, Object> map = new HashMap<>();
			return DefaultDatabaseSupport.getInstance().selectOne(sqlPath + "getTableCount", map);
		}
	}

}
