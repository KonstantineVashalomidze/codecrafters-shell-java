import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExternalCommand implements ICommand {

    private final String programName;

    public ExternalCommand(String name) {
        this.programName = name;
    }

    @Override
    public void execute(List<String> arguments, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException, InterruptedException {

        List<String> command = new ArrayList<>();
        command.add(programName);
        command.addAll(arguments);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(env.getCurrentDirectory().toFile());

        Process process = processBuilder.start();

        Thread outThread = Thread.ofVirtual()
                .start(() -> {
                    try {
                        process.getInputStream().transferTo(out);
                    } catch (IOException _) {
                    }
                });

        Thread errThread = Thread.ofVirtual()
                .start(() -> {
                    try {
                        process.getErrorStream().transferTo(err);
                    } catch (IOException _) {
                    }
                });


        Thread inThread = Thread.ofVirtual()
                .start(() -> {
                    try (OutputStream processOutputStream = process.getOutputStream()) {
                        if (in != null) {
                            in.transferTo(processOutputStream);
                        }
                    } catch (IOException _) {
                    }
                });

        process.waitFor();

        outThread.join();
        errThread.join();
    }
}
