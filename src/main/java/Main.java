import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String command = null;
        System.out.print("$ ");
        while ((command = scanner.nextLine()) != null) {
            System.out.println(command + ": command not found");
            System.out.print("$ ");
        }
    }
}
