import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class TypeCommand extends BuiltinCommand{
    @Override
    public void execute(List<String> arguments, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException {
        if (arguments.size() != 1) {
            err.write("type takes exactly one argument\n".getBytes(StandardCharsets.UTF_8));
            err.flush();
            return;
        }

        String typeArg = arguments.getFirst();
        if (CommandFactory.builtinCommands().contains(typeArg)) {
            out.write((typeArg + " is a shell builtin\n").getBytes(StandardCharsets.UTF_8));
        } else {
            String[] pathsToCheck = env.getEnv("PATH")
                    .split(File.pathSeparator);
            Path execPath = null;
            for (String p : pathsToCheck) {
                Path pathToCheck = Path.of(p);
                if (Files.exists(pathToCheck)) {
                    Stream<Path> stream = Files.list(pathToCheck);
                    for (Path path : stream.toList()) {
                        if (path.getFileName().toString().equals(typeArg)
                                && Files.isExecutable(path)) {
                            stream.close();
                            execPath = pathToCheck.resolve(Path.of(typeArg));
                        }
                    }
                    stream.close();
                }
            }
            if (execPath != null) {
                out.write((typeArg + " is " + execPath + "\n").getBytes(StandardCharsets.UTF_8));
                out.flush();
            } else {
                err.write((typeArg + ": not found\n").getBytes(StandardCharsets.UTF_8));
                err.flush();
            }
        }
    }
}
