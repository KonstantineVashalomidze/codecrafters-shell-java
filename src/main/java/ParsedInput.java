import java.util.List;

public record ParsedInput(String command, List<String> arguments, String filePath) {

}
