package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CommandHandler commandhandler = new CommandHandler();
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("> ");
            String command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting the command line interpreter.");
                break;
            }
            commandhandler.parseCommand(command);
        }
        scanner.close();
    }
}