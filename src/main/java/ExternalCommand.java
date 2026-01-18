import java.io.*;
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

        if (in == System.in) {
            processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        }
        Process process;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            out.write((programName + ": command not found").getBytes(StandardCharsets.UTF_8));
            out.flush();
            return;
        }

        Thread outThread = Thread.ofVirtual().start(() -> {
            try {
                process.getInputStream().transferTo(out);
            } catch (IOException _) {  }
        });

        Thread errThread = Thread.ofVirtual().start(() -> {
            try {
                process.getErrorStream().transferTo(err);
            } catch (IOException _) { }
        });

        if (in != System.in) {
            try (OutputStream pout = process.getOutputStream()) {
                in.transferTo(pout);
            } catch (IOException _) { }
        };

        process.waitFor();
        outThread.join();
        errThread.join();

    }



}
