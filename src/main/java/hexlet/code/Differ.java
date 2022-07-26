package hexlet.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;


public final class Differ {
    private static final String ITEM_UNCHANGED = "unchanged";
    private static final String ITEM_CHANGED = "changed";
    private static final String ITEM_DELETED = "deleted";
    private static final String ITEM_ADDED = "added";
    private static final List<String> OUTPUT_FORMAT_LIST = List.of("stylish");

    public static String generate(String filePath1, String filePath2, String outputFormat) throws Exception {
        checkParametres(filePath1, filePath2, outputFormat);

        outputFormat = outputFormat.toLowerCase();

        Map<String, Map<String, String>> fileData = Parser.parseFileData(filePath1, filePath2);

        String result = getDifference(
                fileData.get(filePath1),
                fileData.get(filePath2),
                outputFormat
        );

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

    public static String getDifference(Map<String, String> map1, Map<String, String> map2, String outputFormat) {
        StringBuilder result = new StringBuilder();
        Map<String, String> differences = getKeysStatus(map1, map2);

        if (differences.size() > 0) {
            result.append("{\n");
            differences.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(mapEntry -> {
                        String v1 = map1.get(mapEntry.getKey());
                        String v2 = map2.get(mapEntry.getKey());

                        switch (mapEntry.getValue()) {
                            case (ITEM_UNCHANGED):
                                result.append(getFormattedString(mapEntry.getKey(), v1, ITEM_UNCHANGED, outputFormat));
                                break;
                            case (ITEM_CHANGED):
                                result.append(getFormattedString(mapEntry.getKey(), v1, ITEM_DELETED, outputFormat));
                                result.append(getFormattedString(mapEntry.getKey(), v2, ITEM_ADDED, outputFormat));
                                break;
                            case (ITEM_DELETED):
                                result.append(getFormattedString(mapEntry.getKey(), v1, ITEM_DELETED, outputFormat));
                                break;
                            case (ITEM_ADDED):
                                result.append(getFormattedString(mapEntry.getKey(), v2, ITEM_ADDED, outputFormat));
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

    private static String getFormattedString(String key, String value, String event, String outputFormat) {
        switch (outputFormat) {
            default:
                return stylish(key, value, event);
        }
    }
    private static String stylish(String key, String value, String event) {
        char mark = ' ';
        switch (event) {
            case (ITEM_ADDED):
                mark = '+';
                break;
            case (ITEM_DELETED):
                mark = '-';
                break;
            default:
        }

        value = value
            .replace("[\"", "[")
            .replace("\"]", "]")
            .replace("{\"", "{")
            .replace("\"}", "}")
            .replaceAll("\"", "")
            .replaceAll(",", ", ")
            .replaceAll(":", "=");

        return String.format("  %c %s: %s\n", mark, key, value);
    }

    private static Map<String, String> getKeysStatus(Map<String, String> map1, Map<String, String> map2) {
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
