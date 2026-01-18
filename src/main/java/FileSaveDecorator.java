import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileSaveDecorator {
    private final ICommand wrappedCommand;
    private final String filePathString;


    public FileSaveDecorator(ICommand commandToWrap, String filePath) {
        wrappedCommand = commandToWrap;
        filePathString = filePath;
    }

    public void execute(List<String> arguments, InputStream in, OutputStream out, OutputStream err, Environment env) throws IOException, InterruptedException {
        Path filePath = Path.of(filePathString).normalize();
        if (!filePath.isAbsolute()) {
            filePath = env.getCurrentDirectory().resolve(filePath).normalize();
        }

        if (!Files.exists(filePath)) {
            Files.createDirectories(filePath.getParent());
            for (Path p : filePath) {
                if (Files.isDirectory(p) && !Files.exists(p)) {
                    Files.createDirectory(p);
                }
            }
            Files.createFile(filePath);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            wrappedCommand.execute(arguments, in, fos, err, env);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
