package com.hundirlaflota.Client.Utils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class UtilsWS  extends WebSocketClient {

    public static UtilsWS sharedInstance = null;
    private Consumer<String> onMessageCallBack = null;
    private String location = "";
    private static AtomicBoolean exitRequested = new AtomicBoolean(false); // Thread safe
    private static AtomicBoolean isConnected = new AtomicBoolean(false);

    private UtilsWS (String location, Draft draft) throws URISyntaxException {
        super (new URI(location), draft);
        this.location = location;
    }

    static public UtilsWS getSharedInstance (String location) {
        if (location.isEmpty()) {
            return null;
        }
        if (sharedInstance == null) {
            try {
                sharedInstance = new UtilsWS(location, (Draft) new Draft_6455());
                sharedInstance.connect();
                System.out.println("aqui"+sharedInstance.getRemoteSocketAddress());
                isConnected.set(true);
            } catch (URISyntaxException e) { 
                e.printStackTrace(); 
                System.out.println("WS Error, " + location + " is not a valid URI");
            }
        }

        return sharedInstance;
    }

    public void setOnMessage (Consumer<String> callBack) {
        this.onMessageCallBack = callBack;
    }

    @Override
    public void onMessage(String message) {
        //System.out.println("Received message: " + message);
        if (onMessageCallBack != null) {
            onMessageCallBack.accept(message);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("WS connected to: " + getURI());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WS closed connection from: " + getURI());

        if (remote) {
            reconnect();
        }
    }

    @Override
    public void onError(Exception e) {
        System.out.println("WS connection error: " + e.getMessage());
        if (e.getMessage().contains("Connection refused") || e.getMessage().contains("Connection reset")) {
            reconnect();
        }
    }

    public void safeSend(String text) {
        System.out.println("aqui" + sharedInstance.getRemoteSocketAddress());
        try {
            System.out.println("aqui: " + sharedInstance.getRemoteSocketAddress());
            sharedInstance.send(text);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("WS Error sending message");
        }
    }

    public void reconnect () {
        if (exitRequested.get()) { return; }
    
        System.out.println("WS reconnecting to: " + this.location);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            System.out.println("WD Error, waiting");
            Thread.currentThread().interrupt();  // Assegurar que el fil es torna a interrompre correctament
        }
    
        if (exitRequested.get()) { return; }
        
        Consumer<String> oldCallBack = this.onMessageCallBack;
        String oldLocation = this.location;
        sharedInstance.close();
        sharedInstance = null;
        getSharedInstance(oldLocation);
        sharedInstance.setOnMessage(oldCallBack);
    }
    
    public void forceExit () {
        System.out.println("WS Closing ...");
        isConnected.set(false);
        exitRequested.set(true);
        try {
            if (!isClosed()) {
                super.closeBlocking();
            }
        } catch (Exception e) {
            System.out.println("WS Interrupted while closing WebSocket connection");
            Thread.currentThread().interrupt();
        }
    }

    public boolean isConnected() {
        return UtilsWS.isConnected.get();
    }
}