package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Help.Visibility.NEVER;


//public class App implements Callable<Integer> {
@Command(name = "gendiff",
        description="Compares two configuration files and shows a difference.",
        headerHeading="%n")
public class App {
    private final String OUTPUT_FORMAT = "stylish";

    @Option(names = {"-f", "--format"},
            paramLabel = "format",
            description = "output format [default: " + OUTPUT_FORMAT +"]",
            showDefaultValue = NEVER,
            defaultValue = OUTPUT_FORMAT)
    String format;

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Show this help message and exit.")
    boolean usageHelpRequested;

    @Option(names = {"-V", "--version"}, versionHelp = true, description = "Print version information and exit.")
    boolean versionInfoRequested;

    @Parameters(paramLabel="filepath1", description = "path to first file")
    File filepath1;

    @Parameters(paramLabel="filepath2", description = "path to second file")
    File filepath2;
    public static void main(String[] args) {
//        int exitCode = new CommandLine(new App()).execute(args);
        CommandLine commandLine = new CommandLine(new App());
        commandLine.parseArgs(args);
        if (commandLine.isUsageHelpRequested()) {
            commandLine.usage(System.out);
            return;
        } else if (commandLine.isVersionHelpRequested()) {
            commandLine.printVersionHelp(System.out);
            return;
        }
//        System.exit(exitCode);
    }

//    @Override
//    public Integer call() throws Exception {
//        return null;
//    }
}
