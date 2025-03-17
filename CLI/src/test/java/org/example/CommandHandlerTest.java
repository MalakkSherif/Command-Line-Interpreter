package org.example;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {

    private CommandHandler commandHandler;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        commandHandler = new CommandHandler();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }

    @Test
    void testMkdir() {
        String dirName = "testDir";
        commandHandler.mkdir(dirName);
        File dir = new File(System.getProperty("user.dir"), dirName);
        assertTrue(dir.exists() && dir.isDirectory());
        assertTrue(outputStream.toString().contains("Directory created successfully"));
        dir.delete();
    }

    @Test
    void testRmdir() {
        String dirName = "testDir";
        File dir = new File(System.getProperty("user.dir"), dirName);
        dir.mkdir();
        assertTrue(dir.exists());

        boolean result = commandHandler.rmdir(dirName);
        assertTrue(result);
        assertFalse(dir.exists());
        assertTrue(outputStream.toString().contains("Directory removed successfully"));
    }

    @Test
    void testRmFile() throws IOException {
        String fileName = "testFile.txt";
        File file = new File(System.getProperty("user.dir"), fileName);
        file.createNewFile();
        assertTrue(file.exists());

        commandHandler.rm(fileName);
        assertFalse(file.exists());
        assertTrue(outputStream.toString().contains("File deleted successfully"));
    }

    @Test
    void testMvFile() throws IOException {
        String fileName = "testFile.txt";
        String destFileName = "movedFile.txt";
        File file = new File(System.getProperty("user.dir"), fileName);
        file.createNewFile();
        assertTrue(file.exists());

        commandHandler.mv(fileName, destFileName);
        File movedFile = new File(System.getProperty("user.dir"), destFileName);
        assertTrue(movedFile.exists());
        assertFalse(file.exists());
        assertTrue(outputStream.toString().contains("Moved/Renamed successfully"));

        movedFile.delete();
    }

    @Test
    void testTouch() {
        String fileName = "testTouch.txt";
        commandHandler.touch(fileName);
        File file = new File(System.getProperty("user.dir"), fileName);
        assertTrue(file.exists());
        assertTrue(outputStream.toString().contains("File created: " + fileName));
        file.delete();
    }

    @Test
    void testEchoToConsole() {
        String message = "Hello, World!";
        commandHandler.echo(message, null, false);
        assertTrue(outputStream.toString().contains(message));
    }

    @Test
    void testEchoToFile() throws IOException {
        String message = "Hello, File!";
        String fileName = "testEchoFile.txt";
        commandHandler.echo(message, fileName, false);

        File file = new File(System.getProperty("user.dir"), fileName);
        assertTrue(file.exists());

        String content = new String(Files.readAllBytes(file.toPath()));
        assertEquals(message + System.lineSeparator(), content);

        file.delete();
    }

    @Test
    void testLs() {
        String testDir = System.getProperty("user.dir");
        commandHandler.ls(testDir, false, false, null, false);
        assertTrue(outputStream.toString().contains("src"));
    }

    @Test
    void testCatFile() throws IOException {
        String fileName = "testCatFile.txt";
        File file = new File(System.getProperty("user.dir"), fileName);
        Files.write(file.toPath(), "Sample content".getBytes());

        commandHandler.cat(new String[]{fileName}, null, false);
        assertTrue(outputStream.toString().contains("Sample content"));

        file.delete();
    }

    @Test
    void testPwd() {
        commandHandler.pwd();
        assertTrue(outputStream.toString().contains(System.getProperty("user.dir")));
    }

    @Test
    void testCd() {
        String currentDir = System.getProperty("user.dir");
        commandHandler.cd("..");
        assertNotEquals(currentDir, System.getProperty("user.dir"));
        commandHandler.cd(currentDir);
    }

    @Test
    void testHelp() {
        commandHandler.help();
        assertTrue(outputStream.toString().contains("Supported commands:"));
    }
}
