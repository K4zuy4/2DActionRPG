package org.projectgame.project2dgame.Data;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameSettings {
    private Map<String, KeyCode> keyMap;
    private static final String DATEN_PFAD = getAppDataPath("/Data/data.json");
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GameSettings() throws IOException {
        this.keyMap = new HashMap<>();
        createAppDataDirectory();
        loadData();
        System.out.println(keyMap);
    }

    private static String getAppDataPath(String fileName) {
        String appData = System.getenv("APPDATA");
        return appData + File.separator + fileName;
    }

    public void savedata() throws IOException {
        Wrapper wrapper = new Wrapper(this.keyMap);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATEN_PFAD), wrapper);
    }

    public Map<String, KeyCode> getKeyMap() {
        return keyMap;
    }

    public void createAppDataDirectory() {
        File file = new File(getAppDataPath("Data"));
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void loadData() throws IOException {
        File file = new File(DATEN_PFAD);
        if (!file.exists()) {
            keyMap.put("upKey", KeyCode.W);
            keyMap.put("downKey", KeyCode.S);
            keyMap.put("leftKey", KeyCode.A);
            keyMap.put("rightKey", KeyCode.D);
            return;
        }

        Wrapper wrapper = objectMapper.readValue(file, Wrapper.class);

        this.keyMap.clear();
        this.keyMap = wrapper.getKeyMap();
        System.out.println(keyMap.get("upKey"));
    }
}
