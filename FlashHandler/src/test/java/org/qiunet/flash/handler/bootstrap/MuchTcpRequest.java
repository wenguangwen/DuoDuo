package org.qiunet.flash.handler.bootstrap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.qiunet.flash.handler.bootstrap.error.DefaultErrorMessage;
import org.qiunet.flash.handler.bootstrap.hook.MyHook;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.handler.mapping.RequestHandlerScanner;
import org.qiunet.flash.handler.interceptor.DefaultTcpInterceptor;
import org.qiunet.flash.handler.netty.client.tcp.ITcpResponseTrigger;
import org.qiunet.flash.handler.netty.client.tcp.NettyTcpClient;
import org.qiunet.flash.handler.netty.server.BootstrapServer;
import org.qiunet.flash.handler.netty.server.hook.Hook;
import org.qiunet.flash.handler.netty.server.param.TcpBootstrapParams;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by qiunet.
 * 17/11/27
 */
public abstract class MuchTcpRequest extends RequestHandlerScanner implements ITcpResponseTrigger {
	protected static String host = "localhost";
	protected static int port = 8889;
	protected static Hook hook = new MyHook();
	protected NettyTcpClient tcpClient;
	private static Thread currThread;
	@BeforeClass
	public static void init(){
		currThread = Thread.currentThread();
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				TcpBootstrapParams tcpParams = TcpBootstrapParams.custom()
						.setTcpInterceptor(new DefaultTcpInterceptor())
						.setErrorMessage(new DefaultErrorMessage())
						.setPort(port)
						.build();
				BootstrapServer server = BootstrapServer.createBootstrap(hook).tcpListener(tcpParams);
				LockSupport.unpark(currThread);
				server.await();
			}
		});
		thread.start();
		LockSupport.park();
	}
	@Before
	public void connect(){
		currThread = Thread.currentThread();
		try {
			tcpClient = new NettyTcpClient(new InetSocketAddress(InetAddress.getByName(host), port), this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	@After
	public void closeConnect(){
		tcpClient.close();
	}

	@Override
	public void response(MessageContent data) {
		this.responseTcpMessage(data);
	}

	protected abstract void responseTcpMessage(MessageContent data);

	@AfterClass
	public static void shutdown() throws InterruptedException {
		BootstrapServer.sendHookMsg(hook.getHookPort(), hook.getShutdownMsg());
	}
}


