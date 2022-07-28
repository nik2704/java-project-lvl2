package hexlet.code.formatters;

import java.util.Map;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.FIRST_MAP_KEY;
import static hexlet.code.utils.Utils.SECOND_MAP_KEY;

public class Plain {
    public static String getResult(Map<String, Map<String, String>> fileData,
                                   Map<String, String> differences) {

        StringBuilder result = new StringBuilder();
        Map<String, String> map1 = fileData.get(FIRST_MAP_KEY);
        Map<String, String> map2 = fileData.get(SECOND_MAP_KEY);

        if (differences.size() > 0) {
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {
                        String v1 = map1.get(mapEntry.getKey());
                        String v2 = map2.get(mapEntry.getKey());

                        if (result.length() > 0 && mapEntry.getValue() != ITEM_UNCHANGED) {
                            result.append("\n");
                        }

                        switch (mapEntry.getValue()) {
                            case (ITEM_CHANGED):
                                result.append(String.format("Property '%s' was updated. From %s to %s",
                                        mapEntry.getKey(), getValueFormatted(v1), getValueFormatted(v2)));
                                break;
                            case (ITEM_DELETED):
                                result.append(String.format("Property '%s' was removed", mapEntry.getKey()));
                                break;
                            case (ITEM_ADDED):
                                result.append(String.format("Property '%s' was added with value: %s",
                                        mapEntry.getKey(), getValueFormatted(v2)));
                                break;
                            default:
                        }
                    });
        } else {
            result.append("");
        }

        return result.toString();
    }

    private static String getValueFormatted(String value) {
        if (value.matches("\\d+") || value.matches("true|false") || value.matches("null")) {
            return value;
        }

        value = value
                .replaceAll("^\\{.*\\}$", "[complex value]")
                .replaceAll("^\\[.*\\]$", "[complex value]");

        if (!value.equals("[complex value]")) {
            value = "'" + value + "'";
        }

        return value;
    }
}
