package hexlet.code;

import hexlet.code.formatters.Json;
import hexlet.code.formatters.Plain;
import hexlet.code.formatters.Stylish;

import java.util.Map;

public class Formatter {
    public static String getFormattedData(Map<String, Map<String, Object>> differences,
                                          String outputFormat) {
        switch (outputFormat) {
            case ("json"):
                return Json.getResult(differences);
            case ("plain"):
                return Plain.getResult(differences);
            default:
                return Stylish.getResult(differences);
        }
    }
}
