package tasks;

import java.util.ArrayList;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.soueidan.extensions.lobby.core.LobbyExtension;
import com.soueidan.extensions.lobby.managers.RoomManager;
import com.soueidan.extensions.lobby.requests.FindPlayerRequestHandler;

public class QuickPlay implements Runnable {

	public static final int SECOUNDS = 3;

	private static final int PLAYERS = 2;
	
	private static LobbyExtension _lobby;
	private static ArrayList<Integer> _users;
	
	public QuickPlay(LobbyExtension lobby) {
		_lobby = lobby;
		_users = FindPlayerRequestHandler.users;
		
		_lobby.trace("Running quickPlay...");
	}

	@Override
	public void run() {		
		while(_users.size()>=PLAYERS) {
			User playerOne = getUser();
			User playerTwo = getUser();
			
			_lobby.trace(playerOne.getName(), "VS", playerTwo.getName());
			
			int gameId = playerOne.getVariable(LobbyExtension.GAME_ID).getIntValue();
			int userId = playerOne.getVariable(LobbyExtension.USER_ID).getIntValue();
			
			String room = RoomManager.createRoomDB(gameId, userId);
			
			ISFSObject params = new SFSObject();
			params.putUtfString("room", room);
			
			notifyUser(playerOne, playerTwo, params);
			notifyUser(playerTwo, playerOne, params);
		}
	}

	private void notifyUser(User player, User oppount, ISFSObject params) {
		_lobby.getApi().sendExtensionResponse(FindPlayerRequestHandler.FIND_PLAYER, params, player, player.getLastJoinedRoom(), false);
	}

	private User getUser() {
		return _lobby.getParentZone().getUserById(_users.remove(0));
	}

}
