package com.zineb.chat.server.model;
import java.io.*; import java.net.Socket;
final class ClientSession implements AutoCloseable {
    private final Socket socket; private final PrintWriter out; private final String username; private final boolean readOnly;
    ClientSession(Socket socket, PrintWriter out, String username, boolean readOnly) { this.socket=socket; this.out=out; this.username=username; this.readOnly=readOnly; }
    String username(){ return username; } boolean readOnly(){ return readOnly; }
    synchronized void send(String message){ out.println(message); }
    public void close(){ try { socket.close(); } catch(IOException ignored){} }
}
