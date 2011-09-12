package com.soueidan.extensions.lobby.requests;

import java.util.ArrayList;

import com.smartfoxserver.v2.annotations.Instantiation;
import com.smartfoxserver.v2.annotations.Instantiation.InstantiationMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

@Instantiation(InstantiationMode.SINGLE_INSTANCE)
public class FindPlayerRequestHandler extends BaseClientRequestHandler {

	public static final String FIND_PLAYER = "find_player";
	
	public static ArrayList<Integer> users = new ArrayList<Integer>();
	
	@Override
	public void handleClientRequest(User sender, ISFSObject params) {
		Boolean add = params.getBool("add");
		trace("add or remove", add);
		int userId = sender.getId();
		if ( add ) {
			users.add(userId);
		} else {
			int removeId = users.indexOf(userId);
			users.remove(removeId);
		}
	}
}
