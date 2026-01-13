import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ICommand {
    void execute(String[] args, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException, InterruptedException;
}
