import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExitCommand extends BuiltinCommand {
    @Override
    public void execute(List<String> arguments, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException {
        if (!arguments.isEmpty()) {
            err.write("exit takes no arguments\n".getBytes(StandardCharsets.UTF_8));
            err.flush();
            return;
        }
        System.exit(0);
    }
}
