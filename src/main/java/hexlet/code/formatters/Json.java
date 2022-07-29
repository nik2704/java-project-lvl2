package hexlet.code.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.FIRST_MAP_KEY;
import static hexlet.code.utils.Utils.SECOND_MAP_KEY;

public class Json {
    public static String getResult(Map<String, Map<String, Object>> fileData,
                                   Map<String, String> differences) {

        Map<String, Object> map1 = fileData.get(FIRST_MAP_KEY);
        Map<String, Object> map2 = fileData.get(SECOND_MAP_KEY);
        String result = "";

        Map<String, Map<String, Object>> payload = new HashMap<>();

        if (differences.size() > 0) {
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {
                        Object v1 = map1.get(mapEntry.getKey());
                        Object v2 = map2.get(mapEntry.getKey());

                        switch (mapEntry.getValue()) {
                            case (ITEM_UNCHANGED):
                                payload.put(mapEntry.getKey(), getKeyMapObject("unchanged", v1));
                                break;
                            case (ITEM_CHANGED):
                                payload.put(mapEntry.getKey(), getKeyMapObject("changed", v1, v2));
                                break;
                            case (ITEM_DELETED):
                                payload.put(mapEntry.getKey(), getKeyMapObject("deleted", v1));
                                break;
                            case (ITEM_ADDED):
                                payload.put(mapEntry.getKey(), getKeyMapObject("added", v2));
                                break;
                            default:
                        }
                    });
        }

        try {
            result = new ObjectMapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(payload);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static Map<String, Object> getKeyMapObject(String status, Object v) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("status", status);
        result.put("value", Objects.toString(v));

        return result;
    }

    private static Map<String, Object> getKeyMapObject(String status, Object v1, Object v2) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("status", status);
        result.put("value", Objects.toString(v2));
        result.put("oldValue", Objects.toString(v1));

        return result;
    }
}
