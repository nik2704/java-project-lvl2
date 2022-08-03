package hexlet.code.formatters;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;


public class Json {
    public static String getResult(Map<String, Map<String, Object>> differences) throws IOException {
        return new ObjectMapper()
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(differences);
    }

}
