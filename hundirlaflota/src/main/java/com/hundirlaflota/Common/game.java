package com.hundirlaflota.Common;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

public class Game {
    private ArrayList<String> shipsPlayer1;
    private ArrayList<String> shipsPlayer2;
    private boolean player1Turn;
    private boolean player2Turn;
    
    
    public Game() {
        
        shipsPlayer1 = new ArrayList<>();
        shipsPlayer2 = new ArrayList<>();
        player1Turn = true;
        player2Turn = false;
    }

    public ArrayList<String> getShipsPlayer1() {
        return shipsPlayer1;
    }

    public void setShipsPlayer1(ArrayList<String> shipsPlayer1) {
        this.shipsPlayer1 = shipsPlayer1;
    }

    public ArrayList<String> getShipsPlayer2() {
        return shipsPlayer2;
    }

    public void setShipsPlayer2(ArrayList<String> shipsPlayer2) {
        this.shipsPlayer2 = shipsPlayer2;
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

        for (char ch = 'a'; ch <= 'j'; ch++) {
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


    public boolean playShips(String player, String coordinate) {
        List<String> shipNames = Arrays.asList("aircraft carrier", "battleship", "cruiser", "submarine", "destroyer");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFileName = player.equals("player1") ? "player2.json" : "player1.json";
        try {
            File jsonFile = new File(jsonFileName);
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
                        }
                        return true;
                    }
                }
                ((ObjectNode) rootNode).put(coordinate, "water");
                objectMapper.writeValue(jsonFile, rootNode);
                if (player.equals("player1")) {
                    player1turn = false;
                    player2turn = true;
                } else {
                    player1turn = true;
                    player2turn = false;
                }
                return false;
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    
    public void gameActions() {
    }

    public static boolean putShip(String player, String coordinate, String ship, boolean isVertical) {
        String jsonFileName = player.equals("player1") ? "player2.json" : "player1.json";
        int shipSize;
        switch (ship.toLowerCase()) {
            case "aircraft carrier":
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
                return false;
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

