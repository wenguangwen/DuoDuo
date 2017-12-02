package org.qiunet.flash.handler.netty.server.param;

import org.qiunet.flash.handler.context.session.DefaultSessionBuilder;
import org.qiunet.flash.handler.context.session.DefaultSessionEvent;
import org.qiunet.flash.handler.context.session.ISessionBuilder;
import org.qiunet.flash.handler.context.session.ISessionEvent;
import org.qiunet.flash.handler.netty.server.interceptor.TcpInterceptor;
import org.qiunet.flash.handler.netty.server.tcp.error.IClientErrorMessage;

/**
 * 使用引导类 参数.
 * 建造者模式
 * Created by qiunet.
 * 17/7/19
 */
public final class TcpBootstrapParams extends AbstractBootstrapParam {
	private TcpInterceptor tcpInterceptor;

	private ISessionEvent sessionEvent;

	private ISessionBuilder sessionBuilder;
	// 一些定义好的错误消息.
	private IClientErrorMessage errorMessage;

	private TcpBootstrapParams(){}

	public ISessionEvent getSessionEvent() {
		return sessionEvent;
	}

	public TcpInterceptor getTcpInterceptor() {
		return tcpInterceptor;
	}

	public IClientErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public ISessionBuilder getSessionBuilder() {
		return sessionBuilder;
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
	 */
	public static class Builder extends SuperBuilder<TcpBootstrapParams, Builder> {
		private ISessionBuilder sessionBuilder = new DefaultSessionBuilder();

		private ISessionEvent sessionEvent = new DefaultSessionEvent();

		private IClientErrorMessage errorMessage;

		private TcpInterceptor tcpInterceptor;

		private Builder(){}

		public Builder setSessionEvent(ISessionEvent sessionEvent) {
			this.sessionEvent = sessionEvent;
			return this;
		}

		public Builder setSessionBuilder(ISessionBuilder sessionBuilder) {
			this.sessionBuilder = sessionBuilder;
			return this;
		}

		public Builder setTcpInterceptor(TcpInterceptor interceptor) {
			this.tcpInterceptor = interceptor;
			return this;
		}

		public Builder setErrorMessage(IClientErrorMessage errorMessage) {
			this.errorMessage = errorMessage;
			return this;
		}

		@Override
		protected TcpBootstrapParams newParams() {
			return new TcpBootstrapParams();
		}

		@Override
		protected void buildInner(TcpBootstrapParams tcpBootstrapParams) {
			if (tcpInterceptor == null) throw new NullPointerException("Interceptor can not be Null");
			tcpBootstrapParams.sessionBuilder = this.sessionBuilder;
			tcpBootstrapParams.errorMessage = this.errorMessage;
			tcpBootstrapParams.sessionEvent = this.sessionEvent;
			tcpBootstrapParams.tcpInterceptor = this.tcpInterceptor;
		}
	}
}
