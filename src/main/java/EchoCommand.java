import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EchoCommand extends BuiltinCommand {

    @Override
    public void execute(List<String> arguments, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException {
        out.write((String.join(" ", arguments) + "\n").getBytes(StandardCharsets.UTF_8));
        out.flush();
    }
}
