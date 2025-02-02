package org.qiunet.data.support;

import org.qiunet.data.async.SyncType;
import org.qiunet.data.cache.entity.ICacheEntity;
import org.qiunet.data.cache.status.EntityStatus;
import org.qiunet.data.core.support.db.DefaultDatabaseSupport;
import org.qiunet.data.util.DbProperties;

import java.util.concurrent.ConcurrentLinkedQueue;

abstract class BaseCacheDataSupport<Do extends ICacheEntity, Bo extends IEntityBo<Do>> extends BaseDataSupport<Do, Bo> {
	/***对Entity 的操作 **/
	private enum  EntityOperate {INSERT, UPDATE, DELETE}

	protected boolean async = DbProperties.getInstance().getSyncType() == SyncType.ASYNC;
	/***有同步需求的 key*/
	protected ConcurrentLinkedQueue<SyncEntityElement> syncKeyQueue = new ConcurrentLinkedQueue<>();

	protected BaseCacheDataSupport(Class<Do> doClass, BoSupplier<Do, Bo> supplier) {
		super(doClass, supplier);
	}

	@Override
	public void syncToDatabase() {
		if (!async) return;

		SyncEntityElement element;
		while ((element = syncKeyQueue.poll()) != null) {
			Do aDo = element.aDo;
			if (element.operate != EntityOperate.DELETE
				&& aDo.entityStatus() == EntityStatus.DELETE) {
				// 队列后面有delete 操作. 留给delete 操作就行.
				continue;
			}
			switch (element.operate) {
				case INSERT:
					if (aDo.atomicSetEntityStatus(EntityStatus.INSERT, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().insert(insertStatement, aDo);
					}else {
						logger.error("Entity status ["+ aDo.entityStatus()+"] is error, can not insert to db.");
					}
					break;
				case UPDATE:
					if (aDo.atomicSetEntityStatus(EntityStatus.UPDATE, EntityStatus.NORMAL)) {
						DefaultDatabaseSupport.getInstance().update(updateStatement, aDo);
					}else {
						logger.error("Entity status ["+ aDo.entityStatus()+"] is error, can not update to db.");
					}
					break;
				case DELETE:
					this.deleteDoFromDb(aDo);
					break;
				default:
					throw new RuntimeException("Db Sync Not Support status: [" + aDo.entityStatus() + "]");
			}
		}
	}
	/**
	 * 插入
	 * @param aDo
	 * @return
	 */
	@Override
	public Bo insert(Do aDo) {
		Bo bo = supplier.get(aDo);

		if (aDo.atomicSetEntityStatus(EntityStatus.INIT, EntityStatus.INSERT)){
			if (! async) {
				DefaultDatabaseSupport.getInstance().insert(insertStatement, aDo);
				aDo.updateEntityStatus(EntityStatus.NORMAL);
			}else {
				syncKeyQueue.add(this.syncQueueElement(aDo, EntityOperate.INSERT));
			}
			this.addToCache(bo);
		} else {
			throw new RuntimeException("entity ["+doClass.getName()+"] status ["+ aDo.entityStatus()+"] is error. Not executor insert!");
		}
		return bo;
	}

	/**
	 * 由子类插入缓存中
	 * @param bo
	 */
	protected abstract void addToCache(Bo bo);
	/***
	 * 更新
	 * @param aDo
	 * @return
	 */
	@Override
	public void update(Do aDo) {
		if (aDo.entityStatus() == EntityStatus.INIT) {
			throw new RuntimeException("Entity must insert first!");
		}

		if (! async) {
			DefaultDatabaseSupport.getInstance().update(updateStatement, aDo);
			return;
		}

		if (aDo.atomicSetEntityStatus(EntityStatus.NORMAL, EntityStatus.UPDATE)){
			syncKeyQueue.add(this.syncQueueElement(aDo, EntityOperate.UPDATE));
		}
		// update 可能update在其它状态的po 所以不需要error打印.
		// insert update 和 delete 状态都不需要操作了
	}

	/**
	 * 删除
	 * @param aDo
	 */
	@Override
	public void delete(Do aDo) {
		if (aDo.entityStatus() == EntityStatus.INIT) {
			throw new RuntimeException("Delete entity ["+doClass.getName()+"] It's not insert, Can't delete!");
		}

		if (aDo.entityStatus() == EntityStatus.DELETE) {
			throw new RuntimeException("Delete entity ["+doClass.getName()+"] double times!");
		}

		// 直接删除缓存. 异步更新时候, 不校验状态
		this.invalidateCache(aDo);
		aDo.updateEntityStatus(EntityStatus.DELETE);

		if (! async) {
			this.deleteDoFromDb(aDo);
		}else {
			syncKeyQueue.add(this.syncQueueElement(aDo, EntityOperate.DELETE));
		}
	}

	/**
	 * 获得同步队列的key
	 * @param aDo
	 * @return
	 */
	private SyncEntityElement syncQueueElement(Do aDo, EntityOperate operate){
		return new SyncEntityElement(aDo, operate);
	}
	/***
	 * 对某个对象失效
	 * @param aDo
	 */
	protected abstract void invalidateCache(Do aDo);

	/***
	 * 从数据库删除do
	 * @param aDo
	 */
	protected abstract void deleteDoFromDb(Do aDo);
	/**
	 * 队列的对象
	 */
	protected class SyncEntityElement {
		private Do aDo;
		private EntityOperate operate;

		protected SyncEntityElement(Do aDo, EntityOperate operate) {
			this.aDo = aDo;
			this.operate = operate;
		}
	}
}
