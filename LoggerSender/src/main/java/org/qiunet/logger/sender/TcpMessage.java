package org.qiunet.logger.sender;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpMessage implements IMessage {
	private static ByteBuffer buffer = ByteBuffer.allocate(1024);

	private SocketChannel channel;
	private byte [] message;
	private short gameId;
	private String secret;
	TcpMessage(SocketChannel channel, short gameId, String secret, byte[] message)
	{
		this.channel = channel;
		this.message = message;
		this.secret = secret;
		this.gameId = gameId;
	}

	@Override
	public void send() {
		try {
			buffer.clear();
			MsgHeader.completeMessageHeader(buffer, gameId, secret, (short) message.length);
			buffer.put(message);
			buffer.flip();
			channel.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				channel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
