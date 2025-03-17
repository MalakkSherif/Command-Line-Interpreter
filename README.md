# Command Line Interpreter (CLI)

## 📌 Project Overview
This project is an implementation of a **Command Line Interpreter (CLI)** as part of the **CS341 - Operating Systems 1** course at **Cairo University, Faculty of Computers & Artificial Intelligence**. The CLI mimics a Unix/Linux shell and supports basic system commands, internal commands, and redirection operations.

---

## 🔹 Implemented Features
### ✅ **1. Command Execution**
The CLI can execute standard system commands, including:
- `pwd`: Print the current working directory.
- `cd <directory>`: Change the working directory.
- `ls`: List directory contents.
- `ls -a`: List all directory contents, including hidden files.
- `ls -r`: List directory contents in reverse order.
- `mkdir <directory>`: Create a new directory.
- `rmdir <directory>`: Remove a directory.
- `touch <file>`: Create an empty file.
- `mv <source> <destination>`: Move or rename files.
- `rm <file>`: Delete a file.
- `cat <file>`: Display the content of a file.
- `>`: Redirect output to a file (overwrite).
- `>>`: Redirect output to a file (append).
- `|`: Pipe output from one command to another.
- `exit`: Terminates the CLI session.
- `help`: Displays available commands and their usage.

---

## 🧪 JUnit Testing
### 📌 **Why JUnit?**
JUnit is a widely used framework for writing and running unit tests in Java. It ensures:
- **Code Quality**: Catches bugs early in development.
- **Efficiency**: Saves time by automating test execution.
- **Job Readiness**: Many companies emphasize **Test-Driven Development (TDD)**.
- **Debugging**: Helps isolate errors efficiently.

### 📜 **JUnit Test Case Instructions**
1. **Set up JUnit** in your IDE (e.g., IntelliJ IDEA, Eclipse) and add JUnit dependencies.
2. **Create a test class** for each command.
3. **Write test methods** using:
   - `@Test` annotation for unit tests.
   - Assertions like `assertEquals()`, `assertTrue()`, and `assertFalse()` to verify expected results.

---

## ⚙️ Technical Details
- **Programming Language:** Java
- **Testing Framework:** JUnit
- **Course:** CS341 - Operating Systems 1
- **Institution:** Cairo University, Faculty of Computers & Artificial Intelligence

---

## 📖 How to Use the CLI
1. **Compile the project:**
   ```sh
   javac CLI.java
   ```
2. **Run the CLI:**
   ```sh
   java CLI
   ```
3. **Enter commands** in the terminal and execute them as in a Unix shell.

---

## 👨‍💻 Contributors
- **Malak Sherif** 
- **Jana Abdallah** 
- **Raghad Ahmed** 
- **Afnan Baqais** 

---
### 📚 Developed for the **CS341 - Operating Systems 1** Course at **Cairo University**

