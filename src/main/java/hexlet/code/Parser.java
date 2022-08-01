package hexlet.code;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.util.Map;


public class Parser {

    public static Map<String, Object> parseFileData(String data, String dataFormat) throws Exception {
        dataFormat = dataFormat.toLowerCase();
        if (!dataFormat.equals("yaml") && !dataFormat.equals("json")) {
            throw new Exception(String.format("Incorrect format '%s'", dataFormat));
        }

        ObjectMapper mapper =  dataFormat.equals("yaml") ? new ObjectMapper(new YAMLFactory()) : new ObjectMapper();
        return mapper.readValue(data, new TypeReference<Map<String, Object>>() { });
    }
}
