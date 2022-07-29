package hexlet.code;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import hexlet.code.utils.Utils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Map;


public class Parser {

    public static Map<String, Object> parseFileData(String filePath) throws Exception {

        String fileFormat = Utils.getFormatName(FilenameUtils.getExtension(filePath));

        ObjectMapper mapper = new ObjectMapper();

        if (fileFormat.equals("yaml")) {
            mapper = new ObjectMapper(new YAMLFactory());
        }

        return mapper.readValue(new File(filePath), new TypeReference<Map<String, Object>>() { });
    }
}
