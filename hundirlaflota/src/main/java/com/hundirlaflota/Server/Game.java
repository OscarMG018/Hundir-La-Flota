package com.hundirlaflota.Server;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

public class Game {

    public enum ShootResult {
        HIT,
        MISS,
        SUNK,
        END,
        INVALID,
        ERROR
    }

    public static void modifyJson(String jsonFileName, String key, String newValue) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(jsonFileName);
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            if (rootNode.has(key)) {
                ((ObjectNode) rootNode).put(key, newValue);
                objectMapper.writeValue(jsonFile, rootNode);
            } else {
            }
        } catch (IOException e) {
        }
    }


    public static void createJson(String jsonFileName, String playerName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();

        for (char ch = 'A'; ch <= 'J'; ch++) {
            for (int num = 1; num <= 10; num++) {
                String key = ch + String.valueOf(num);
                rootNode.put(key, "");  
            }
        }

        rootNode.put("playerPosition", "");
        rootNode.put("playerName", playerName);
        try {
            objectMapper.writeValue(new File(jsonFileName), rootNode);
        } catch (IOException e) {
        }
    }

    public static ShootResult playShips(String jsonFileName, String coordinate) {
        List<String> shipNames = Arrays.asList("aircraftcarrier", "battleship", "cruiser", "submarine", "destroyer");
        ObjectMapper objectMapper = new ObjectMapper();
        String FileName = jsonFileName;
        try {
            File jsonFile = new File(FileName);
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            if (rootNode.has(coordinate)) {
                String value = rootNode.get(coordinate).asText();
                for (String ship : shipNames) {
                    if (value.contains(ship)) {
                        String damagedShip = "damaged " + ship;
                        ((ObjectNode) rootNode).put(coordinate, damagedShip);
                        objectMapper.writeValue(jsonFile, rootNode);
                        boolean allDamaged = true;
                        for (JsonNode node : rootNode) {
                            String nodeValue = node.asText();
                            if (nodeValue.equals(ship)) {
                                allDamaged = false;
                                break;
                            }
                        }
                        if (allDamaged) {
                            for (Iterator<String> it = rootNode.fieldNames(); it.hasNext(); ) {
                                String key = it.next();
                                if (rootNode.get(key).asText().equals(damagedShip)) {
                                    ((ObjectNode) rootNode).put(key, "destroyed " + ship);
                                }
                            }
                            objectMapper.writeValue(jsonFile, rootNode);
                            if (checkIfLost(jsonFileName)) {
                                return ShootResult.END;
                            } else {
                                return ShootResult.SUNK;
                            }
                        }
                        return ShootResult.HIT;
                    }
                }
                ((ObjectNode) rootNode).put(coordinate, "water");
                objectMapper.writeValue(jsonFile, rootNode);
                return ShootResult.MISS;
            } else {
                return ShootResult.INVALID;
            }
        } catch (IOException e) {
            return ShootResult.ERROR;
        }
    }

    public static boolean checkIfLost(String jsonFileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> shipNames = Arrays.asList("aircraftcarrier", "battleship", "cruiser", "submarine", "destroyer");
        try {
            File jsonFile = new File(jsonFileName);
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            for (JsonNode node : rootNode) {
                if (node.fieldNames().next().equals("playerPosition") || node.fieldNames().next().equals("playerName")) {
                    continue;
                }
                if (shipNames.contains(node.asText())) {
                    // there is a part of a ship that is not damaged
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean areShipsReady(String jsonFileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(jsonFileName);
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            for (JsonNode node : rootNode) {
                //if the field is PlayerPosition or playerName, continue
                if (node.fieldNames().next().equals("playerPosition") || node.fieldNames().next().equals("playerName")) {
                    continue;
                }
                if (!node.asText().isEmpty()) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean putShip(String jsonFileName, String coordinate, String ship, boolean isVertical) {
        int shipSize;
        switch (ship.toLowerCase()) {
            case "aircraftcarrier":
                shipSize = 5;
                break;
            case "battleship":
                shipSize = 4;
                break;
            case "cruiser":
                shipSize = 3;
                break;
            case "submarine":
                shipSize = 3;
                break;
            case "destroyer":
                shipSize = 2;
                break;
            default:
                throw new IllegalArgumentException("ship name not recognized: " + ship);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(jsonFileName);
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            char row = coordinate.charAt(0);
            int col = Integer.parseInt(coordinate.substring(1));

            if (isVertical) {
                if (row + shipSize - 1 > 'j') {
                    return false;
                }
                for (int i = 0; i < shipSize; i++) {
                    String coordToCheck = "" + (char) (row + i) + col;
                    if (!rootNode.get(coordToCheck).asText().isEmpty()) {
                        return false;
                    }
                }
                for (int i = 0; i < shipSize; i++) {
                    String coordToPlace = "" + (char) (row + i) + col;
                    ((ObjectNode) rootNode).put(coordToPlace, ship);
                }
            } else {
                if (col + shipSize - 1 > 10) {
                    return false;
                }
                for (int i = 0; i < shipSize; i++) {
                    String coordToCheck = "" + row + (col + i);
                    if (!rootNode.get(coordToCheck).asText().isEmpty()) {
                        return false;
                    }
                }
                for (int i = 0; i < shipSize; i++) {
                    String coordToPlace = "" + row + (col + i);
                    ((ObjectNode) rootNode).put(coordToPlace, ship);
                }
            }
            objectMapper.writeValue(jsonFile, rootNode);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}

