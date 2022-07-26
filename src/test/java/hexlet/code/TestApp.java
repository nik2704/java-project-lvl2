package hexlet.code;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class TestApp {
    private Path resourceDirectory = Paths.get("src", "test", "resources");
    private String filePath1;
    private String filePath2;
    private String fileNestedPath1;
    private String fileNestedPath2;
    private String yamlFileNestedPath1;
    private String yamlFileNestedPath2;
    private String emptyJsonFile;
    private String yamlFilePath1;
    private String yamlFilePath2;
    private String yamlIncorrectFilePath;
    private String emptyYamlFile;
    private String outputFormat;
    private String incorrectJsonFilePath;

    @BeforeAll
    public void beforeEach() {
        filePath1 = resourceDirectory.toFile().getAbsolutePath() + "/file1.json";
        filePath2 = resourceDirectory.toFile().getAbsolutePath() + "/file2.json";
        incorrectJsonFilePath = resourceDirectory.toFile().getAbsolutePath() + "/incorrectFile1.json";
        emptyJsonFile = resourceDirectory.toFile().getAbsolutePath() + "/empty.json";

        fileNestedPath1 = resourceDirectory.toFile().getAbsolutePath() + "/file1nested.json";
        fileNestedPath2 = resourceDirectory.toFile().getAbsolutePath() + "/file2nested.json";
        yamlFileNestedPath1 = resourceDirectory.toFile().getAbsolutePath() + "/file1nested.yaml";
        yamlFileNestedPath2 = resourceDirectory.toFile().getAbsolutePath() + "/file2nested.yaml";

        yamlFilePath1 = resourceDirectory.toFile().getAbsolutePath() + "/file1.yaml";
        yamlFilePath2 = resourceDirectory.toFile().getAbsolutePath() + "/file2.yaml";
        yamlIncorrectFilePath = resourceDirectory.toFile().getAbsolutePath() + "/incorrectFile1.yaml";
        emptyYamlFile = resourceDirectory.toFile().getAbsolutePath() + "/empty.yml";

        outputFormat = "stylish";
    }

    @Test
    public void testJsonComparison() {
        try {
            String correctAnswer1 = "{\n"
                    + "  - follow: false\n"
                    + "    host: hexlet.io\n"
                    + "  - proxy: 123.234.53.22\n"
                    + "  - timeout: 50\n"
                    + "  + timeout: 20\n"
                    + "  + verbose: true\n"
                    + "}";

            String diff1 = Differ.generate(filePath1, filePath2, outputFormat);
            assertThat(diff1).isEqualTo(correctAnswer1);

            String diff1yml = Differ.generate(yamlFilePath1, yamlFilePath2, outputFormat);
            assertThat(diff1yml).isEqualTo(correctAnswer1);

            String correctAnswer2 = "{\n"
                    + "  + follow: false\n"
                    + "    host: hexlet.io\n"
                    + "  + proxy: 123.234.53.22\n"
                    + "  - timeout: 20\n"
                    + "  + timeout: 50\n"
                    + "  - verbose: true\n"
                    + "}";

            String diff2 = Differ.generate(filePath2, filePath1, outputFormat);
            assertThat(diff2).isEqualTo(correctAnswer2);

            String diff2yml = Differ.generate(yamlFilePath2, yamlFilePath1, outputFormat);
            assertThat(diff2yml).isEqualTo(correctAnswer2);

            String correctAnswer3 = "{}";
            String diff3 = Differ.generate(emptyJsonFile, emptyJsonFile, outputFormat);
            assertThat(diff3).isEqualTo(correctAnswer3);

            String diff3yml = Differ.generate(emptyYamlFile, emptyYamlFile, outputFormat);
            assertThat(diff3yml).isEqualTo(correctAnswer3);

            String correctAnswer4 = "{\n"
                    + "  + host: hexlet.io\n"
                    + "  + timeout: 20\n"
                    + "  + verbose: true\n"
                    + "}";

            String diff4 = Differ.generate(emptyJsonFile, filePath2, outputFormat);
            assertThat(diff4).isEqualTo(correctAnswer4);

            String diff4yml = Differ.generate(emptyYamlFile, yamlFilePath2, outputFormat);
            assertThat(diff4yml).isEqualTo(correctAnswer4);

            String correctAnswer5 = "{\n"
                    + "  - host: hexlet.io\n"
                    + "  - timeout: 20\n"
                    + "  - verbose: true\n"
                    + "}";

            String diff5 = Differ.generate(filePath2, emptyJsonFile, outputFormat);
            assertThat(diff5).isEqualTo(correctAnswer5);

            String diff5yml = Differ.generate(yamlFilePath2, emptyYamlFile, outputFormat);
            assertThat(diff5yml).isEqualTo(correctAnswer5);

            String correctAnswer6 = "{\n"
                    + "    chars1: [a, b, c]\n"
                    + "  - chars2: [d, e, f]\n"
                    + "  + chars2: false\n"
                    + "  - checked: false\n"
                    + "  + checked: true\n"
                    + "  - default: null\n"
                    + "  + default: [value1, value2]\n"
                    + "  - id: 45\n"
                    + "  + id: null\n"
                    + "  - key1: value1\n"
                    + "  + key2: value2\n"
                    + "    numbers1: [1, 2, 3, 4]\n"
                    + "  - numbers2: [2, 3, 4, 5]\n"
                    + "  + numbers2: [22, 33, 44, 55]\n"
                    + "  - numbers3: [3, 4, 5]\n"
                    + "  + numbers4: [4, 5, 6]\n"
                    + "  + obj1: {nestedKey=value, isNested=true}\n"
                    + "  - setting1: Some value\n"
                    + "  + setting1: Another value\n"
                    + "  - setting2: 200\n"
                    + "  + setting2: 300\n"
                    + "  - setting3: true\n"
                    + "  + setting3: none\n"
                    + "}";

            String diff6json = Differ.generate(fileNestedPath1, fileNestedPath2, outputFormat);
            assertThat(diff6json).isEqualTo(correctAnswer6);

            String diff6yml = Differ.generate(yamlFileNestedPath1, yamlFileNestedPath2, outputFormat);
            assertThat(diff6yml).isEqualTo(correctAnswer6);

        } catch (Exception err) {
            System.out.println("ERROR has appeared in 'generate' method: " + err.getMessage());
        }

//        assertThat(absolutePath).endsWith("src/test/resources");
    }

    @Test
    public void testJsonWrongFileComparison() {
        String wrongFileName = "fileisAbsent.json";
        assertThatThrownBy(() ->
                Differ.generate(wrongFileName, filePath2, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath2, wrongFileName, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(incorrectJsonFilePath, filePath2, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(null, filePath2, outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath1, null, outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate("", filePath2, outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath1, "", outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath2, filePath2, "incorrectFormat")).isInstanceOf(Exception.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath2, filePath2, "")).isInstanceOf(Exception.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath2, filePath2, null)).isInstanceOf(Exception.class);

    }

    @Test
    public void testYamlWrongFileComparison() {
        String wrongFileName = "fileisAbsent.yml";
        assertThatThrownBy(() ->
                Differ.generate(wrongFileName, yamlFilePath2, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(yamlFilePath2, wrongFileName, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(yamlIncorrectFilePath, yamlFilePath2, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(null, yamlFilePath2, outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate(yamlFilePath1, null, outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate("", yamlFilePath2, outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate(yamlFilePath1, "", outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate(yamlFilePath1, yamlFilePath1, "incorrectFormat")).isInstanceOf(Exception.class);

        assertThatThrownBy(() ->
                Differ.generate(yamlFilePath2, yamlFilePath2, "")).isInstanceOf(Exception.class);

        assertThatThrownBy(() ->
                Differ.generate(yamlFilePath2, yamlFilePath2, null)).isInstanceOf(Exception.class);

    }

    @Test
    public void testDifferentFormats() {
        assertThatThrownBy(() ->
                Differ.generate(filePath1, yamlFilePath2, outputFormat)).isInstanceOf(Exception.class);
    }
}
