import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandFactory {
    private static final Map<String, ICommand> builtins = new HashMap<>();

    static {
        builtins.put("cd", new CdCommand());
        builtins.put("pwd", new PwdCommand());
        builtins.put("type", new TypeCommand());
        builtins.put("echo", new EchoCommand());
        builtins.put("exit", new ExitCommand());
    }


    public ICommand getCommand(String name) {
        if (builtins.containsKey(name)) {
            return builtins.get(name);
        }

        return new ExternalCommand(name);
    }


    public static Set<String> builtinCommands() {
        return builtins.keySet();
    }


}
