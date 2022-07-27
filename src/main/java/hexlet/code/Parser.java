package hexlet.code;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import hexlet.code.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.Iterator;

import static hexlet.code.utils.Utils.FIRST_MAP_KEY;
import static hexlet.code.utils.Utils.SECOND_MAP_KEY;

public class Parser {

    public static Map<String, Map<String, String>> parseFileData(String filePath1, String filePath2) throws Exception {

        checkFile(filePath1);
        checkFile(filePath2);

        String fileFormat = "unknown";
        String fileFormat1 = Utils.getFormatName(Utils.getFileExtension(filePath1));
        String fileFormat2 = Utils.getFormatName(Utils.getFileExtension(filePath2));

        if (Objects.equals(fileFormat1, fileFormat2)) {
            fileFormat = fileFormat1;
        } else {
            throw new Exception(String.format("Formats of the files are different: '%s' and '%s'",
                    fileFormat1, fileFormat2));
        }

        Map<String, Map<String, String>> result = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        if (fileFormat.equals("yaml")) {
            mapper = new ObjectMapper(new YAMLFactory());
        }

        result.put(FIRST_MAP_KEY, getFileData(filePath1, mapper));
        result.put(SECOND_MAP_KEY, getFileData(filePath2, mapper));

        return result;
    }

    private static void checkFile(String fileName) throws IOException {
        File file = new File(fileName);

        if (!file.isFile()) {
            throw new IOException(String.format("There is no file with name '%s'", fileName));
        }

        if (!file.canRead()) {
            throw new IOException(String.format("Unable to read the file '%s'", fileName));
        }
    }

    private static Map<String, String> getFileData(String fileName, ObjectMapper mapper) throws Exception {
        File file = new File(fileName);
        Map<String, String> result = new HashMap<>();

        try {
            mapper.readTree(file);
            JsonNode rootNode = mapper.readTree(file);

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {
                Map.Entry<String, JsonNode> field = fieldsIterator.next();
                JsonNodeType nodeType = field.getValue().getNodeType();
//                if (nodeType.equals(JsonNodeType.STRING)) {
//                    result.put(field.getKey(), field.getValue().asText());
//                } else {
                result.put(field.getKey(), field.getValue().toString());
//                }
            }
        } catch (IOException err) {
            throw new IOException(String.format("Incorrect format of the file '%s'", fileName));
        }

        return result;
    }
}
