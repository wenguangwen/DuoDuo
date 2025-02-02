package org.qiunet.data.cache;

import org.apache.ibatis.type.Alias;
import org.qiunet.data.cache.entity.CacheEntity;

@Alias("GuildDo")
public class GuildDo extends CacheEntity<Long, GuildBo> {
	private long guildId;
	private String name;
	private int level;

	public long getGuildId() {
		return guildId;
	}

	public void setGuildId(long guildId) {
		this.guildId = guildId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public Long key() {
		return guildId;
	}

	@Override
	public String keyFieldName() {
		return "guildId";
	}
}
