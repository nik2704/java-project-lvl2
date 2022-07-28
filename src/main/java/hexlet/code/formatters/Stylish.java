package hexlet.code.formatters;

import java.util.Map;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.FIRST_MAP_KEY;
import static hexlet.code.utils.Utils.SECOND_MAP_KEY;

public class Stylish {
    public static String getResult(Map<String, Map<String, String>> fileData,
                                   Map<String, String> differences) {

        StringBuilder result = new StringBuilder();
        Map<String, String> map1 = fileData.get(FIRST_MAP_KEY);
        Map<String, String> map2 = fileData.get(SECOND_MAP_KEY);

        if (differences.size() > 0) {
            result.append("{\n");
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {
                        String v1 = map1.get(mapEntry.getKey());
                        String v2 = map2.get(mapEntry.getKey());

                        switch (mapEntry.getValue()) {
                            case (ITEM_UNCHANGED):
                                result.append(getStringFormatted(mapEntry.getKey(), v1, ' '));
                                break;
                            case (ITEM_CHANGED):
                                result.append(getStringFormatted(mapEntry.getKey(), v1, '-'));
                                result.append(getStringFormatted(mapEntry.getKey(), v2, '+'));
                                break;
                            case (ITEM_DELETED):
                                result.append(getStringFormatted(mapEntry.getKey(), v1, '-'));
                                break;
                            case (ITEM_ADDED):
                                result.append(getStringFormatted(mapEntry.getKey(), v2, '+'));
                                break;
                            default:
                        }
                    });
            result.append("}");
        } else {
            result.append("{}");
        }

        return result.toString();
    }

    private static String getStringFormatted(String key, String value, char mark) {
//        value = value
//                .replace("[\"", "[")
//                .replace("\"]", "]")
//                .replace("{\"", "{")
//                .replace("\"}", "}")
//                .replaceAll("\"", "")
////                .replaceAll(",", ", ")
//                .replaceAll(":", "=");

        return String.format("  %c %s: %s\n", mark, key, value);
    }
}
