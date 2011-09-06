package com.soueidan.extensions.lobby.requests;

import java.util.Arrays;
import java.util.List;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class StatusRequestHandler extends BaseClientRequestHandler {

	public static final String UPDATE_STATUS = "update_status";

	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		// TODO Auto-generated method stub
		trace("Execute statusRequestHandler");
		int status = params.getInt("status");
		trace(user.getName(), "set status to:", status);
		
		UserVariable variable = new SFSUserVariable("status", status);
		List<UserVariable> list = Arrays.asList(variable);
		getApi().setUserVariables(user, list);
	}

}
