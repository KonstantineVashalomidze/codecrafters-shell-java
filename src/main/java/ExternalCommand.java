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

        try (ExecutorService executorService = Executors.newFixedThreadPool(3)) {
            Process process = processBuilder.start();
            try {
                Future<?> inputFuture = executorService.submit(() -> {
                    try (OutputStream processOut = process.getOutputStream()) {
                        in.transferTo(processOut);
                    } catch (IOException e) {}
                });

                Future<?> outputFuture = executorService.submit(() -> {
                    try {
                        InputStream inputStream = process.getInputStream();
                        inputStream.transferTo(out);
                    } catch (IOException e) {}
                });

                Future<?> errorFuture = executorService.submit(() -> {
                    try {
                        InputStream errorStream = process.getErrorStream();
                        errorStream.transferTo(err);
                    } catch (IOException e) {}
                });

                int exitCode = process.waitFor();

                inputFuture.get();
                outputFuture.get();
                errorFuture.get();

            } catch (ExecutionException e) {

            }


        } catch (IOException e) {
            String errorMessage = programName + ": command not found\n";
            err.write(errorMessage.getBytes(StandardCharsets.UTF_8));
            err.flush();
        }


    }
}
