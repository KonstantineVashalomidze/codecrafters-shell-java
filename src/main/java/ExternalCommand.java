import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExternalCommand implements ICommand {

    private final String programName;

    public ExternalCommand(String name) {
        this.programName = name;
    }

    @Override
    public void execute(String[] args, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException, InterruptedException {

        List<String> command = new ArrayList<>();
        command.add(programName);
        command.addAll(List.of(args));

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        processBuilder.directory(env.getCurrentDirectory().toFile());

        processBuilder.inheritIO();

        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException e) {
            String errorMessage = programName + ": command not found\n";
            err.write(errorMessage.getBytes(StandardCharsets.UTF_8));
            err.flush();
        }


    }
}
