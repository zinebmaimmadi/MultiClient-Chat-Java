package com.zineb.chat.server.model;
import java.util.List;
public interface ServerEvents {
    void onLog(String message);
    void onUsersChanged(List<String> users);
    void onStateChanged(boolean online, String host, int port);
}
