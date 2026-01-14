import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Shell {
    private Environment environment;
    private CommandParser commandParser;
    private CommandFactory commandFactory;


    public Shell() {
        environment = Environment.INSTANCE;
        commandParser = new CommandParser();
        commandFactory = new CommandFactory();
    }



    private void printPrompt() {
        // environment.getCurrentDirectory() +
        IO.print("$ ");
    }


    public void start() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        String commandLine;
        printPrompt();
        while (scanner.hasNextLine()) {
            commandLine = scanner.nextLine();

            if (!commandLine.trim().isEmpty())
                repl(commandLine);
            printPrompt();
        }
    }

    private void repl(String commandLine) throws IOException, InterruptedException {
        ParsedInput parsedInput = commandParser.parse(commandLine);
        ICommand command = commandFactory.getCommand(parsedInput.command());
        if (parsedInput.outputFile() != null) {
            Path outputFilePath = Path.of(parsedInput.outputFile());
            if (Files.exists(outputFilePath)) {
                if (Files.isRegularFile(outputFilePath)) {
                    try (OutputStream outputStream = Files.newOutputStream(outputFilePath)) {
                        command.execute(parsedInput.arguments(), System.in, outputStream, outputStream, environment);
                    }
                }
            } else {
                if (outputFilePath.isAbsolute()) {
                    try (OutputStream outputStream = Files.newOutputStream(Files.createFile(outputFilePath))) {
                        command.execute(parsedInput.arguments(), System.in, outputStream, outputStream, environment);
                    }
                } else {
                    try (OutputStream outputStream = Files.newOutputStream(
                            Files.createFile(
                            environment.getCurrentDirectory()
                            .toAbsolutePath()
                            .resolve(outputFilePath)
                            .normalize()))) {
                        command.execute(parsedInput.arguments(), System.in, outputStream, outputStream, environment);
                    }
                }
            }
        } else {
            command.execute(parsedInput.arguments(), System.in, System.out, System.err, environment);
        }
    }
}
