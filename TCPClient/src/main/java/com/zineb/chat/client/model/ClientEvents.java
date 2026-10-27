package com.zineb.chat.client.model;
public interface ClientEvents { void onConnected(boolean readOnly); void onMessage(String message); void onDisconnected(); void onError(String message); }
