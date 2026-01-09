import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

public class Main {

    public static Path findExecutable(String execName) throws IOException {
        String[] pathsToCheck = System.getenv("PATH")
                .split(File.pathSeparator);

        for (String p : pathsToCheck) {
            Path pathToCheck = Path.of(p);
            if (Files.exists(pathToCheck)) {
                Stream<Path> stream = Files.list(pathToCheck);
                for (Path path : stream.toList()) {
                    if (path.getFileName().toString().equals(execName)
                            && Files.isExecutable(path)) {
                        stream.close();
                        return pathToCheck.resolve(execName);
                    }
                }
                stream.close();
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String command;
        Path currentDir = Paths.get("").toAbsolutePath();
        System.out.print("$ ");
        while ((command = scanner.nextLine()) != null) {
            if (command.startsWith("cd")) {
                Path changeTo = Path.of(command.split(" ")[1]);
                if (changeTo.toString().equals("~")) {
                    changeTo = Path.of(System.getenv("HOME"));
                }
                Path potentialPath = currentDir.resolve(changeTo).normalize();
                if (Files.exists(potentialPath) && Files.isDirectory(potentialPath)) {
                    currentDir = potentialPath;
                } else if (changeTo.isAbsolute()) {
                    System.out.println("cd: " + changeTo + ": No such file or directory");
                }
            } else if (command.equals("pwd")) {
                System.out.println(currentDir);
            } else if (command.startsWith("type")) {
                String typeArg = command.split(" ", 2)[1];
                if (Set.of("exit", "echo", "type", "pwd", "cd").contains(typeArg)) {
                    System.out.println(typeArg + " is a shell builtin");
                } else {
                    Path execPath = findExecutable(typeArg);
                    if (execPath != null) System.out.println(typeArg + " is " + execPath);
                    else System.out.println(typeArg + ": not found");
                }
            } else if (command.equals("exit")) break;
            else if (command.startsWith("echo")) {
                String echoArg = command.split(" ", 2)[1];
                System.out.println(echoArg);
            } else {
                String[] execFileNameWithArgs = command.split(" ");
                Path execPath = Path.of(execFileNameWithArgs[0]);
                if (!execPath.isAbsolute()) {
                    execPath = findExecutable(execFileNameWithArgs[0]);
                }

                if (execPath != null) { // Probably exec file
                    ProcessBuilder pb = new ProcessBuilder(execFileNameWithArgs);
                    pb.inheritIO();

                    Process process = pb.start();

                    process.waitFor();
                } else {
                    System.out.println(command + ": command not found");
                }
            }
            System.out.print("$ ");
        }
    }
}
