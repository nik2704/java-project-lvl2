package hexlet.code;

import hexlet.code.formatters.Plain;
import hexlet.code.formatters.Stylish;

import java.util.Map;

public class Formatter {
    public static String getFormattedData(Map<String, Map<String, String>> fileData,
                                          Map<String, String> differences,
                                          String outputFormat) {
        switch (outputFormat) {
            case ("plain"):
                return Plain.getResult(fileData, differences);
            default:
                return Stylish.getResult(fileData, differences);
        }
    }
}
