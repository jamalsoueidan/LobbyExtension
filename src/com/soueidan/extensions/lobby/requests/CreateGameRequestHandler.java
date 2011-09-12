package com.soueidan.extensions.lobby.requests;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSDataWrapper;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.soueidan.extensions.lobby.core.LobbyExtension;
import com.soueidan.extensions.lobby.managers.RoomManager;

public class CreateGameRequestHandler extends BaseClientRequestHandler {

public static final String CREATE_GAME = "create_game";
	
	private User userInviter;
	
	@Override
	public void handleClientRequest(User user, ISFSObject params) {
		trace(user.getName(), "create room");
		
		int gameId = user.getVariable(LobbyExtension.GAME_ID).getIntValue();
		userInviter = user;	
		
		List<User> users = getUsers(params);
		users.add(user);
		
		// TODO ONLY ALLOW users in the params.getSFSArray("users") to access this 
		// createdRoom, so save the users id in the room table.
		
		int userId = userInviter.getVariable(LobbyExtension.USER_ID).getIntValue();
		String roomName = RoomManager.createRoomDB(gameId, userId);
		
		ISFSObject data = new SFSObject();
		data.putUtfString("room", roomName);
		
		getParentExtension().send(CREATE_GAME, data, users);
	}
	
	private List<User> getUsers(ISFSObject params) {
		ISFSArray usersArray = params.getSFSArray("users");
		Iterator<SFSDataWrapper> itr = usersArray.iterator();
		List<User> users = new ArrayList<User>();
		Room room = userInviter.getLastJoinedRoom();
		
		User currentUser;
		Integer userId;
		while(itr.hasNext()) {
			userId = (Integer) itr.next().getObject();
			currentUser = room.getUserById(userId);
			users.add(currentUser);
		}
		
		return users;
	}

}
