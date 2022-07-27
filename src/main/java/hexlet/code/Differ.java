package hexlet.code;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.FIRST_MAP_KEY;
import static hexlet.code.utils.Utils.SECOND_MAP_KEY;
import static hexlet.code.utils.Utils.OUTPUT_FORMAT_LIST;

public final class Differ {

    public static String generate(String filePath1, String filePath2) throws Exception {
        return generate(filePath1, filePath2, "stylish");
    }
    public static String generate(String filePath1, String filePath2, String outputFormat) throws Exception {
        checkParametres(filePath1, filePath2, outputFormat);

        outputFormat = outputFormat.toLowerCase();

        Map<String, Map<String, String>> fileData = Parser.parseFileData(filePath1, filePath2);
        Map<String, String> differences = getDifference(fileData);

        String result = Formatter.getFormattedData(fileData, differences, outputFormat);

        System.out.println(result);
        return result;
    }

    private static void checkParametres(String filePath1, String filePath2, String outputFormat) throws Exception {
        if (filePath1.isEmpty()) {
            throw new NullPointerException("The paramenter 'filePath1' must not be empty!");
        }

        if (filePath2.isEmpty()) {
            throw new NullPointerException("The paramenter 'filePath2' must not be empty!");
        }

        if (outputFormat.isEmpty()) {
            throw new NullPointerException("The FORMAT must not be not empty!");
        }

        if (!OUTPUT_FORMAT_LIST.contains(outputFormat.toLowerCase())) {
            throw new Exception(String.format("There is no such format '%s'", outputFormat));
        }
    }

    public static Map<String, String> getDifference(Map<String, Map<String, String>> fileData) {
        StringBuilder result = new StringBuilder();
        Map<String, String> map1 = fileData.get(FIRST_MAP_KEY);
        Map<String, String> map2 = fileData.get(SECOND_MAP_KEY);

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
