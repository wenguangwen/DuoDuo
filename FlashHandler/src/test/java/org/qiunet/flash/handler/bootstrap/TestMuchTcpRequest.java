package org.qiunet.flash.handler.bootstrap;

import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Test;
import org.qiunet.flash.handler.common.message.MessageContent;
import org.qiunet.flash.handler.handler.proto.LoginProto;
import org.qiunet.flash.handler.netty.client.param.TcpClientParams;
import org.qiunet.flash.handler.netty.client.trigger.ILongConnResponseTrigger;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;

/**
 * 大量请求的泄漏测试
 * Created by qiunet.
 * 17/11/27
 */
public class TestMuchTcpRequest extends MuchTcpRequest {
	private int requestCount = 1000000;
	private CountDownLatch latch = new CountDownLatch(requestCount);
	@Test
	public void muchRequest() throws InterruptedException {
		long start = System.currentTimeMillis();
		final int threadCount = 100;
		for (int j = 0; j < threadCount; j++) {
			new Thread(() -> {
				NettyTcpClient tcpClient = null;
				try {
					tcpClient = new NettyTcpClient(
						TcpClientParams.custom()
							.setAddress(new InetSocketAddress(InetAddress.getByName(host), port))
							.build()
						, new Trigger());
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				int count = requestCount/threadCount;

				for (int i = 0 ; i < count; i ++) {
					String text = "test [testTcpProtobuf]: "+i;
					LoginProto.LoginRequest request = LoginProto.LoginRequest.newBuilder().setTestString(text).build();
					MessageContent content = new MessageContent(1004, request.toByteArray());
					tcpClient.sendMessage(content);
				}
			}).start();
		}

		latch.await();
		long end = System.currentTimeMillis();
		System.out.println("All Time is:["+(end - start)+"]ms");
	}

	public class Trigger implements ILongConnResponseTrigger {
		@Override
		public void response(MessageContent data) {
			LoginProto.LoginResponse response = null;
			try {
				response = LoginProto.LoginResponse.parseFrom(data.bytes());
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
			System.out.println(response.getTestString());
			latch.countDown();
		}
	}
}
