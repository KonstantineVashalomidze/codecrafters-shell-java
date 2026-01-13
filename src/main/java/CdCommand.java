import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CdCommand extends BuiltinCommand {
    @Override
    public void execute(List<String> arguments, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException {
        if (arguments.size() != 1) {
            err.write("cd takes exactly one argument\n".getBytes(StandardCharsets.UTF_8));
            err.flush();
        }

        Path changeTo = Path.of(arguments.getFirst());
        if (changeTo.toString().equals("~"))
            changeTo = Path.of(System.getenv("HOME"));

        Path potentialPath = env.getCurrentDirectory().resolve(changeTo).normalize();
        if (Files.exists(potentialPath) && Files.isDirectory(potentialPath)) {
            env.setCurrentDirectory(potentialPath);
        } else {
            err.write(("cd: " + arguments.getFirst() + ": No such file or directory\n").getBytes(StandardCharsets.UTF_8));
            err.flush();
        }

    }
}
