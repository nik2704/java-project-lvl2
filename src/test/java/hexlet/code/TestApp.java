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
    private String outputFormat;
    private String incorrectJsonFilePath;

    @BeforeAll
    public void beforeEach() {
        filePath1 = resourceDirectory.toFile().getAbsolutePath() + "/file1.json";
        filePath2 = resourceDirectory.toFile().getAbsolutePath() + "/file2.json";
        incorrectJsonFilePath = resourceDirectory.toFile().getAbsolutePath() + "/incorrectFile1.json";
        outputFormat = "stylish";
    }

    @Test
    public void testJsonCorrectComparison() {
        try {
            String correctAnswer1 = "{\n"
                    + "  - proxy: 123.234.53.22\n"
                    + "    host: hexlet.io\n"
                    + "  - follow: false\n"
                    + "  - timeout: 50\n"
                    + "  + timeout: 20\n"
                    + "  + verbose: null\n"
                    + "}";

            String diff1 = Differ.generate(filePath1, filePath2, outputFormat);
            assertThat(diff1).isEqualTo(correctAnswer1);

            String correctAnswer2 = "{\n"
                    + "  + proxy: null\n"
                    + "    host: hexlet.io\n"
                    + "  + follow: null\n"
                    + "  - timeout: 20\n"
                    + "  + timeout: 50\n"
                    + "  - verbose: true\n"
                    + "}";

            String diff2 = Differ.generate(filePath2, filePath1, outputFormat);
            assertThat(diff2).isEqualTo(correctAnswer2);

        } catch (Exception err) {
            System.out.println("ERROR has appeared in 'generate' method: " + err.getMessage());
        }

//        assertThat(absolutePath).endsWith("src/test/resources");
    }

    @Test
    public void testJsonWrongFileComparison() {
        String wrongFileName = filePath1 + "1";
        assertThatThrownBy(() ->
                Differ.generate(wrongFileName, filePath2, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath2, wrongFileName, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(incorrectJsonFilePath, filePath2, outputFormat)).isInstanceOf(IOException.class);

        assertThatThrownBy(() ->
                Differ.generate(null, filePath2, outputFormat)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() ->
                Differ.generate(filePath2, filePath2, "incorrectFormat")).isInstanceOf(Exception.class);
    }

}
