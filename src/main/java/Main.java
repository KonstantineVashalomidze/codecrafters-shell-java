import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String command = null;
        System.out.print("$ ");
        while ((command = scanner.nextLine()) != null) {
            if (command.equals("exit")) break;
            else if (command.startsWith("echo")) {
                String echoArg = command.split(" ", 2)[1];
                System.out.println(echoArg);
            } else {
                System.out.println(command + ": command not found");
            }
            System.out.print("$ ");
        }
    }
}
