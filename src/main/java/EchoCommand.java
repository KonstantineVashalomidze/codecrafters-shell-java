import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EchoCommand extends BuiltinCommand {

    @Override
    public void execute(String[] args, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException {
        if (args.length != 1) {
            err.write("echo takes exactly one argument\n".getBytes(StandardCharsets.UTF_8));
            err.flush();
            return;
        }

        String echoValue = args[0];
        out.write((echoValue + "\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}
