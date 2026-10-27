package com.zineb.chat.client.controller;
import com.zineb.chat.client.model.*; import com.zineb.chat.client.view.ClientView; import javafx.application.Platform; import java.io.IOException;
public final class ClientController implements ClientEvents {
 private final ClientView view; private final ChatClient client; private final String host; private final int port; private boolean readOnly;
 public ClientController(String h,int p,ClientView v){host=h;port=p;view=v;client=new ChatClient(this);view.setTarget(h,p);view.onConnect(this::connect);view.onSend(this::send);view.onLeave(this::leave);}
 private void connect(){try{client.connect(host,port,view.username().trim());}catch(IOException e){onError("Connection failed: "+e.getMessage());}}
 private void send(){String m=view.message().trim();if(m.isEmpty())return;if(readOnly){view.append("READ-ONLY MODE: sending is disabled.");return;}try{client.send(m);view.clearMessage();if(m.equalsIgnoreCase("bye")||m.equalsIgnoreCase("end"))client.disconnect();}catch(IOException e){onError(e.getMessage());}}
 private void leave(){client.disconnect();} public void shutdown(){client.disconnect();}
 public void onConnected(boolean ro){readOnly=ro;Platform.runLater(()->{view.setConnected(true,ro);view.append(ro?"Connected in READ-ONLY MODE.":"Connected. You can now chat.");});}
 public void onMessage(String m){Platform.runLater(()->view.append(m));} public void onDisconnected(){Platform.runLater(()->{view.setConnected(false,false);view.append("Disconnected.");});} public void onError(String m){Platform.runLater(()->view.append("ERROR: "+m));}
}
