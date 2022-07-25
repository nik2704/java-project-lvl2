package hexlet.code;

//import java.util.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

public class Parser {
    private static final String ITEM_UNCHANGED = "unchanged";
    private static final String ITEM_CHANGED = "changed";
    private static final String ITEM_DELETED = "deleted";
    private static final String ITEM_ADDED = "added";

    public static String getDifference(Map<String, Object> map1, Map<String, Object> map2) {
        StringBuilder result = new StringBuilder();
        Map<String, String> differences = getKeysStatus(map1, map2);

        if (differences.size() > 0) {
            result.append("{\n");
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {
                        System.out.println(mapEntry.getKey());
                        switch (mapEntry.getValue()) {
                            case (ITEM_UNCHANGED):
                                result.append(String.format("    %s: %s\n", mapEntry.getKey(),
                                        map1.get(mapEntry.getKey())));
                                break;
                            case (ITEM_CHANGED):
                                result.append(String.format("  - %s: %s\n", mapEntry.getKey(),
                                        map1.get(mapEntry.getKey())));
                                result.append(String.format("  + %s: %s\n", mapEntry.getKey(),
                                        map2.get(mapEntry.getKey())));
                                break;
                            case (ITEM_DELETED):
                                result.append(String.format("  - %s: %s\n", mapEntry.getKey(),
                                        map1.get(mapEntry.getKey())));
                                break;
                            case (ITEM_ADDED):
                                result.append(String.format("  + %s: %s\n", mapEntry.getKey(),
                                        map2.get(mapEntry.getKey())));
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

    private static Map<String, String> getKeysStatus(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, String> differences = new HashMap<>();

        Set<String> set = new HashSet<>();
        set.addAll(map1.keySet());
        set.addAll(map2.keySet());

        set.stream()
                .sorted()
                .forEach(keyLine -> {
                    boolean keyInV1 = map1.containsKey(keyLine);
                    boolean keyInV2 = map2.containsKey(keyLine);
                    String value = ITEM_UNCHANGED;

                    if (keyInV1 && keyInV2) {
                        if (!Objects.equals(map2.get(keyLine), map1.get(keyLine))) {
                            value = ITEM_CHANGED;
                        }
                    } else if (keyInV1 && !keyInV2) {
                        value = ITEM_DELETED;
                    } else if (!keyInV1 && keyInV2) {
                        value = ITEM_ADDED;
                    }

                    differences.put(keyLine, value);
                });

        return differences;
    }
}
