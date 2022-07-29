package hexlet.code;

import hexlet.code.utils.Utils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
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

        Map<String, Map<String, Object>> fileData = new HashMap<>();

        fileData.put(FIRST_MAP_KEY, Parser.parseFileData(filePath1));
        fileData.put(SECOND_MAP_KEY, Parser.parseFileData(filePath2));

        Map<String, String> differences = getDifference(fileData);
//        System.out.println(differences);

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

        checkFile(filePath1);
        checkFile(filePath2);

        String fileFormat1 = Utils.getFormatName(FilenameUtils.getExtension(filePath1));
        String fileFormat2 = Utils.getFormatName(FilenameUtils.getExtension(filePath2));

        if (!Objects.equals(fileFormat1, fileFormat2)) {
            throw new Exception(String.format("Formats of the files are different: '%s' and '%s'",
                    fileFormat1, fileFormat2));
        }

        if (!OUTPUT_FORMAT_LIST.contains(outputFormat.toLowerCase())) {
            throw new Exception(String.format("There is no such format '%s'", outputFormat));
        }
    }

    private static void checkFile(String fileName) throws IOException {
        File file = new File(fileName);

        if (!file.isFile()) {
            throw new IOException(String.format("There is no file with name '%s'", fileName));
        }

        if (!file.canRead()) {
            throw new IOException(String.format("Unable to read the file '%s'", fileName));
        }
    }

    public static Map<String, String> getDifference(Map<String, Map<String, Object>> fileData) {
        StringBuilder result = new StringBuilder();
        Map<String, Object> map1 = fileData.get(FIRST_MAP_KEY);
        Map<String, Object> map2 = fileData.get(SECOND_MAP_KEY);

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
