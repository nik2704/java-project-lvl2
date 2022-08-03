package hexlet.code;

import hexlet.code.utils.Differences;
import hexlet.code.utils.Utils;
import hexlet.code.utils.Utils.FileFormats;
import org.apache.commons.io.FilenameUtils;
import java.util.Map;


public final class Differ {

    public static String generate(String filePath1, String filePath2) throws Exception {
        return generate(filePath1, filePath2, "stylish");
    }
    public static String generate(String filePath1, String filePath2, String outputFormat) throws Exception {
        outputFormat = outputFormat.toLowerCase();

        Utils.checkParametres(new String[]{filePath1, filePath2}, outputFormat);

        FileFormats fileFormat = Utils.getFormatName(FilenameUtils.getExtension(filePath1));
        Map<String, Object> fileData1 = Parser.parseFileData(Utils.readFile(filePath1), fileFormat);
        Map<String, Object> fileData2 = Parser.parseFileData(Utils.readFile(filePath2), fileFormat);

        Map<String, Map<String, Object>> differences = Differences.getDifference(fileData1, fileData2);

        return Formatter.getFormattedData(differences, outputFormat);
    }
}
