package org.qiunet.test.server.handler.room;

import org.qiunet.flash.handler.common.annotation.RequestHandler;
import org.qiunet.flash.handler.context.request.tcp.ITcpRequest;
import org.qiunet.flash.handler.handler.tcp.TcpProtobufHandler;
import org.qiunet.test.proto.LoginRoomProto;

/**
 * Created by qiunet.
 * 17/12/9
 */
@RequestHandler(ID = 1003, desc = "房间服的登录")
public class LoginRoomHandler extends TcpProtobufHandler<LoginRoomProto.LoginRoomRequest> {
	@Override
	public void handler(ITcpRequest<LoginRoomProto.LoginRoomRequest> context)throws Exception {
		logger.info("LoginRoomHandler received message "+context.getRequestData());
		context.response(1000003, LoginRoomProto.LoginRoomResponse.newBuilder().setRoomSize(context.getRequestData().getHeader().getUid()).build());
	}
}
