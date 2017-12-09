package org.qiunet.flash.handler.netty.client.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.netty.client.ILongConnClient;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;

import java.net.URI;

/**
 * Created by qiunet.
 * 17/12/1
 */
public class NettyWebsocketClient implements ILongConnClient {
	private static final NioEventLoopGroup group = new NioEventLoopGroup(1);
	private ChannelHandlerContext channelHandlerContext;
	private ILongConnResponseTrigger trigger;

	public NettyWebsocketClient(URI uri, ILongConnResponseTrigger trigger) {
		this.trigger = trigger;

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group);

		NettyClientHandler handler = new NettyClientHandler(
				WebSocketClientHandshakerFactory.newHandshaker(
						uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.TCP_NODELAY,true);
		bootstrap.handler(new NettyWebsocketClient.NettyClientInitializer(handler));
		try {
			ChannelFuture future = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
			handler.handshakeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void sendMessage(MessageContent content){
		ProtocolHeader header = new ProtocolHeader(content.bytes().length, content.getProtocolId(), (int) CrcUtil.getCrc32Value(content.bytes()));
		ByteBuf byteBuf = Unpooled.buffer();
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(content.bytes());
		channelHandlerContext.channel().writeAndFlush(new BinaryWebSocketFrame(byteBuf));
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


	private class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
		private NettyClientHandler handler;
		public NettyClientInitializer(NettyClientHandler handler) {
			this.handler = handler;
		}
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ChannelPipeline pipeline = ch.pipeline();
			pipeline.addLast(new HttpClientCodec(),
					new HttpObjectAggregator(1024*1024*2),
					WebSocketClientCompressionHandler.INSTANCE,
					handler);
		}
	}


	private class NettyClientHandler extends ChannelInboundHandlerAdapter {
		private WebSocketClientHandshaker handshaker;
		private ChannelPromise handshakeFuture;

		public ChannelFuture handshakeFuture() {
			return handshakeFuture;
		}

		public NettyClientHandler(WebSocketClientHandshaker handshaker){
			this.handshaker = handshaker;
		}


		@Override
		public void handlerAdded(ChannelHandlerContext ctx) {
			handshakeFuture = ctx.newPromise();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) {
			channelHandlerContext = ctx;
			handshaker.handshake(ctx.channel());
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) {
			System.out.println("WebSocket Client disconnected!");
		}


		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if (!handshaker.isHandshakeComplete()) {
				try {
					handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
					System.out.println("WebSocket Client connected!");
					handshakeFuture.setSuccess();
				} catch (WebSocketHandshakeException e) {
					System.out.println("WebSocket Client failed to connect");
					handshakeFuture.setFailure(e);
				}
				return;
			}

			if (msg instanceof FullHttpResponse) {
				throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + ((FullHttpResponse) msg).status());
			}

			if (! (msg instanceof BinaryWebSocketFrame)) {
				System.out.println("Not a Binary WebSocket Frame");
				return;
			}

			BinaryWebSocketFrame webSocketFrame = ((BinaryWebSocketFrame) msg);
			ProtocolHeader header = new ProtocolHeader(webSocketFrame.content());
			byte [] bytes = new byte[webSocketFrame.content().readableBytes()];
			webSocketFrame.content().readBytes(bytes);
			trigger.response(new MessageContent(header.getProtocolId() ,bytes));
		}
	}
}
