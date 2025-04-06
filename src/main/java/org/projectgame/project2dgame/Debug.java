/*
  * Allgemeine Debug Klasse zum testen neuer Funktionen
 */



package org.projectgame.project2dgame;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import org.projectgame.project2dgame.Data.GameSettings;

import java.io.IOException;
import java.util.Scanner;


public class Debug {
    private final Scanner scanner = new Scanner(System.in);
    private volatile boolean running = true;

    public Debug() {
        startDebug();
    }

    public void startDebug() {
        new Thread(() -> {
            while (running) {
                System.out.println("Was willst du tun?" +
                        "\n1. Keybind ändern\n2. Map laden");

                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        System.out.println("Welchen Keybind willst du ändern?" +
                                "\n1. Hoch" +
                                "\n2. Runter" +
                                "\n3. Links" +
                                "\n4. Rechts");
                        int choice2 = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            changeKeybind(choice2);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case 2:
                        System.out.println("Welche Map laden? ");
                        int mapChoice = scanner.nextInt();
                        scanner.nextLine();
                        try {
                            Platform.runLater(() -> {
                                try {
                                    Main.setWindow("GameField", mapChoice);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        System.out.println("Ungültige Eingabe");
                        break;
                }
            }
        }).start();
    }


    public void changeKeybind(int choice) throws IOException {
        switch (choice) {
            case 1:
                System.out.println("Welchen Key willst du für Hoch setzen?");
                String key = "upKey";
                String input = scanner.nextLine().toUpperCase();
                KeyCode keyCode = getKeyCode(input);
                if (keyCode != null) {
                    GameSettings.changeKey(key, keyCode);
                } else {
                    System.out.println("Ungültiger KeyCode");
                }
                break;
            case 2:
                System.out.println("Welchen Key willst du für Runter setzen?");
                String key2 = "downKey";
                input = scanner.nextLine().toUpperCase();
                keyCode = getKeyCode(input);
                if (keyCode != null) {
                    GameSettings.changeKey(key2, keyCode);
                } else {
                    System.out.println("Ungültiger KeyCode");
                }
                break;
            case 3:
                System.out.println("Welchen Key willst du für Links setzen?");
                String key3 = "leftKey";
                input = scanner.nextLine().toUpperCase();
                keyCode = getKeyCode(input);
                if (keyCode != null) {
                    GameSettings.changeKey(key3, keyCode);
                } else {
                    System.out.println("Ungültiger KeyCode");
                }
                break;
            case 4:
                System.out.println("Welchen Key willst du für Rechts setzen?");
                String key4 = "rightKey";
                input = scanner.nextLine().toUpperCase();
                keyCode = getKeyCode(input);
                if (keyCode != null) {
                    GameSettings.changeKey(key4, keyCode);
                } else {
                    System.out.println("Ungültiger KeyCode");
                }
                break;
            default:
                System.out.println("Ungültige Eingabe");
                break;
        }
    }

    private KeyCode getKeyCode(String key) {
        try {
            return KeyCode.valueOf(key.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
