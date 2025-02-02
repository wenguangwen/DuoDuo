package org.qiunet.flash.handler.context.status;

/**
 * Created by qiunet.
 * 17/12/9
 */
public enum  BaseGameStatus implements IGameStatus {
	SUCCESS(1, "成功"),
	TOKEN_ERROR(2, "TOKEN错误"),
	MAINTENANCE(3, "服务器维护中"),
	BAN(4, "被封号, 禁止登陆了"),
	FAST_REQUEST(5, "请求频繁"),

	PARAMS_ERROR(10, "参数错误"),

	HANdLER_NOT_FOUND(404, "没有Cmdid对应的RequestHandler"),
	EXCEPTION(500, "服务器出现问题了"),
	;
	private int status;
	private String desc;
	private BaseGameStatus(int status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getDesc() {
		return desc;
	}
}
