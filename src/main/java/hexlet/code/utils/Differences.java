package hexlet.code.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.ITEM_ADDED;

public class Differences {
    public static Map<String, Map<String, Object>> getDifference(Map<String, Object> map1, Map<String, Object> map2) {
        Map<String, Map<String, Object>> differences = new HashMap<>();
        Set<String> set = new HashSet<>();

        if (!Objects.isNull(map1)) {
            set.addAll(map1.keySet());
        }

        if (!Objects.isNull(map2)) {
            set.addAll(map2.keySet());
        }

        set.stream()
                .sorted()
                .forEach(keyLine -> {
                    boolean keyInV1 = Objects.isNull(map1) ? false : map1.containsKey(keyLine);
                    boolean keyInV2 = Objects.isNull(map2) ? false : map2.containsKey(keyLine);
                    Map<String, Object> currentMap = new HashMap<>();

                    String value = ITEM_UNCHANGED;
                    if (keyInV1 && keyInV2) {
                        if (!Objects.equals(map2.get(keyLine), map1.get(keyLine))) {
                            value = ITEM_CHANGED;
                            currentMap.put("value", map2.get(keyLine));
                            currentMap.put("oldValue", map1.get(keyLine));
                        } else {
                            currentMap.put("value", map1.get(keyLine));
                        }
                    } else if (keyInV1 && !keyInV2) {
                        value = ITEM_DELETED;
                        currentMap.put("oldValue", map1.get(keyLine));
                    } else if (!keyInV1 && keyInV2) {
                        value = ITEM_ADDED;
                        currentMap.put("value", map2.get(keyLine));
                    }

                    currentMap.put("comparisonResult", value);
                    differences.put(keyLine, currentMap);
                });


        return differences;
    }
}
