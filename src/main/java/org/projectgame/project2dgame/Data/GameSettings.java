package org.projectgame.project2dgame.Data;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.input.KeyCode;
import org.projectgame.project2dgame.Controller.SoundEngine;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSettings {

    private static final Map<String, KeyCode> keyMap = new HashMap<>();
    private static final List<TimeWrapper> times = new ArrayList<>();
    private static final List<WaveWrapper> waves = new ArrayList<>();
    private static final String DATEN_PFAD = getAppDataPath("/Sanctum_of_Sorrow/data.json");
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static double volume = 0.5;

    static {
        createAppDataDirectory();
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getAppDataPath(String fileName) {
        String appData = System.getenv("APPDATA");
        return appData + File.separator + fileName;
    }

    public static void savedata() throws IOException {
        Wrapper wrapper = new Wrapper(keyMap, volume, times, waves);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATEN_PFAD), wrapper);
    }

    public static Map<String, KeyCode> getKeyMap() {
        return keyMap;
    }

    public static double getVolume() {
        return volume;
    }

    public static void setVolume(double newVolume) throws IOException {
        volume = Math.max(0.0, Math.min(1.0, newVolume));
        SoundEngine.changeVolume(volume);
        savedata();
    }

    private static void createAppDataDirectory() {
        File file = new File(getAppDataPath("Sanctum_of_Sorrow"));
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void loadData() throws IOException {
        File file = new File(DATEN_PFAD);
        if (!file.exists()) {
            keyMap.put("upKey", KeyCode.W);
            keyMap.put("downKey", KeyCode.S);
            keyMap.put("leftKey", KeyCode.A);
            keyMap.put("rightKey", KeyCode.D);
            keyMap.put("lookUpKey", KeyCode.UP);
            keyMap.put("lookDownKey", KeyCode.DOWN);
            keyMap.put("lookLeftKey", KeyCode.LEFT);
            keyMap.put("lookRightKey", KeyCode.RIGHT);
            return;
        }

        Wrapper wrapper = objectMapper.readValue(file, Wrapper.class);
        keyMap.clear();
        keyMap.putAll(wrapper.getKeyMap());
        volume = wrapper.getVolume();
        times.clear();
        times.addAll(wrapper.getTimes());
        waves.clear();
        waves.addAll(wrapper.getWaves());
    }

    public static void changeKey(String key, KeyCode keyCode) throws IOException {
        keyMap.replace(key, keyCode);
        savedata();
    }

    public static void saveTime(int level, double time) throws IOException {
        times.add(new TimeWrapper(level, time, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm - dd.MM.yyyy"))));
        savedata();
    }

    public static List<TimeWrapper> getAllTimes() {
        return times;
    }

    public static void saveWave(int wave, double time) throws IOException {
        waves.add(new WaveWrapper(wave, time, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm - dd.MM.yyyy"))));
        savedata();
    }

    public static List<WaveWrapper> getAllWaves() {
        return waves;
    }

}
