package hexlet.code.formatters;

import java.util.Map;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;

public class Stylish {
    public static String getResult(Map<String, Map<String, Object>> differences) {

        StringBuilder result = new StringBuilder();

        if (differences.size() > 0) {
            result.append("{\n");
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {

                        Map<String, Object> currentMap = mapEntry.getValue();

                        switch (currentMap.get("comparisonResult").toString()) {
                            case (ITEM_UNCHANGED):
                                result.append(String.format("  %c %s: %s\n",
                                        ' ', mapEntry.getKey(), currentMap.get("value")));
                                break;
                            case (ITEM_CHANGED):
                                result.append(String.format("  %c %s: %s\n",
                                        '-', mapEntry.getKey(), currentMap.get("oldValue")));
                                result.append(String.format("  %c %s: %s\n",
                                        '+', mapEntry.getKey(), currentMap.get("value")));
                                break;
                            case (ITEM_DELETED):
                                result.append(String.format("  %c %s: %s\n",
                                        '-', mapEntry.getKey(), currentMap.get("oldValue")));
                                break;
                            case (ITEM_ADDED):
                                result.append(String.format("  %c %s: %s\n",
                                        '+', mapEntry.getKey(), currentMap.get("value")));
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
}
