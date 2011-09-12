package com.soueidan.extensions.lobby.core;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import tasks.QuickPlay;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.soueidan.extensions.lobby.requests.CreateGameRequestHandler;
import com.soueidan.extensions.lobby.requests.FindPlayerRequestHandler;
import com.soueidan.extensions.lobby.requests.InviteCancelRequestHandler;
import com.soueidan.extensions.lobby.requests.StatusRequestHandler;

public class LobbyExtension extends SFSExtension {
	
	public static final String USER_ID = "user_id";

	public static final String GAME_ID = "game_id";
	
	private static LobbyExtension _instance;
	
	public static LobbyExtension getInstance() {
		return _instance;
	}
	 // Keeps a reference to the task execution
    ScheduledFuture<?> taskHandle;
    
	@Override
	public void init() {

		_instance = this;
		
		trace("LobbyExtension init - updated");
		
		addRequestHandler(StatusRequestHandler.UPDATE_STATUS, StatusRequestHandler.class);
		addRequestHandler(InviteCancelRequestHandler.CANCEL_INVITE, InviteCancelRequestHandler.class);
		addRequestHandler(FindPlayerRequestHandler.FIND_PLAYER, FindPlayerRequestHandler.class);
		
		addRequestHandler(CreateGameRequestHandler.CREATE_GAME, CreateGameRequestHandler.class);
		
		//addRequestHandler(CreateRoomRequestHandler.CREATE_ROOM, CreateRoomRequestHandler.class);
		
		SmartFoxServer sfs = SmartFoxServer.getInstance();
        
		QuickPlay quickPlay = new QuickPlay(this);
        taskHandle = sfs.getTaskScheduler().scheduleAtFixedRate(quickPlay, 0, QuickPlay.SECOUNDS, TimeUnit.SECONDS);
	}
	
	@Override
	public void destroy()
	{
	    super.destroy();
	    trace("LobbyExtension destroy - updated");
	}

}
