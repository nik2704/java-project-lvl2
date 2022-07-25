package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import hexlet.code.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.Objects;


public final class Differ {
    private static final List<String> OUTPUT_FORMAT_LIST = List.of("stylish");

    public static String generate(String filePath1, String filePath2, String outputFormat) throws Exception {
        checkParametres(filePath1, filePath2, outputFormat);

        String outputFormatUsed = outputFormat.toLowerCase();
        String fileFormat = "unknown";
        String fileFormat1 = Utils.getFormatName(Utils.getFileExtension(filePath1));
        String fileFormat2 = Utils.getFormatName(Utils.getFileExtension(filePath2));

        if (Objects.equals(fileFormat1, fileFormat2)) {
            fileFormat = fileFormat1;
        } else {
            throw new Exception(String.format("Formats of the files are different: '%s' and '%s'",
                    fileFormat1, fileFormat2));
        }

        ObjectMapper mapper = getMapper(fileFormat);

        String result = Parser.getDifference(
                getFileData(filePath1, mapper),
                getFileData(filePath2, mapper)
        );

        System.out.println(result);
        return result;
    }

    private static void checkParametres(String filePath1, String filePath2, String outputFormat) throws Exception {
        if (filePath1.isEmpty()) {
            throw new NullPointerException("The paramenter 'filePath1' must not be empty!");
        }

        if (filePath2.isEmpty()) {
            throw new NullPointerException("The paramenter 'filePath2' must not be empty!");
        }

        if (outputFormat.isEmpty()) {
            throw new NullPointerException("The FORMAT must not be not empty!");
        }

        if (!OUTPUT_FORMAT_LIST.contains(outputFormat.toLowerCase())) {
            throw new Exception(String.format("There is no such format '%s'", outputFormat));
        }
    }

    private static ObjectMapper getMapper(String fileFormat) {
        switch (fileFormat) {
            case "yaml":
                return new ObjectMapper(new YAMLFactory());
            default:
                return new ObjectMapper();
        }
    }

    private static Map<String, Object> getFileData(String fileName, ObjectMapper mapper) throws Exception {
        File file = new File(fileName);

        if (!file.isFile()) {
            throw new IOException(String.format("There is no file with name '%s'", fileName));
        }

        if (!file.canRead()) {
            throw new IOException(String.format("Unable to read the file '%s'", fileName));
        }

        if (!mapper.getFactory().getClass().equals(YAMLFactory.class)) {
            try {
                mapper.readTree(file);
            } catch (IOException err) {
                throw new IOException(String.format("Incorrect JSON format of the file '%s'", fileName));
            }
        }

        return mapper.readValue(file, new TypeReference<Map<String, Object>>() { });
    }

}
