import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class CdCommand extends BuiltinCommand {
    @Override
    public void execute(String[] args, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException {
        if (args.length != 1) {
            err.write("cd takes exactly one argument\n".getBytes(StandardCharsets.UTF_8));
            err.flush();
        }

        Path changeTo = Path.of(args[0]);
        if (changeTo.toString().equals("~"))
            changeTo = Path.of(System.getenv("HOME"));

        Path potentialPath = env.getCurrentDirectory().resolve(changeTo).normalize();
        if (Files.exists(potentialPath) && Files.isDirectory(potentialPath)) {
            env.setCurrentDirectory(potentialPath);
        } else {
            err.write(("cd: " + args[0] + ": No such file or directory\n").getBytes(StandardCharsets.UTF_8));
            err.flush();
        }

    }
}
