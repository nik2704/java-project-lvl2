package hexlet.code;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class Differ {
    private static final String ITEM_UNCHANGED = "unchanged";
    private static final String ITEM_CHANGED = "changed";
    private static final String ITEM_DELETED = "deleted";
    private static final String ITEM_ADDED = "added";
    private static final List<String> FORMAT_LIST = List.of("stylish");
    private static Map<String, Object> getFileData(String fileName, ObjectMapper mapper) throws Exception {
        File file = new File(fileName);

        if (!file.isFile()) {
            throw new IOException(String.format("There is no file with name '%s'", fileName));
        }

        if (!file.canRead()) {
            throw new IOException(String.format("Unable to read the file '%s'", fileName));
        }

        try {
            mapper.readTree(file);
        } catch (IOException err) {
            throw new IOException(String.format("Incorrect JSON format of the file '%s'", fileName));
        }

        return mapper.readValue(file, new TypeReference<Map<String, Object>>() { });
    }

    private static Map<String, String> genDiff(Map<String, Object> jsonMap1, Map<String, Object> jsonMap2) {
        Map<String, String> result = new HashMap<>();

        Set<String> set = new HashSet<>();
        set.addAll(jsonMap1.keySet());
        set.addAll(jsonMap2.keySet());

        set.stream()
                .sorted()
                .forEach(keyLine -> {
                    boolean keyInV1 = jsonMap1.containsKey(keyLine);
                    boolean keyInV2 = jsonMap2.containsKey(keyLine);
                    String value = ITEM_UNCHANGED;

                    if (keyInV1 && keyInV2) {
                        if (!Objects.equals(jsonMap2.get(keyLine), jsonMap1.get(keyLine))) {
                            value = ITEM_CHANGED;
                        }
                    } else if (keyInV1 && !keyInV2) {
                        value = ITEM_DELETED;
                    } else if (!keyInV1 && keyInV2) {
                        value = ITEM_ADDED;
                    }

                    result.put(keyLine, value);
                });

        return result;
    }

    public static String generate(String filePath1, String filePath2, String format) throws Exception {
        if (filePath1.isEmpty() || filePath1 == null || filePath2.isEmpty() || filePath2 == null) {
            throw new NullPointerException("The paramentres 'filePath1' and 'filePath2' must be not empty!");
        }

        StringBuilder result = new StringBuilder();

        String formatUsed = format.toLowerCase();
        if (!FORMAT_LIST.contains(formatUsed)) {
            throw new Exception(String.format("There is no such format '%s'", format));
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        mapper.disable(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature());

        Map<String, Object> jsonMap1 = getFileData(filePath1, mapper);
        Map<String, Object> jsonMap2 = getFileData(filePath2, mapper);

        Map<String, String> differences = genDiff(jsonMap1, jsonMap2);

        if (differences.size() > 0) {
            result.append("{\n");
            differences.entrySet().stream()
                    .forEach(mapEntry -> {
                        switch (mapEntry.getValue()) {
                            case (ITEM_UNCHANGED):
                                result.append(String.format("    %s: %s\n", mapEntry.getKey(),
                                        jsonMap1.get(mapEntry.getKey())));
                                break;
                            case (ITEM_CHANGED):
                                result.append(String.format("  - %s: %s\n", mapEntry.getKey(),
                                        jsonMap1.get(mapEntry.getKey())));
                                result.append(String.format("  + %s: %s\n", mapEntry.getKey(),
                                        jsonMap2.get(mapEntry.getKey())));
                                break;
                            case (ITEM_DELETED):
                                result.append(String.format("  - %s: %s\n", mapEntry.getKey(),
                                        jsonMap1.get(mapEntry.getKey())));
                                break;
                            case (ITEM_ADDED):
                                result.append(String.format("  + %s: %s\n", mapEntry.getKey(),
                                        jsonMap1.get(mapEntry.getKey())));
                                break;
                            default:
                        }
                    });
            result.append("}");
        } else {
            result.append("{}");
        }

//        for (Map.Entry<String, String> mapEntry : differences.entrySet()) {
//            System.out.println(mapEntry.getKey() + "--->" + mapEntry.getValue());
//        }

//        System.out.println(format);
//        mapper.writeValue(System.out, differences);
//
//        System.out.println(mapper
//                .writer()
//                .withDefaultPrettyPrinter()
//                .writeValueAsString(differences));
        System.out.println(result.toString());
        return result.toString();
    }

}
