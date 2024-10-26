package com.hundirlaflota.Common.ServerMessages;

import org.json.JSONObject;

public class ShipData {
    private String shipName;
    private boolean isVertical;
    private int x;
    private int y;

    public ShipData(String shipName, boolean isVertical, int x, int y) {
        this.shipName = shipName;
        this.isVertical = isVertical;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "coordinate:\"" + getCoordinate() + "\"" +
                ", shipName:\"" + shipName + "\"" +
                ", isVertical:\"" + isVertical + "\"" +
                ", x:" + x +
                ", y:" + y +
                "}";
    }

    public static ShipData fromJson(JSONObject json) {
        return new ShipData(json.getString("shipName"), json.getBoolean("isVertical"), json.getInt("x"), json.getInt("y"));
    }

    public String getCoordinate() {
        return (char) (y + 'A') + "" + (x + 1);
    }
}
