package org.qiunet.flash.handler.netty.server.idle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.qiunet.flash.handler.context.session.ISession;
import org.qiunet.flash.handler.context.session.SessionManager;
import org.qiunet.utils.logger.LoggerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 检查 如果超时 , 关闭channel
 */
public class NettyIdleCheckHandler extends ChannelInboundHandlerAdapter {
	private static Logger logger = LoggerType.DUODUO.getLogger();
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent && ((IdleStateEvent) evt).state() == IdleState.READER_IDLE) {
			ISession session = SessionManager.getInstance().getSession(ctx.channel());
			logger.info("[NettyIdleCheckHandler] Uid ["+(session == null ? 0 : session.getUid())+"] Channel ["+ctx.channel().id()+"] close by idle check");
			ctx.channel().close();
		}
	}
}
