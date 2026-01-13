import java.io.IOException;
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
        command.execute(parsedInput.args(), System.in, System.out, System.err, environment);
    }
}
