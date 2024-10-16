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
    private boolean turn;
    
    public Game() {
        shipsPlayer1 = new ArrayList<>();
        shipsPlayer2 = new ArrayList<>();
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
                System.out.println("Se ha modificado '" + key + "' a: " + newValue);
            } else {
                System.out.println("La clave '" + key + "' no existe, inútil.");
            }
        } catch (IOException e) {
            System.out.println("Error! Error!: " + e.getMessage());
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
            System.out.println("Archivo creado: " + jsonFileName);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }


    public void gameActions() {
    }
}

