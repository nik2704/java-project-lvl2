package hexlet.code;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class TestParser {
    private final int timeout = 50;
    private Path resourceDirectory = Paths.get("src", "test", "resources");
    private static String readFile(String path) {
        Path fullPath = Paths.get(path).toAbsolutePath().normalize();
        String content = "";
        try {
            content = Files.readString(fullPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return content;
    }

    @ParameterizedTest
    @CsvSource(value = {
            "file1.json, json",
            "file1.yaml, yaml"
        }, ignoreLeadingAndTrailingWhitespace = true)
    public void testParserJson(String fileName, String formatName) throws Exception {
        Map<String, Object> expected1 = Map.of(
                "host", "hexlet.io",
                "timeout", timeout,
                "proxy", "123.234.53.22",
                "follow", false
        );

        String jsonData = readFile(resourceDirectory.toFile().getAbsolutePath() + "/" + fileName);
        Map<String, Object> actual1 = Parser.parseFileData(jsonData, formatName);
        assertThat(expected1).isEqualTo(actual1);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "file1.json, csv, java.lang.Exception",
            "file1.yaml, csv, java.lang.Exception"
        }, ignoreLeadingAndTrailingWhitespace = true)
    public void testJsonWrongFileComparison1(String fileName, String formatName, Class type) {
        String jsonData = readFile(resourceDirectory.toFile().getAbsolutePath() + "/" + fileName);
        assertThatThrownBy(() -> Parser.parseFileData(jsonData, formatName)).isInstanceOf(type);
    }

}
