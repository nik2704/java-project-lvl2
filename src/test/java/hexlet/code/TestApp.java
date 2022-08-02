package hexlet.code;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public final class TestApp {
    private Path resourceDirectory = Paths.get("src", "test", "resources");
    private String filePath1;
    private String emptyJsonFile;
    private String yamlFilePath2;
    private String emptyYamlFile;
    private String outputFormat;

    @BeforeAll
    public void beforeEach() {
        filePath1 = resourceDirectory.toFile().getAbsolutePath() + "/file1.json";
        emptyJsonFile = resourceDirectory.toFile().getAbsolutePath() + "/empty.json";
        yamlFilePath2 = resourceDirectory.toFile().getAbsolutePath() + "/file2.yaml";
        emptyYamlFile = resourceDirectory.toFile().getAbsolutePath() + "/empty.yml";
        outputFormat = "stylish";
    }

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
        "file1nested.json, file2nested.json, jsonFormat.json, json",
        "file1nested.yaml, file2nested.yaml, jsonFormat.json, json",
        "file1nested.json, file2nested.json, plainFormat.txt, plain",
        "file1nested.yaml, file2nested.yaml, plainFormat.txt, plain",
        "file1.json, file2.json, stylishFormat1.txt, stylish",
        "file1.yaml, file2.yaml, stylishFormat1.txt, stylish",
        "file2.json, file1.json, stylishFormat2.txt, stylish",
        "file2.yaml, file1.yaml, stylishFormat2.txt, stylish",
        "empty.json, file2.json, stylishFormat4.txt, stylish",
        "empty.yml, file2.yaml, stylishFormat4.txt, stylish",
        "file2.json, empty.json, stylishFormat5.txt, stylish",
        "file2.yaml, empty.yml, stylishFormat5.txt, stylish",
        "file1nested.json, file2nested.json, stylishFormat6.txt, stylish",
        "file1nested.yaml, file2nested.yaml, stylishFormat6.txt, stylish"
        }, ignoreLeadingAndTrailingWhitespace = true)
    public void testJsonFormat(String file1, String file2, String fileAnswer, String format) throws Exception {
        String corresctAnswer1 = readFile(resourceDirectory.toFile().getAbsolutePath()
                + "/answers/" + fileAnswer);

        String diff1json = Differ.generate(
                resourceDirectory.toFile().getAbsolutePath() + "/" + file1,
                resourceDirectory.toFile().getAbsolutePath() + "/" + file2,
                format
        );

        assertThat(diff1json).isEqualTo(corresctAnswer1);
    }



    @Test
    public void testJsonComparison() throws Exception {
        String correctAnswer3 = "{}";
        String diff3 = Differ.generate(emptyJsonFile, emptyJsonFile, outputFormat);
        assertThat(diff3).isEqualTo(correctAnswer3);

        String diff3yml = Differ.generate(emptyYamlFile, emptyYamlFile, outputFormat);
        assertThat(diff3yml).isEqualTo(correctAnswer3);
    }

    @ParameterizedTest
    @CsvSource(value = {
        "wrongFileName.json,file2.json,stylish,java.io.IOException",
        "file2.json,wrongFileName.json,stylish,java.io.IOException",
        "wrongFileName.yml,file2.yaml,stylish,java.io.IOException",
        "file2.yaml,wrongFileName.yml,stylish,java.io.IOException",
        ", file2.json,stylish,java.lang.NullPointerException",
        "file1.json,,stylish,java.lang.NullPointerException",
        " ,file2.json,stylish,java.lang.NullPointerException",
        "file1.json, ,stylish,java.lang.NullPointerException",
        ", file2.yaml,stylish,java.lang.NullPointerException",
        "file1.yaml,,stylish,java.lang.NullPointerException",
        " ,file2.yaml,stylish,java.lang.NullPointerException",
        "file1.yaml, ,stylish,java.lang.NullPointerException",
        "file2.json,file2.json,incorrectFormat,java.lang.Exception",
        "file2.json,file2.json, ,java.lang.Exception",
        "file2.json,file2.json,,java.lang.Exception",
        "file2.yaml,file2.yaml,incorrectFormat,java.lang.Exception",
        "file2.yaml,file2.yaml, ,java.lang.Exception",
        "file2.yaml,file2.yaml,,java.lang.Exception"
        })
    public void testJsonWrongFileComparison1(String file1, String file2, String format, Class type) {
        final String f1 = file1 != null ? resourceDirectory.toFile().getAbsolutePath() + "/" + file1 : null;
        final String f2 = file2 != null ? resourceDirectory.toFile().getAbsolutePath() + "/" + file2 : null;

        assertThatThrownBy(() -> Differ.generate(f1, f2, format)).isInstanceOf(type);
    }

    @Test
    public void testDifferentFormats() {
        assertThatThrownBy(() ->
                Differ.generate(filePath1, yamlFilePath2, outputFormat)).isInstanceOf(Exception.class);
    }
}
