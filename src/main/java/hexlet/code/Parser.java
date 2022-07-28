package hexlet.code;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import hexlet.code.utils.Utils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class Parser {

    public static Map<String, String> parseFileData(String filePath) throws Exception {

        String fileFormat = Utils.getFormatName(FilenameUtils.getExtension(filePath));

        ObjectMapper mapper = new ObjectMapper();

        if (fileFormat.equals("yaml")) {
            mapper = new ObjectMapper(new YAMLFactory());
        }

//        return getFileData(filePath, mapper);
        try {
            return mapper.readValue(new File(filePath),
                    new TypeReference<Map<String, Object>>() { }).entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey,
                            entry -> Objects.isNull(entry.getValue()) ? "null" : entry.getValue().toString()));

        } catch (Exception err) {
            System.out.println(err.getMessage());
            throw new IOException("Unable to parse " + filePath + "\n" + err.getMessage());
        }
    }

//    private static Map<String, String> getFileData(String fileName, ObjectMapper mapper) throws Exception {
//        File file = new File(fileName);
//        Map<String, String> result = new HashMap<>();
//
//        try {
//            mapper.readTree(file);
//            JsonNode rootNode = mapper.readTree(file);
//
//            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
//            while (fieldsIterator.hasNext()) {
//                Map.Entry<String, JsonNode> field = fieldsIterator.next();
//                JsonNodeType nodeType = field.getValue().getNodeType();
//                result.put(field.getKey(), field.getValue().toString());
//            }
//        } catch (Exception err) {
//            throw new IOException(String.format("Incorrect format of the file '%s'", fileName));
//        }
//
//
//        return result;
//    }
}
