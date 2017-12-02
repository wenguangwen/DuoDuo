package org.qiunet.flash.handler.context.request.websocket;


/**
 * Created by qiunet.
 * 17/12/2
 */
public class FacadeWebSocketRequest<RequestData> implements IWebSocketRequest<RequestData> {
	private AbstractWebSocketRequestContext<RequestData, Object> context;
	public FacadeWebSocketRequest(AbstractWebSocketRequestContext<RequestData, Object> context) {
		this.context = context;
	}

	@Override
	public RequestData getRequestData() {
		return context.getRequestData();
	}

	@Override
	public String getRemoteAddress() {
		return context.getRemoteAddress();
	}

	@Override
	public Object getAttribute(String key) {
		return context.getAttribute(key);
	}

	@Override
	public void setAttribute(String key, Object val) {
		context.setAttribute(key, val);
	}

	@Override
	public void response(int protocolId, Object responseData) {
		context.response(protocolId, responseData);
	}
}