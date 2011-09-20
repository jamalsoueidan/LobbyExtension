package com.soueidan.extensions.lobby.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.api.CreateRoomSettings.RoomExtensionSettings;
import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.entities.SFSRoomRemoveMode;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import com.soueidan.extensions.lobby.core.LobbyExtension;

public class RoomManager {
	public static String createRoom(User user) {
		int gameId = user.getVariable(LobbyExtension.GAME_ID).getIntValue();
		int userId = user.getVariable(LobbyExtension.USER_ID).getIntValue();
		
		String roomName = generateRandomWord();
		
		createServerRoom(roomName, gameId, user);
		createRoomDB(roomName, gameId, userId);
		
		return roomName;
	}
	
	private static void createServerRoom(String roomName, int gameId, User user) {
		// TODO ONLY ALLOW users in the params.getSFSArray("users") to access this 
		// createdRoom, so save the users id in the room table.
		
		LobbyExtension api = LobbyExtension.getInstance();
		api.trace("Create room name:", roomName);
		
		IDBManager dbManager = LobbyExtension.getInstance().getParentZone().getDBManager();
        
		String extensionName = "";
		String extensionClass = "";
		
        try {
        	Connection connection = dbManager.getConnection();
        	
        	PreparedStatement stmt = connection.prepareStatement("SELECT extension_name, extension_class FROM games where id=? limit 1");
    		stmt.setInt(1, gameId);
    		
        	ResultSet res = stmt.executeQuery();
        	res.next();
        	
        	extensionName = res.getString("extension_name");
        	extensionClass = res.getString("extension_class");
        	
        	connection.close();
        }
        catch (SQLException e)
        {
            LobbyExtension.getInstance().trace(ExtensionLogLevel.WARN, "SQL Failed: " + e.toString());
        }
        
		RoomExtensionSettings extension = new RoomExtensionSettings(extensionName, extensionClass);
		
		CreateRoomSettings setting = new CreateRoomSettings();
		setting.setGroupId("games");
		setting.setGame(true);
		setting.setMaxUsers(2);
		setting.setDynamic(true);
		setting.setAutoRemoveMode(SFSRoomRemoveMode.NEVER_REMOVE);
		setting.setName(roomName);
		setting.setHidden(false);
		setting.setExtension(extension);
		
		try {
			api.trace("Room created complete");
			
			api.getApi().createRoom(api.getParentZone(), setting, user, false, null);
			
		} catch ( SFSCreateRoomException err ) {
			api.trace(err.getMessage());
		}
	}
	
	private static void createRoomDB(String roomName, int gameId, int creatorId) {	
		IDBManager dbManager = LobbyExtension.getInstance().getParentZone().getDBManager();
		
        try {
        	Connection connection = dbManager.getConnection();
        	
        	Date dt = new Date();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	
        	PreparedStatement stmt = connection.prepareStatement("INSERT INTO rooms (name, creator_id, game_id, started_date) VALUES(?, ?, ?, ?)");
    		stmt.setString(1, roomName);
    		stmt.setInt(2, creatorId);
    		stmt.setInt(3, gameId);
    		stmt.setString(4, sdf.format(dt));
        	stmt.execute();
            
			connection.close();
        }
        catch (SQLException e)
        {
            LobbyExtension.getInstance().trace(ExtensionLogLevel.WARN, "SQL Failed: " + e.toString());
        }
	}

	private static String generateRandomWord()
	{
		int numberOfWords = 1;
	    String[] randomStrings = new String[numberOfWords];
	    Random random = new Random();
	    for(int i = 0; i < numberOfWords; i++)
	    {
	        char[] word = new char[random.nextInt(8)+3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
	        for(int j = 0; j < word.length; j++)
	        {
	            word[j] = (char)('a' + random.nextInt(26));
	        }
	        randomStrings[i] = new String(word);
	    }
	    return randomStrings[0];
	}
}
