package com.soueidan.extensions.lobby.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.smartfoxserver.v2.db.IDBManager;
import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import com.soueidan.extensions.lobby.core.LobbyExtension;

public class RoomManager {
	public static String createRoomDB(int gameId, int creatorId) {	
		IDBManager dbManager = LobbyExtension.getInstance().getParentZone().getDBManager();
        
		String room = generateRandomWord();
		
        try {
        	Connection connection = dbManager.getConnection();
        	
        	Date dt = new Date();
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	
        	PreparedStatement stmt = connection.prepareStatement("INSERT INTO rooms (name, creator_id, game_id, started_date) VALUES(?, ?, ?, ?)");
    		stmt.setString(1, room);
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
        
        return room;
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
