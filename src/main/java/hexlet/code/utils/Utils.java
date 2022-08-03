package hexlet.code.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.List;


public class Utils {
    public static final String ITEM_UNCHANGED = "unchanged";
    public static final String ITEM_CHANGED = "changed";
    public static final String ITEM_DELETED = "deleted";
    public static final String ITEM_ADDED = "added";
    public static final List<String> OUTPUT_FORMAT_LIST = List.of("stylish", "plain", "json");

    public enum FileFormats {
        YAML,
        JSON
    }
    private static final Map<String, FileFormats> FORMAT_NAMES  = Map.of(
            "json", FileFormats.JSON,
            "yaml", FileFormats.YAML,
            "yml", FileFormats.YAML
    );

    public static FileFormats getFormatName(String extension) throws Exception {
        if (extension.isEmpty()) {
            throw new Exception("File must have an extension!");
        }

        if (!FORMAT_NAMES.containsKey(extension)) {
            throw new Exception("Unknown format of a file (." + extension + ")!");
        }

        return FORMAT_NAMES.get(extension.toLowerCase());
    }

    public static String readFile(String path) throws Exception {
        Path fullPath = Paths.get(path).toAbsolutePath().normalize();
        return Files.readString(fullPath);
    }

    public static void checkParametres(String[] filePaths, String commonOutputFormat) throws Exception {
        if (commonOutputFormat.isEmpty()) {
            throw new NullPointerException("The FORMAT must not be not empty!");
        }

        Set<FileFormats> extensions = new HashSet<>();

        for (String filePath : filePaths) {
            checkFile(filePath);
            extensions.add(getFormatName(FilenameUtils.getExtension(filePath)));
        }

        if (extensions.size() > 1) {
            throw new Exception(String.format("Formats of the files are different: %s", extensions.toString()));
        }

        if (!OUTPUT_FORMAT_LIST.contains(commonOutputFormat)) {
            throw new Exception(String.format("There is no such format '%s'", commonOutputFormat));
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
}
