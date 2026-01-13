import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExitCommand extends BuiltinCommand{
    @Override
    public void execute(String[] args, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException {
        if (args.length != 0) {
            err.write("exit takes no arguments\n".getBytes(StandardCharsets.UTF_8));
            err.flush();
            return;
        }
        System.exit(0);
    }
}
