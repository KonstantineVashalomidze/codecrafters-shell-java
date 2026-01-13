import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Environment {
    private Map<String, String> variables;
    private Path currentDirectory;

    public static Environment INSTANCE = new Environment();

    private Environment() {
        variables = System.getenv();
        currentDirectory = Paths.get("").toAbsolutePath();
    }


    public Path getCurrentDirectory() {
        return currentDirectory;
    }


    public String getEnv(String key) {
        return variables.get(key);
    }


    public void setCurrentDirectory(Path currentDirectory) {
        this.currentDirectory = currentDirectory;
    }
}
