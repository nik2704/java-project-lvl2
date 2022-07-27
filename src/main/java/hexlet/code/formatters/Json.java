package hexlet.code.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.FIRST_MAP_KEY;
import static hexlet.code.utils.Utils.SECOND_MAP_KEY;

public class Json {
    public static String getResult(Map<String, Map<String, String>> fileData,
                                   Map<String, String> differences) {

        Map<String, String> map1 = fileData.get(FIRST_MAP_KEY);
        Map<String, String> map2 = fileData.get(SECOND_MAP_KEY);
        String result = "";

        Map<String, Map<String, String>> payload = new HashMap<>();

        if (differences.size() > 0) {
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {
                        String v1 = map1.get(mapEntry.getKey());
                        String v2 = map2.get(mapEntry.getKey());

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

    private static Map<String, String> getKeyMapObject(String status, String v) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put("status", status);
        result.put("value", getValueFormatted(v));

        return result;
    }

    private static Map<String, String> getKeyMapObject(String status, String v1, String v2) {
//        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        result.put("status", status);
        result.put("oldValue", getValueFormatted(v1));
        result.put("newValue", getValueFormatted(v2));

        return result;
//        return Map.of(
//                "status", status,
//                "oldValue", getValueFormatted(v1),
//                "newValue", getValueFormatted(v2));
    }
    private static String getValueFormatted(String value) {
        return value
                .replace("[\"", "[")
                .replace("\"]", "]")
                .replace("{\"", "{")
                .replace("\"}", "}")
                .replaceAll("\"", "")
                .replaceAll(",", ", ")
                .replaceAll(":", "=");
    }
}
