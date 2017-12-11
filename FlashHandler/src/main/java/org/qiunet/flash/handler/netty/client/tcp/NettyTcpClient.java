package org.qiunet.flash.handler.netty.client.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.coder.Decoder;
import org.qiunet.flash.handler.netty.coder.Encoder;
import org.qiunet.utils.nonSyncQuene.factory.DefaultThreadFactory;

import java.net.InetSocketAddress;

/**
 * Created by qiunet.
 * 17/11/25
 */
public class NettyTcpClient implements ILongConnClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("netty-tcp-client-event-loop-"));
	private ChannelHandlerContext channelHandlerContext;
	private ILongConnResponseTrigger trigger;

	public NettyTcpClient(InetSocketAddress address, ILongConnResponseTrigger trigger) {
		this.trigger = trigger;
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyClientInitializer());
		try {
			ChannelFuture future = bootstrap.connect(address).sync();
			future.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendMessage(MessageContent content){
		channelHandlerContext.channel().writeAndFlush(content);
	}
	@Override
	public void close(){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		channelHandlerContext.channel().close();
	}

	public static void shutdown(){
		if (! group.isShutdown()) group.shutdownGracefully();
	}

	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast("Encoder", new Encoder());
			pipeline.addLast("Decoder", new Decoder(1024*1024*2, true));
			pipeline.addLast(new NettyClientHandler());
		}
	}

	private class NettyClientHandler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			channelHandlerContext = ctx;
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			trigger.response(((MessageContent) msg));
		}
	}
}
