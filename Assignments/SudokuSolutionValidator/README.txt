Name: Zachary Jagoda
Student ID: 2274813
Email: jagod101@mail.chapman.edu
Course: CPSC 380-01

Project 1: Sudoku Validator

Description:
The Sudoku Validator Program (SVP) is a program that aims to check a regular 9x9 Sudoku Board for Errors and suggest fixes if one or more are encountered.
The program does this through three Threads, not including the Main Thread (so four in total). The Three Threads that are created work to check the Columns, Rows, and 3x3 Sub Grids on the Board.
Each Thread recursively checks the board using nested for loops and if statements, allowing the minimum of three threads to be used for the project.

SudokuValidator$1.class
SudokuValidator$CheckColumns.class
SudokuValidator$CheckRows.class
SudokuValidator$CheckSubGrids.class
SudokuValidator$SubGridCheck.class

Runtime Errors:
No current known runtime errors

References/Resources:
- https://stackoverflow.com/
  - Used for Hash Maps/Sets, Thread Syntax for Java, Creating Executable
- Viseth Sean (Discussion during TA Hours)
- Professor Hansen (Discussion during Office Hours)

Instructions:
- Use cd to navigate to .jar file
- Use 'java -jar SudokuValidator.jar' to start program

To Compile and Run (as non .exe):
- 'javac SudokuValidator.java'
- 'java SudokuValidator'
