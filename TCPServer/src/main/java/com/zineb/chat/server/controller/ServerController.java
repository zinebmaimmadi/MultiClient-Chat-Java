package com.zineb.chat.server.controller;
import com.zineb.chat.server.model.*; import com.zineb.chat.server.view.ServerView; import javafx.application.Platform; import java.io.IOException; import java.util.List;
public final class ServerController implements ServerEvents {
    private final ChatServer server; private final ServerView view;
    public ServerController(ServerView view){ this.view=view; this.server=new ChatServer(ServerConfig.load(),this); }
    public void start(){ try{server.start();}catch(IOException e){onLog("ERROR: "+e.getMessage());} }
    public void stop(){server.stop();}
    public void onLog(String m){Platform.runLater(()->view.addLog(m));}
    public void onUsersChanged(List<String> u){Platform.runLater(()->view.setUsers(u));}
    public void onStateChanged(boolean o,String h,int p){Platform.runLater(()->view.setStatus(o,h,p));}
}
