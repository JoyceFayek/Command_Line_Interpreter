import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    String commandName;
    String[] args;
    ArrayList<String> commandssList;

    {
        commandssList = new ArrayList<>(Arrays.asList("echo", "pwd", "cd", "ls", "mkdir", "rmdir",
                "touch", "cp", "rm", "cat","exit"));
    }

    public boolean parse(String input) {
        for (int i = 0; i < 13; i++) {
            String[] splitt = input.split("[ ]+");
            if (commandssList.contains(splitt[0])) {
                commandName = splitt[0];
                args = new String[splitt.length - 1];
                System.arraycopy(splitt, 1, args, 0, args.length);
                return true;
            } else {
                System.out.println("can not find a command");
                return false;
            }
        }
        return false;

    }



    public String getCommandName(){
                return commandName;
    }
    public String[] getArgs(){
        return args;
    }

}
