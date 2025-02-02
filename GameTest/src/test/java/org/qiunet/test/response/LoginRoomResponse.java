package org.qiunet.test.response;

import org.qiunet.test.proto.LoginRoomProto;
import org.qiunet.test.response.annotation.Response;
import org.qiunet.test.robot.Robot;

/**
 * Created by qiunet.
 * 17/12/11
 */
@Response(ID = 1000003)
public class LoginRoomResponse extends ProtobufResponse<LoginRoomProto.LoginRoomResponse, Robot> {

	@Override
	public void response(Robot robot, LoginRoomProto.LoginRoomResponse loginRoomResponse) {
		robot.roomSize = loginRoomResponse.getRoomSize();
	}
}
