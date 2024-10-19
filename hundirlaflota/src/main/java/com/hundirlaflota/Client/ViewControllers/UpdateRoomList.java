package com.hundirlaflota.Client.ViewControllers;

import java.util.concurrent.atomic.AtomicBoolean;

import com.hundirlaflota.Client.Utils.UtilsWS;
import com.hundirlaflota.Common.ServerMessages.ListRoomsMessage;

public class UpdateRoomList implements Runnable {
    private UtilsWS ws;
    private AtomicBoolean running = new AtomicBoolean(true);
    public static final int UPDATE_INTERVAL = 200;

    public UpdateRoomList(UtilsWS ws) {
        this.ws = ws;
    }

    public void run() {
        while (running.get()) {
            ws.safeSend(new ListRoomsMessage().toString());
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