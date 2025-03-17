package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CommandHandler {

    public void mkdir(String path) {
        File dir = new File(System.getProperty("user.dir"), path);
        if (dir.mkdir()) {
            System.out.println("Directory created successfully: " + dir.getAbsolutePath());
        } else {
            printError("Could not create directory. It may already exist or the path is invalid.");
        }
    }

    public boolean rmdir(String path) {
        File dir = new File(System.getProperty("user.dir"), path);
        if (dir.isDirectory() && dir.list().length == 0) {
            if (dir.delete()) {
                System.out.println("Directory removed successfully: " + dir.getAbsolutePath());
                return true;
            } else {
                printError("Failed to delete the directory. Check permissions or if it's in use.");
            }
        } else {
            printError("Directory is not empty or does not exist.");
        }
        return false;
    }

    public void rm(String filePath) {
        File file = new File(System.getProperty("user.dir"), filePath);
        if (!file.exists()) {
            printError("No such file or directory: " + filePath);
        } else if (file.isDirectory()) {
            printError("Cannot delete directory: " + filePath + ". Use rmdir instead.");
        } else {
            if (file.delete()) {
                System.out.println("File deleted successfully: " + filePath);
            } else {
                printError("Failed to delete the file. Check file permissions or if it is in use.");
            }
        }
    }

    public void mv(String sourcePath, String destPath) {
        Path sourceFile = Paths.get(System.getProperty("user.dir"), sourcePath);
        Path destFile = Paths.get(System.getProperty("user.dir"), destPath);
        try {
            if (!Files.exists(sourceFile)) {
                printError("Source file does not exist: " + sourcePath);
                return;
            }
            if (Files.isDirectory(destFile)) {
                destFile = destFile.resolve(sourceFile.getFileName());
            }
            Files.move(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved/Renamed successfully: " + sourcePath + " to " + destFile.toString());
        } catch (IOException e) {
            printError("Failed to move or rename: " + e.getMessage());
        }
    }

    public void touch(String filename) {
        File file = new File(System.getProperty("user.dir"), filename);
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + filename);
            } else {
                printError("File already exists: " + filename);
            }
        } catch (IOException e) {
            printError("Failed to create file: " + e.getMessage());
        }
    }

    public void echo(String message, String outputFile, boolean appendMode) {
        handleRedirection(message + System.lineSeparator(), outputFile, appendMode);
    }

    public void ls(String directoryPath, boolean showAll, boolean reverse, String outputFile, boolean appendMode) {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            printError("Invalid directory: " + directoryPath);
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            printError("Unable to list files in: " + directoryPath);
            return;
        }
        if (!showAll) {
            files = Arrays.stream(files).filter(file -> !file.getName().startsWith(".")).toArray(File[]::new);
        }
        Arrays.sort(files, reverse ? Comparator.comparing(File::getName).reversed() : Comparator.comparing(File::getName));
        StringBuilder output = new StringBuilder();
        for (File file : files) {
            output.append(file.getName()).append(System.lineSeparator());
        }
        handleRedirection(output.toString(), outputFile, appendMode);
    }

    public void cat(String[] fileNames, String outputFile, boolean appendMode) {
        StringBuilder content = new StringBuilder();
        for (String fileName : fileNames) {
            File file = new File(System.getProperty("user.dir"), fileName);
            if (!file.exists()) {
                printError("File does not exist: " + fileName);
                continue;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                printError("Error reading file: " + e.getMessage());
            }
        }
        handleRedirection(content.toString(), outputFile, appendMode);
    }

    public void pwd() {
        System.out.println(System.getProperty("user.dir"));
    }

    public void cd(String path) {
        File dir;

        if (Paths.get(path).isAbsolute()) {
            dir = new File(path);
        } else if (path.equals("..")) {
            dir = new File(System.getProperty("user.dir")).getParentFile();
        } else {
            dir = new File(System.getProperty("user.dir"), path);
        }

        if (dir != null && dir.exists() && dir.isDirectory()) {
            System.setProperty("user.dir", dir.getAbsolutePath());
            System.out.println("Changed directory to: " + dir.getAbsolutePath());
        } else {
            printError("Invalid path: No such directory.");
        }
    }

    public void printError(String message) {
        System.out.println("Error: " + message);
    }

    public void parseCommand(String input) {
        String[] commands = input.split("\\|");
        String output = null;

        for (String command : commands) {
            String[] tokens = command.trim().split(" ");
            String cmd = tokens[0];
            List<String> argsList = new ArrayList<>(Arrays.asList(tokens).subList(1, tokens.length));

            String outputFile = null;
            boolean appendMode = false;
            int redirectIndex = argsList.indexOf(">");
            int appendIndex = argsList.indexOf(">>");

            if (redirectIndex != -1) {
                outputFile = argsList.get(redirectIndex + 1);
                argsList = argsList.subList(0, redirectIndex);
            } else if (appendIndex != -1) {
                outputFile = argsList.get(appendIndex + 1);
                appendMode = true;
                argsList = argsList.subList(0, appendIndex);
            }

            String[] args = argsList.toArray(new String[0]);

            switch (cmd) {
                case "echo":
                    if (args.length > 0) {
                        String message = String.join(" ", args);
                        echo(message, outputFile, appendMode);
                    } else {
                        printError("Usage: echo <message> [> file | >> file]");
                    }
                    break;
                case "ls":
                    String directoryPath = System.getProperty("user.dir");
                    boolean showAll = false;
                    boolean reverse = false;
                    for (String arg : args) {
                        switch (arg) {
                            case "-a":
                                showAll = true;
                                break;
                            case "-r":
                                reverse = true;
                                break;
                            default:
                                directoryPath = arg;
                        }
                    }
                    ls(directoryPath, showAll, reverse, outputFile, appendMode);
                    break;
                case "cat":
                    if (args.length > 0) {
                        cat(args, outputFile, appendMode);
                    } else {
                        printError("Usage: cat <file1> <file2> ... [> file | >> file]");
                    }
                    break;
                case "pwd":
                    pwd();
                    break;
                case "cd":
                    if (args.length > 0) {
                        cd(args[0]);
                    } else {
                        printError("Usage: cd <path>");
                    }
                    break;
                case "mkdir":
                    if (args.length > 0) {
                        mkdir(args[0]);
                    } else {
                        printError("Usage: mkdir <directory>");
                    }
                    break;
                case "rmdir":
                    if (args.length > 0) {
                        rmdir(args[0]);
                    } else {
                        printError("Usage: rmdir <directory>");
                    }
                    break;
                case "rm":
                    if (args.length > 0) {
                        rm(args[0]);
                    } else {
                        printError("Usage: rm <file>");
                    }
                    break;
                case "mv":
                    if (args.length > 1) {
                        mv(args[0], args[1]);
                    } else {
                        printError("Usage: mv <source> <destination>");
                    }
                    break;
                case "touch":
                    if (args.length > 0) {
                        touch(args[0]);
                    } else {
                        printError("Usage: touch <filename>");
                    }
                    break;
                case "help":
                    help();
                    break;
                default:
                    printError("Unknown command: " + cmd);
            }
        }
    }

    private void handleRedirection(String content, String outputFile, boolean appendMode) {
        if (outputFile != null) {
            try (FileWriter fw = new FileWriter(new File(System.getProperty("user.dir"), outputFile), appendMode);
                 PrintWriter pw = new PrintWriter(fw)) {
                pw.print(content);
            } catch (IOException e) {
                printError("Error writing to file: " + e.getMessage());
            }
        } else {
            System.out.print(content);
        }
    }

    public void help() {
        System.out.println("Supported commands:");
        System.out.println("pwd - Print current directory");
        System.out.println("cd <path> - Change directory");
        System.out.println("cat <file1> <file2> ... [> <outputFile> | >> <outputFile>] - Display file contents");
        System.out.println("mkdir <directory> - Create a new directory");
        System.out.println("rmdir <directory> - Remove a directory");
        System.out.println("rm <file> - Remove a file");
        System.out.println("mv <sourcePath> <destPath> - Move or rename a file");
        System.out.println("touch <filename> - Create a new empty file");
        System.out.println("echo <message> [> <file> | >> <file>] - Write message to console or file");
        System.out.println("ls <directory> [-a] [-r] [> <outputFile> | >> <outputFile>] - List files");
        System.out.println("exit - Exit the CLI");
        System.out.println("help - Display available commands");
    }
}

