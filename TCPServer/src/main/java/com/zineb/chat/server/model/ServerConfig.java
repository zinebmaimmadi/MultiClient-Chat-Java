package com.zineb.chat.server.model;
import java.io.*; import java.util.Properties;
public record ServerConfig(String host, int port) {
    public static ServerConfig load() {
        Properties p = new Properties();
        try (InputStream in = ServerConfig.class.getResourceAsStream("/server.properties")) { if (in != null) p.load(in); }
        catch (IOException ignored) {}
        String host = p.getProperty("server.host", "0.0.0.0").trim();
        int port = 3000;
        try { port = Integer.parseInt(p.getProperty("server.port", "3000").trim()); } catch (NumberFormatException ignored) {}
        return new ServerConfig(host.isBlank() ? "0.0.0.0" : host, port);
    }
}
