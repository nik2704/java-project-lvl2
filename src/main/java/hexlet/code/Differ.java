package hexlet.code;

import hexlet.code.utils.Utils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import static hexlet.code.utils.Utils.ITEM_ADDED;
import static hexlet.code.utils.Utils.ITEM_CHANGED;
import static hexlet.code.utils.Utils.ITEM_DELETED;
import static hexlet.code.utils.Utils.ITEM_UNCHANGED;
import static hexlet.code.utils.Utils.OUTPUT_FORMAT_LIST;

public final class Differ {

    public static String generate(String filePath1, String filePath2) throws Exception {
        return generate(filePath1, filePath2, "stylish");
    }
    public static String generate(String filePath1, String filePath2, String outputFormat) throws Exception {
        checkParametres(filePath1, filePath2, outputFormat);

        outputFormat = outputFormat.toLowerCase();

        String fileFormat = Utils.getFormatName(FilenameUtils.getExtension(filePath1));
        Map<String, Object> fileData1 = Parser.parseFileData(readFile(filePath1), fileFormat);
        Map<String, Object> fileData2 = Parser.parseFileData(readFile(filePath2), fileFormat);

//        Map<String, String> differences = getDifference(fileData1, fileData2);
        Map<String, Map<String, Object>> differences = getDifference(fileData1, fileData2);

        String result = Formatter.getFormattedData(differences, outputFormat);

        return result;
    }

    private static void checkParametres(String filePath1, String filePath2, String outputFormat) throws Exception {
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
        if (fileName.isEmpty()) {
            throw new NullPointerException("The file name must not be empty!");
        }

        File file = new File(fileName);

        if (!file.isFile()) {
            throw new IOException(String.format("There is no file with name '%s'", fileName));
        }

        if (!file.canRead()) {
            throw new IOException(String.format("Unable to read the file '%s'", fileName));
        }
    }

    private static String readFile(String path) {
        Path fullPath = Paths.get(path).toAbsolutePath().normalize();
        String content = "";
        try {
            content = Files.readString(fullPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return content;
    }

    public static Map<String, Map<String, Object>> getDifference(Map<String, Object> map1, Map<String, Object> map2) {
        StringBuilder result = new StringBuilder();

//        Map<String, String> differences = new HashMap<>();
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
