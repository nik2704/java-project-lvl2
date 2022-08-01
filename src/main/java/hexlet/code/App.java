package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.concurrent.Callable;


@Command(name = "gendiff", mixinStandardHelpOptions = true,
        description = "Compares two configuration files and shows a difference.",
        version = "Project 2. Version 1.0",
        headerHeading = "%n")
public final class App implements Callable<Integer> {
    private static final String DEFAULT_OUTPUT_FORMAT = "stylish";

    @Option(names = { "-f", "--format" },
            paramLabel = "format",
            defaultValue = DEFAULT_OUTPUT_FORMAT,
            description = "output format [default: ${DEFAULT-VALUE}]")
    private String outputFormat;

    @Parameters(paramLabel = "filepath1", index = "0", description = "path to first file")
    private String filePath1;

    @Parameters(paramLabel = "filepath2", index = "1", description = "path to second file")
    private String filePath2;

    public static void main(String[] args) throws Exception {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        try {
            String diff = Differ.generate(this.filePath1, this.filePath2, this.outputFormat);
            System.out.println(diff);
        } catch (IOException err) {
            System.out.println("Unable to process: " + err.getMessage());
        } catch (Exception err) {
            System.out.println("The error: " + err.getMessage());
        }
        return null;
    }
}
