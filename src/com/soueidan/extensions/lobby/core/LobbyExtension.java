package com.soueidan.extensions.lobby.core;

import com.smartfoxserver.v2.extensions.SFSExtension;
import com.soueidan.extensions.lobby.requests.InviteCancelRequestHandler;
import com.soueidan.extensions.lobby.requests.StatusRequestHandler;

public class LobbyExtension extends SFSExtension {

	@Override
	public void init() {

		trace("LobbyExtension init - updated");
		
		addRequestHandler(StatusRequestHandler.UPDATE_STATUS, StatusRequestHandler.class);
		addRequestHandler(InviteCancelRequestHandler.CANCEL_INVITE, InviteCancelRequestHandler.class);
	}
	
	@Override
	public void destroy()
	{
	    super.destroy();
	    trace("LobbyExtension destroy - updated");
	}

}
