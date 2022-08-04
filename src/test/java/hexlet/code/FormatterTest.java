package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static hexlet.code.Formatter.getFormattedData;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class FormatterTest {
    private Path resourceDirectory = Paths.get("src", "test", "resources");

    @ParameterizedTest
    @CsvSource(value = {
            "jsonFormat.json, jsonFormat.json, json",
            "jsonFormat.json, plainFormat.txt, plain",
            "jsonFormat.json, stylishFormat6.txt, stylish"
        }, ignoreLeadingAndTrailingWhitespace = true)
    public void testFormatter(String jsonFileName, String fileName, String format) throws Exception {
        String data = readFile(resourceDirectory.toFile().getAbsolutePath() + "/answers/" + jsonFileName);
        String controlData = readFile(resourceDirectory.toFile().getAbsolutePath() + "/answers/" + fileName);
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Map<String, Object>> map =
                mapper.readValue(data, new TypeReference<Map<String, Map<String, Object>>>() { });

        assertThat(controlData).isEqualTo(getFormattedData(map, format));
    }

    private static String readFile(String path) throws Exception {
        Path fullPath = Paths.get(path).toAbsolutePath().normalize();
        return Files.readString(fullPath);
    }
}
