package hexlet.code;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import hexlet.code.utils.Utils.FileFormats;

import java.util.Map;


public class Parser {

    public static Map<String, Object> parseFileData(String data, FileFormats dataFormat) throws Exception {
        if (dataFormat == null) {
            throw new Exception(String.format("Data format must have a value"));
        }

        ObjectMapper mapper = dataFormat.equals(FileFormats.YAML)
                ? new ObjectMapper(new YAMLFactory()) : new ObjectMapper();
        return mapper.readValue(data, new TypeReference<Map<String, Object>>() { });
    }
}
