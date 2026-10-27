package com.zineb.chat.client.model;
import java.io.*; import java.net.Socket; import java.util.concurrent.atomic.AtomicBoolean;
public final class ChatClient {
 private final ClientEvents events; private Socket socket; private BufferedReader in; private PrintWriter out; private volatile boolean connected; private final AtomicBoolean notified=new AtomicBoolean(true);
 public ChatClient(ClientEvents e){events=e;}
 public synchronized void connect(String host,int port,String username)throws IOException{if(connected)throw new IOException("Already connected");socket=new Socket(host,port);in=new BufferedReader(new InputStreamReader(socket.getInputStream()));out=new PrintWriter(socket.getOutputStream(),true);connected=true;notified.set(false);out.println("HELLO|"+username);events.onConnected(username.isBlank());Thread.ofVirtual().name("client-reader").start(this::readLoop);}
 private void readLoop(){try{String line;while(connected&&(line=in.readLine())!=null)events.onMessage(line);}catch(IOException e){if(connected)events.onError(e.getMessage());}finally{close();notifyOnce();}}
 public synchronized void send(String text)throws IOException{if(!connected||out==null)throw new IOException("Not connected");out.println(text);}
 public synchronized void disconnect(){if(!connected)return;try{out.println("LEAVE");}catch(Exception ignored){}close();notifyOnce();}
 private synchronized void close(){connected=false;try{if(socket!=null)socket.close();}catch(IOException ignored){}socket=null;in=null;out=null;}
 private void notifyOnce(){if(notified.compareAndSet(false,true))events.onDisconnected();}
}
