package com.soueidan.extensions.lobby.requests;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class InviteCancelRequestHandler extends BaseClientRequestHandler {

	public static final String CANCEL_INVITE = "cancel_invite";

	@Override
	public void handleClientRequest(User user, ISFSObject params) {

		trace("Execute InviteCancelRequestHandler");
		
		User invitee = getParentExtension().getParentRoom().getUserById(params.getInt("invitee_id"));
		
		getParentExtension().send(CANCEL_INVITE, null, invitee);

	}

}
