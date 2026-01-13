import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface ICommand {
    void execute(List<String> arguments, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException, InterruptedException;
}
