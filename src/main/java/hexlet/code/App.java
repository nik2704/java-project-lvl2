package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.concurrent.Callable;


@Command(name = "gendiff",
        description = "Compares two configuration files and shows a difference.",
        headerHeading = "%n")
public final class App implements Callable<Integer> {
    private static final String OUTPUT_FORMAT = "stylish";

    @Option(names = { "-f", "--format" },
            paramLabel = "format",
            defaultValue = OUTPUT_FORMAT,
            description = "output format [default: ${DEFAULT-VALUE}]")
    private String format;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Show this help message and exit.")
    private boolean usageHelpRequested;

    @Option(names = {"-V", "--version"}, versionHelp = true, description = "Print version information and exit.")
    private boolean versionInfoRequested;

    @Parameters(paramLabel = "filepath1", index = "0", description = "path to first file")
    private String filePath1;

    @Parameters(paramLabel = "filepath2", index = "1", description = "path to second file")
    private String filePath2;
    public static void main(String[] args) throws Exception {
        int exitCode = 0;
        CommandLine commandLine = new CommandLine(new App());
        commandLine.parseArgs(args);
        if (commandLine.isUsageHelpRequested()) {
            commandLine.usage(System.out);
        } else if (commandLine.isVersionHelpRequested()) {
            commandLine.printVersionHelp(System.out);
        } else {
            exitCode = commandLine.execute(args);
        }
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        try {
            String diff = Differ.generate(this.filePath1, this.filePath2, this.format);
        } catch (IOException err) {
            System.out.println("Unable to process: " + err.getMessage());
        } catch (Exception err) {
            System.out.println("The error: " + err.getMessage());
        }
        return null;
    }
}
