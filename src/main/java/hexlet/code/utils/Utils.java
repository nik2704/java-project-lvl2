package hexlet.code.utils;

import java.util.Map;

public class Utils {
    private static final Map<String, String> FORMAT_NAMES  = Map.of(
            "json", "json",
            "yaml", "yaml",
            "yml", "yaml"
    );

    public static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    public static String getFormatName(String extension) throws Exception {
        if (extension.isEmpty()) {
            throw new Exception("File must have an extension!");
        }

        if (!FORMAT_NAMES.containsKey(extension)) {
            throw new Exception("Unknown format of a file (." + extension + ")!");
        }

        return FORMAT_NAMES.get(extension.toLowerCase());
    }
}
