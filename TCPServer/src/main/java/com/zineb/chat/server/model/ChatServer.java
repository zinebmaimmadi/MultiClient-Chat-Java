package com.zineb.chat.server.model;
import java.io.*; import java.net.*; import java.time.*; import java.time.format.DateTimeFormatter; import java.util.*; import java.util.concurrent.*; import java.util.concurrent.atomic.AtomicInteger;

public final class ChatServer {
    private static final DateTimeFormatter CLOCK = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final ServerConfig config; private final ServerEvents events;
    private final Set<ClientSession> clients = ConcurrentHashMap.newKeySet();
    private final AtomicInteger guestNumber = new AtomicInteger();
    private volatile boolean running; private ServerSocket serverSocket; private Thread acceptThread;

    public ChatServer(ServerConfig config, ServerEvents events){ this.config=config; this.events=events; }

    public synchronized void start() throws IOException {
        if(running) return;
        serverSocket = new ServerSocket(); serverSocket.bind(new InetSocketAddress(config.host(), config.port())); running=true;
        events.onStateChanged(true, config.host(), config.port()); log("Server Started"); log("Waiting for Client");
        acceptThread = Thread.ofVirtual().name("accept-loop").start(this::acceptLoop);
    }

    private void acceptLoop(){
        while(running){
            try {
                Socket socket = serverSocket.accept();
                Thread.startVirtualThread(() -> serve(socket));
            } catch(IOException e){ if(running) log("Accept error: " + e.getMessage()); }
        }
    }

    private void serve(Socket socket){
        ClientSession session=null;
        try(BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintWriter out=new PrintWriter(socket.getOutputStream(),true)){
            String hello=in.readLine(); if(hello==null) return;
            String requested = hello.startsWith("HELLO|") ? hello.substring(6).trim() : hello.trim();
            boolean readOnly=requested.isBlank(); String username=readOnly ? "Guest-"+guestNumber.incrementAndGet() : uniqueName(requested);
            session=new ClientSession(socket,out,username,readOnly); clients.add(session);
            session.send(system("Welcome ["+username+"]")); if(readOnly) session.send(system("READ-ONLY MODE: you may receive messages but cannot send chat messages."));
            log("Welcome ["+username+"]"); announce(username+" joined the chat."); publishUsers();
            String line;
            while(running && (line=in.readLine())!=null){
                String cmd=line.trim(); if(cmd.isEmpty()) continue;
                if(cmd.equalsIgnoreCase("bye") || cmd.equalsIgnoreCase("end") || cmd.equalsIgnoreCase("LEAVE")){ session.send(system("Goodbye.")); break; }
                if(cmd.equalsIgnoreCase("allUsers")){ sendUserList(session); continue; }
                if(session.readOnly()){ session.send(system("READ-ONLY MODE: sending is disabled.")); continue; }
                String msg="["+LocalTime.now().format(CLOCK)+"] "+session.username()+": "+line;
                broadcastToOthers(msg,session); log(msg);
            }
        } catch(IOException e){ if(running) log("Client I/O error: "+e.getMessage()); }
        finally { remove(session); log("Waiting for Client"); }
    }

    private String uniqueName(String base){ String name=base; int n=2; Set<String> used=new HashSet<>(); clients.forEach(c->used.add(c.username().toLowerCase())); while(used.contains(name.toLowerCase())) name=base+"-"+n++; return name; }
    private void sendUserList(ClientSession target){ List<String> names=clients.stream().map(ClientSession::username).sorted(String.CASE_INSENSITIVE_ORDER).toList(); target.send(system("Active users: "+String.join(", ",names))); }
    private void broadcastToOthers(String message, ClientSession sender){ for(ClientSession c:clients) if(c!=sender) c.send(message); }
    private void announce(String text){ String msg=system(text); for(ClientSession c:clients) c.send(msg); log(msg); }
    private String system(String text){ return "["+LocalTime.now().format(CLOCK)+"] SYSTEM: "+text; }
    private void remove(ClientSession session){ if(session==null) return; if(clients.remove(session)){ String name=session.username(); session.close(); announce(name+" left the chat."); publishUsers(); } }
    private void publishUsers(){ events.onUsersChanged(clients.stream().map(ClientSession::username).sorted(String.CASE_INSENSITIVE_ORDER).toList()); }
    private void log(String m){ events.onLog(m); }
    public synchronized void stop(){ if(!running)return; running=false; try{serverSocket.close();}catch(IOException ignored){} clients.forEach(ClientSession::close); clients.clear(); publishUsers(); events.onStateChanged(false,config.host(),config.port()); log("Server Stopped"); }
}
