import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String command;
        System.out.print("$ ");
        while ((command = scanner.nextLine()) != null) {
            if (command.startsWith("type")) {
                String typeArg = command.split(" ", 2)[1];
                if (Set.of("exit", "echo", "type").contains(typeArg)) {
                    System.out.println(typeArg + " is a shell builtin");
                } else {
                    String[] pathsToCheck = System.getenv("PATH")
                            .split(File.pathSeparator);
                    boolean foundExecutable = false;
                    for (String p : pathsToCheck) {
                        Path pathToCheck = Path.of(p);
                        if (Files.exists(pathToCheck)) {
                            try (Stream<Path> files = Files.list(pathToCheck)) {
                                foundExecutable = files.anyMatch(path -> {
                                    if (path.getFileName().toString().equals(typeArg)
                                            && Files.isExecutable(path)) {
                                        System.out.println(typeArg + " is " + pathToCheck.resolve(typeArg));
                                        return true;
                                    } else {
                                        return false;
                                    }
                                });
                            }
                        }
                        if (foundExecutable)
                            break;

                    }
                    if (!foundExecutable) {
                        System.out.println(typeArg + ": not found");
                    }

                }
            } else if (command.equals("exit")) break;
            else if (command.startsWith("echo")) {
                String echoArg = command.split(" ", 2)[1];
                System.out.println(echoArg);
            } else {
                System.out.println(command + ": command not found");
            }
            System.out.print("$ ");
        }
    }
}
