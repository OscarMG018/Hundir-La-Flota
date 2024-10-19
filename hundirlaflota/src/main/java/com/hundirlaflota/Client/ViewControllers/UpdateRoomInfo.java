package com.hundirlaflota.Client.ViewControllers;

import java.util.concurrent.atomic.AtomicBoolean;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Client.Utils.UtilsWS;
import com.hundirlaflota.Common.ServerMessages.RoomInfoMessage;

public class UpdateRoomInfo implements Runnable {
    private AtomicBoolean running;
    public static final int UPDATE_INTERVAL = 200;
    private UtilsWS ws;

    public UpdateRoomInfo() {
        this.ws = UtilsWS.getSharedInstance(Main.location);
    }

    public void run() {
        running = new AtomicBoolean(true);
        while (running.get()) {
            ws.safeSend(new RoomInfoMessage().toString());
            try {
                Thread.sleep(UPDATE_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        running.set(false);
    }
}