package org.qiunet.data.support;

import org.qiunet.data.cache.entity.ICacheEntity;
import org.qiunet.data.core.support.cache.LocalCache;
import org.qiunet.data.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.core.select.DbParamMap;


public class CacheDataSupport<Key, Do extends ICacheEntity<Key, Bo>, Bo extends IEntityBo<Do>> extends BaseCacheDataSupport<Do, Bo> {
	/**防止缓存击穿的 NULL值*/
	private Bo NULL;
	/**保存的cache*/
	private LocalCache<Key, Bo> cache = new LocalCache<>();

	public CacheDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
		this.NULL = supplier.get(defaultDo);
	}


	@Override
	protected void invalidateCache(Do aDo) {
		cache.invalidate(aDo.key());
	}

	@Override
	protected void deleteDoFromDb(Do aDo) {
		DbParamMap map = DbParamMap.create().put(defaultDo.keyFieldName(), aDo.key());
		DefaultDatabaseSupport.getInstance().delete(deleteStatement, map);
	}

	@Override
	protected void addToCache(Bo bo) {
		Key key = bo.getDo().key();
		if (! cache.replace(key, NULL, bo)) {
			Bo newBo = this.cache.putIfAbsent(key, bo);
			if (newBo != null && newBo != bo) {
				throw new RuntimeException("bo exist, and status is ["+ newBo.getDo().entityStatus()+"]");
			}
		}
	}

	/***
	 * 对外提供Bo对象
	 * @param key
	 * @return
	 */
	public Bo getBo(Key key) {
		Bo bo = cache.get(key);
		if (bo == NULL) return null;

		if (bo == null) {
			DbParamMap map = DbParamMap.create().put(defaultDo.keyFieldName(), key);

			Do aDo = DefaultDatabaseSupport.getInstance().selectOne(selectStatement, map);
			if (aDo == null) {
				cache.putIfAbsent(key, NULL);
				return null;
			}

			aDo.updateEntityStatus(EntityStatus.NORMAL);
			bo = cache.putIfAbsent(key, supplier.get(aDo));
		}
		return bo;
	}

	/***
	 * 对指定key的缓存失效
	 * @param keys
	 */
	public void invalidate(Key... keys) {
		cache.invalidateAll(keys);
	}
}
