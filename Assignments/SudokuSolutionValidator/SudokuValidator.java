import java.io.*;
import java.util.*;

public class SudokuValidator {

  private static int board[][] = new int[9][9];
  private static int MRowNum = -1;
  private static int MColNum = -1;
  private static int MSubGridNum = -1;
  private static int cError;
  private static String MsgRow = " ";
  private static String MsgCol = " ";
  private static String MsgSubGrid = " ";

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
  printGrid();
  }

  private static void printGrid() {
    int SGCCounter = 0;
    int SGRCounter = 0;

    for (int x =0; x < 9; ++x) {
      for (int y =0; y < 9; ++y) {
        System.out.print(board[x][y] + "  ");
      }
      System.out.println();
    }
    System.out.println();
  }

  static private class CheckColumns extends Thread {

    private int colError = -1;
    private HashSet<Integer> colSet = new HashSet<>();

    public void run() {
      for (int x = 0; x < 9; ++x) {
        for (int y = 0; y < 9; ++y) {
          if(!colSet.add(board[x][y])) {
            colError = x;
            System.out.println("COLUMN THREAD\t\tFound Error @ Column " + (x + 1));
            Integer[] tempArray = colSet.toArray(new Integer[colSet.size()]);
            Arrays.sort(tempArray);
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

  static private class CheckRows extends Thread {

    private int rowError = -1;
    private HashSet<Integer> rowSet = new HashSet<>();

    public void run() {
      for (int x = 0; x < 9; ++x) {
        for (int y = 0; y < 9; ++y) {
          if(!rowSet.add(board[x][y])) {
            rowError = x;
            System.out.println("ROW THREAD\t\tFound Error @ Row " + (x + 1));
            Integer[] tempArray = rowSet.toArray(new Integer[rowSet.size()]);
            Arrays.sort(tempArray);
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

  private static void checkSubGrid(int rowStart, int rowStop, int columnStart, int columnStop, int subGridNum) {

    HashSet<Integer> checkGrid = new HashSet<>();

    for(int x = rowStart; x < rowStop; ++x) {
      for(int y = columnStart; y < columnStop; ++y) {
        if(!checkGrid.add(board[x][y])) {
          System.out.println("SUBGRID " + subGridNum + "\t\tFound Error @ Row " + (x + 1) + ", Column " + (y + 1));
          Integer[] tempArray = checkGrid.toArray(new Integer[checkGrid.size()]);

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
          checkGrid.clear();
        }
      }
    }
  }

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

  public static void main(String args[]) {
    System.out.print("INPUT File Name: ");
    //Set the Scanner
    Scanner sc = new Scanner(System.in);
    String fileName = sc.next();

    System.out.println();
    readBoard(fileName);

    ArrayList<Thread> threads = new ArrayList<>();

    CheckColumns columnThread = new CheckColumns();
    CheckRows rowThread = new CheckRows();
    SubGridCheck subGridThread = new SubGridCheck();

    threads.add(new Thread(columnThread));
    threads.add(new Thread(rowThread));
    threads.add(new Thread(subGridThread));

    for(Thread t : threads) {
      t.start();
    }

    try {
      for(Thread t : threads) {
        t.join();
      }
    }
    catch(InterruptedException e) {
      e.printStackTrace();
    }

    int cError = columnThread.getColError();

    int rError = rowThread.getRowError();

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
