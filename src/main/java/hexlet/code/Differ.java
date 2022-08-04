package hexlet.code;

import hexlet.code.utils.CalcDiff;
import hexlet.code.utils.Utils;
import hexlet.code.utils.Utils.FileFormats;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


public final class Differ {

    public static String generate(String filePath1, String filePath2) throws Exception {
        return generate(filePath1, filePath2, "stylish");
    }
    public static String generate(String filePath1, String filePath2, String outputFormat) throws Exception {
        outputFormat = outputFormat.toLowerCase();

        Utils.checkParametres(new String[]{filePath1, filePath2}, outputFormat);

        FileFormats fileFormat = Utils.getFormatName(FilenameUtils.getExtension(filePath1));
        Map<String, Object> fileData1 = Parser.parseFileData(readFile(filePath1), fileFormat);
        Map<String, Object> fileData2 = Parser.parseFileData(readFile(filePath2), fileFormat);

        Map<String, Map<String, Object>> differences = CalcDiff.getDifference(fileData1, fileData2);

        return Formatter.getFormattedData(differences, outputFormat);
    }

    private static String readFile(String path) throws Exception {
        Path fullPath = Paths.get(path).toAbsolutePath().normalize();
        return Files.readString(fullPath);
    }
}
