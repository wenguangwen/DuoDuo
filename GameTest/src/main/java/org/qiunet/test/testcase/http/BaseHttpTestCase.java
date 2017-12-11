package org.qiunet.test.testcase.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.qiunet.flash.handler.context.header.MessageContent;
import org.qiunet.flash.handler.context.header.ProtocolHeader;
import org.qiunet.flash.handler.netty.client.http.NettyHttpClient;
import org.qiunet.test.robot.IRobot;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.encryptAndDecrypt.CrcUtil;

import java.net.URI;

/**
 * Created by qiunet.
 * 17/12/4
 */
public abstract class BaseHttpTestCase<RequestData, ResponseData, Robot extends IRobot> implements ITestCase<Robot> {
	/***
	 * 得到请求的uri
	 * @return
	 */
	public abstract URI getServerUri();
	@Override
	public void sendRequest(Robot robot) {
		MessageContent content = buildRequest(robot);
		ByteBuf byteBuf = Unpooled.buffer();

		ProtocolHeader header = new ProtocolHeader(content.bytes().length, content.getProtocolId(), (int) CrcUtil.getCrc32Value(content.bytes()));
		header.writeToByteBuf(byteBuf);
		byteBuf.writeBytes(content.bytes());
		FullHttpResponse httpResponse = NettyHttpClient.sendRequest(byteBuf , getServerUri().toString());
		if (httpResponse == null) {
			robot.brokeRobot("http response is null .server maybe was shutdown!");
			return;
		}

		if (httpResponse.status() != HttpResponseStatus.OK) {
			robot.brokeRobot("http status not 200");
			return;
		}
		new ProtocolHeader(httpResponse.content());
		byte [] bytes = new byte[httpResponse.content().readableBytes()];
		httpResponse.content().readBytes(bytes);
		content = new MessageContent(0, bytes);
		responseData(robot, content);
	}

	@Override
	public boolean cancelIfConditionMiss() {
		return false;
	}
	/**
	 *
	 * @param bytes
	 * @return
	 */
	protected abstract void responseData(Robot robot, MessageContent bytes);

	/**
	 * 下层的requestBuild
	 * @param robot
	 * @return
	 */
	protected abstract RequestData requestBuild(Robot robot);

	/**
	 * 下层的响应
	 * @param robot
	 * @param responseData
	 */
	protected abstract void responseData(Robot robot, ResponseData responseData);
}
