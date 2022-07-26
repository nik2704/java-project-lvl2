package hexlet.code.formatters;

import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;


public class Plain {
    public static String getResult(Map<String, Map<String, Object>> differences) {

        StringBuilder result = new StringBuilder();

        if (differences.size() > 0) {
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {
                        Map<String, Object> currentMap = mapEntry.getValue();
                        String comparisonResult = currentMap.get("comparisonResult").toString();

                        if (result.length() > 0 && !comparisonResult.equals(ITEM_UNCHANGED)) {
                            result.append("\n");
                        }

                        switch (comparisonResult) {
                            case (ITEM_CHANGED):
                                result.append(String.format("Property '%s' was updated. From %s to %s",
                                        mapEntry.getKey(),
                                        getValueFormatted(currentMap.get("oldValue")),
                                        getValueFormatted(currentMap.get("value"))));
                                break;
                            case (ITEM_DELETED):
                                result.append(String.format("Property '%s' was removed", mapEntry.getKey()));
                                break;
                            case (ITEM_ADDED):
                                result.append(String.format("Property '%s' was added with value: %s",
                                        mapEntry.getKey(), getValueFormatted(currentMap.get("value"))));
                                break;
                            default:
                        }
                    });
        } else {
            result.append("");
        }

        return result.toString();
    }

    private static String getValueFormatted(Object value) {

        if ((value instanceof ArrayList) || (value instanceof LinkedHashMap)) {
            return "[complex value]";
        } else if (value instanceof String) {
            return "'" + Objects.toString(value) + "'";
        }

        return Objects.toString(value);
    }
}
