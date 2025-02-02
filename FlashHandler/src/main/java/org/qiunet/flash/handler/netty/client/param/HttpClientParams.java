package org.qiunet.flash.handler.netty.client.param;

import org.qiunet.flash.handler.common.enums.HandlerType;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * 使用引导类 参数.
 * 该模式通用 websocket
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public class HttpClientParams extends AbstractClientParam {
	/**
	 * 是否使用ssl
	 */
	private boolean ssl;
	/***
	 * 如果uri 匹配, 则会在字节数据前加入 IProtocolHeader 信息
	 */
	private String uriIPath;

	private HttpClientParams(){}

	public boolean isSsl() {
		return ssl;
	}

	public String getUriIPath() {
		return uriIPath;
	}

	public URI getURI(){
		return getURI(this.uriIPath);
	}

	public URI getURI(String pathAndParam){
		StringBuilder sb = new StringBuilder("http");
		if (ssl) sb.append("s");
		sb.append("://").append(address.getHostString());
		if ((ssl && address.getPort() != 443)
			|| (!ssl && address.getPort() != 80)) {
			sb.append(":").append(address.getPort());
		}
		if (! pathAndParam.startsWith("/")) pathAndParam = "/" + pathAndParam;
		sb.append(pathAndParam);
		return URI.create(sb.toString());
	}

	@Override
	public HandlerType getHandlerType() {
		return HandlerType.HTTP;
	}
	/***
	 * 得到
	 * @return
	 */
	public static Builder custom(){
		return new Builder();
	}
	/***
	 * 使用build模式 set和 get 分离. 以后有有顺序的构造时候也可以不动
	 * */
	public static class Builder extends SuperBuilder<HttpClientParams, Builder> {
		private Builder(){}

		private boolean ssl = false;

		private String uriPath = "/f";

		public Builder setUriPath(String uriPath) {
			this.uriPath = uriPath;
			return this;
		}

		public Builder setSsl(boolean ssl) {
			this.ssl = ssl;
			return this;
		}

		@Override
		protected HttpClientParams newParams() {
			return new HttpClientParams();
		}

		@Override
		protected void buildInner(HttpClientParams params) {
			params.ssl = ssl;
			params.uriIPath = uriPath;
		}
	}
}
