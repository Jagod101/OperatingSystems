import java.io.*;
import java.util.*;

public class SudokuValidator {

  //Initalize Sudoku Board as a 9x9 Multidimensional Array
  private static int board[][] = new int[9][9];
  //Set Values to -1 to be true zero numbers
  private static int MRowNum = -1;
  private static int MColNum = -1;
  private static int MSubGridNum = -1;
  //Set Msgs to empty strings
  private static String MsgRow = " ";
  private static String MsgCol = " ";
  private static String MsgSubGrid = " ";

  //Read file input and transpose data from .txt file
  private static void readBoard(String fileName) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(fileName));
      String line;
      int row = 0;

      while((line = br.readLine()) != null) {
        String[] vals = line.trim().split(",");

        for(int col = 0; col < 9; col++) {
            board[row][col] = Integer.parseInt(vals[col]);
        }
        row++;
      }
    }
    catch (IOException io) {
      io.printStackTrace();
    }
  printBoard();
  }

  //Print Board
  private static void printBoard() {
    for (int x =0; x < 9; ++x) {
      for (int y =0; y < 9; ++y) {
        System.out.print(board[x][y] + "  ");
      }
      System.out.println();
    }
    System.out.println();
  }

  //Thread that Checks Columns for Errors
  static private class CheckColumns extends Thread {

    private int colError = -1;
    private HashSet<Integer> colSet = new HashSet<>();

    public void run() {
      for (int x = 0; x < 9; ++x) {
        for (int y = 0; y < 9; ++y) {
          //If Error is Found
          if(!colSet.add(board[x][y])) {
            colError = x;
            System.out.println("COLUMN THREAD\t\tFound Error @ Column " + (x + 1));
            Integer[] tempArray = colSet.toArray(new Integer[colSet.size()]);
            Arrays.sort(tempArray);

            //Iterate through and find duplicate, find correct/suggest new value to replace the duplicate
            for(int z = 1; z < 10; z++)
            {
              if(Arrays.asList(tempArray).contains(z) == false)
              {
                MColNum += z + 1;
                System.out.println("\nCOLUMN ERROR: Column " + (x + 1) + "\tDuplicate " + board[x][y] + "\tReplace the Duplicate " + board[x][y] + " with a " + MColNum);
                MColNum -= 2;
                break;
              }
            }
          }
          if (y == 8) {
            colSet.clear();
          }
        }
      }
    }
    private int getColError() {
      return colError;
    }
  }

  //Thread that Checks Rows for Errors
  static private class CheckRows extends Thread {

    private int rowError = -1;
    private HashSet<Integer> rowSet = new HashSet<>();

    public void run() {
      for (int x = 0; x < 9; ++x) {
        for (int y = 0; y < 9; ++y) {
          //If Error is Found
          if(!rowSet.add(board[x][y])) {
            rowError = x;
            System.out.println("ROW THREAD\t\tFound Error @ Row " + (x + 1));
            Integer[] tempArray = rowSet.toArray(new Integer[rowSet.size()]);
            Arrays.sort(tempArray);

            //Iterate through and find duplicate, find correct/suggest new value to replace the duplicate
            for(int z = 1; z < 10; z++)
            {
              if(Arrays.asList(tempArray).contains(z) == false)
              {
                MRowNum += z + 1;
                System.out.println("\nROW ERROR: Row " + (x + 1) + "\tDuplicate " + board[x][y] + "\tReplace the duplicate " + board[x][y] + " with a " + MRowNum);
                MRowNum -= 2;
                break;
              }
            }
          }
          if (y == 8) {
            rowSet.clear();
          }
        }
      }
    }
    private int getRowError() {
      return rowError;
    }
  }

  //Function to check each of the 9 Sub Grids for possible Errors
  private static void checkSubGrid(int rowStart, int rowStop, int columnStart, int columnStop, int subGridNum) {

    HashSet<Integer> checkBoard = new HashSet<>();

    for(int x = rowStart; x < rowStop; ++x) {
      for(int y = columnStart; y < columnStop; ++y) {
        //If Error is Found
        if(!checkBoard.add(board[x][y])) {
          System.out.println("SUBGRID " + subGridNum + "\t\tFound Error @ Row " + (x + 1) + ", Column " + (y + 1));
          Integer[] tempArray = checkBoard.toArray(new Integer[checkBoard.size()]);

          //Iterate through and find duplicate, find correct/suggest new value to replace the duplicate
          for(int z = 1; z < 10; z++) {
            if(Arrays.asList(tempArray).contains(z) == false) {
              MSubGridNum += z + 1;
              System.out.println("\nSG ERROR: SubGrid " + subGridNum + "\tDuplicate " + board[x][y] + "\tReplace the Duplicate " + board[x][y] + " with a " + MSubGridNum);
              MSubGridNum -= 2;
              break;
            }
          }
          x = rowStop;
          y = columnStop;
          checkBoard.clear();
        }
      }
    }
  }

  //Thread that separates each Sub Grid respectivley, calls function checkSubGrid
  static private class SubGridCheck extends Thread {
    public void run() {
      checkSubGrid(0,3,0,3,1);
      checkSubGrid(0,3,3,6,2);
      checkSubGrid(0,3,6,9,3);
      checkSubGrid(3,6,0,3,4);
      checkSubGrid(3,6,3,6,5);
      checkSubGrid(3,6,6,9,6);
      checkSubGrid(6,9,0,3,7);
      checkSubGrid(6,9,3,6,8);
      checkSubGrid(6,9,6,9,9);
    }
  }

  //Main Thread/Function
  public static void main(String args[]) {
    //User Provides File Input
    System.out.print("INPUT File Name: ");
    //Set the Scanner
    Scanner sc = new Scanner(System.in);
    String fileName = sc.next();

    System.out.println();
    //Calls Function readBoard that transposes data from file
    readBoard(fileName);

    //Creates an ArrayList for Threads being used in program
    ArrayList<Thread> threads = new ArrayList<>();

    //Three Threads, 1) Column, 2) Row, and 3) SubGrid will be ran
    CheckColumns columnThread = new CheckColumns();
    CheckRows rowThread = new CheckRows();
    SubGridCheck subGridThread = new SubGridCheck();

    threads.add(new Thread(columnThread));
    threads.add(new Thread(rowThread));
    threads.add(new Thread(subGridThread));

    //Try to Start all the Threads from Main
    try {
      for(Thread t : threads) {
        t.start();
      }
    }
    catch(Exception e) {
      System.out.println("Unable To Start Threads");
    }

    //Try to Join all the Threads back in Main
    try {
      for(Thread t : threads) {
        t.join();
      }
    }
    catch(InterruptedException e) {
      e.printStackTrace();
    }

    //If Errors are returned from Column
    int cError = columnThread.getColError();

    //If Errors are returned from Row
    int rError = rowThread.getRowError();

    //If neither Column or Row return Errors
    if(cError == -1 && rError == -1) {
      System.out.println("Sudoku Board Contains Zero Errors");
    }
    else {
      if(MsgRow != " ")
      {
        System.out.println(MsgRow);
      }
      if(MsgCol != " ")
      {
        System.out.println(MsgCol);
      }
      if(MsgSubGrid != " ")
      {
        System.out.println(MsgSubGrid);
      }
    }
  }
}
