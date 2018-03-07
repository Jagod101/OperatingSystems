import java.io.*;
import java.util.*;

public class SudokuValidator {

  private static int board[][] = new int[9][9];
  private static int missingRowNumber = -1;
  private static int missingColNumber = -1;
  private static int missingSubGridNumber = -1;
  private static int errorCount;
  private static String rowMessage = " ";
  private static String columnMessage = " ";
  private static String subGridMessage = " ";

  private static void readInGrid(String fileName) {

      try {
          BufferedReader br = new BufferedReader(new FileReader(fileName));
          String line;
          int row = 0;

          while((line = br.readLine()) != null) {
              String[] vals = line.trim().split(",");

              for (int col = 0; col < 9; col++) {
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
      for (int x =0; x < 9; ++x) {
          for (int y =0; y < 9; ++y) {
              System.out.print(board[x][y] + " ");
          }
          System.out.println();
      }
  }

  static private class CheckColumns extends Thread {

      private int columnError = -1;
      private HashSet<Integer> columnSet = new HashSet<>();

      public void run() {
          for (int x = 0; x < 9; ++x) {
              for (int y = 0; y < 9; ++y) {

                  if(!columnSet.add(board[x][y])) {
                      columnError = x;
                      System.out.println("COLUMN THREAD:\tFound Error @ Column " + (x + 1));
                      Integer[] tempArray = columnSet.toArray(new Integer[columnSet.size()]);
                      Arrays.sort(tempArray);
                      for(int z = 1; z < 10; z++)
                      {
                        if(Arrays.asList(tempArray).contains(z) == false)
                        {
                          missingColNumber += z;
                          System.out.println("COLUMN ERROR: Column " + (x + 1) + "\tDuplicate " + board[x][y] + " in Column."+ "\tReplace the Duplicate " + board[x][y] + " with a " + missingColNumber + "\n");
                          break;
                        }
                      }
                  }

                  if (y == 8) {

                      columnSet.clear();

                  }
              }
          }
      }
      private int getColumnError() {
          return columnError;
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
                      System.out.println("ROW THREAD:\tFound Error @ Row " + (x + 1));
                      Integer[] tempArray = rowSet.toArray(new Integer[rowSet.size()]);
                      Arrays.sort(tempArray);
                      for(int z = 1; z < 10; z++)
                      {
                        if(Arrays.asList(tempArray).contains(z) == false)
                        {
                          missingRowNumber += z;
                          System.out.println("ROW ERROR: Row " + (x + 1) + "\tDuplicate " + board[x][y] + " in Row" + "\tReplace the duplicate " + board[x][y] + " with a " + missingRowNumber + "\n");
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

  private static void subGridCheck(int rowStart, int rowStop, int columnStart, int columnStop, int subGridNum) {

      HashSet<Integer> checkSet = new HashSet<>();

      for (int x = rowStart; x < rowStop; ++ x) {
          for (int y = columnStart; y < columnStop; ++y) {

              if (!checkSet.add(board[x][y])) {
                  System.out.println("SUBGRID " + subGridNum + "\tERROR at Row: " + (x + 1) + ", Column: " + (y + 1));
                  Integer[] tempArray = checkSet.toArray(new Integer[checkSet.size()]);

                  for(int z = 1; z < 10; z++) {
                    if(Arrays.asList(tempArray).contains(z) == false) {
                      missingSubGridNumber = z;
                      System.out.println("SUBGRID ERROR: " + subGridNum + "\tDuplicate " + board[x][y] + " in Sub Grid"+ "\tReplace the Duplicate " + board[x][y] + " with a " + missingSubGridNumber + "\n");
                      break;
                    }
                  }
                  x = rowStop;
                  y = columnStop;
                  checkSet.clear();
              }
          }
      }
  }

  static private class CheckSubGrids extends Thread {

      public void run() {
          subGridCheck(0,3,0,3,1);
          subGridCheck(0,3,3,6,2);
          subGridCheck(0,3,6,9,3);
          subGridCheck(3,6,0,3,4);
          subGridCheck(3,6,3,6,5);
          subGridCheck(3,6,6,9,6);
          subGridCheck(6,9,0,3,7);
          subGridCheck(6,9,3,6,8);
          subGridCheck(6,9,6,9,9);
      }
  }

      public static void main(String args[]) {
          System.out.print("INPUT File Name: ");
          //Set the Scanner
          Scanner sc = new Scanner(System.in);
          String fileName = sc.next();

          readInGrid(fileName);

          ArrayList<Thread> threads = new ArrayList<>();

          CheckColumns columnThread = new CheckColumns();
          CheckRows rowThread = new CheckRows();
          CheckSubGrids subGridThread = new CheckSubGrids();

          threads.add(new Thread(columnThread));
          threads.add(new Thread(rowThread));
          threads.add(new Thread(subGridThread));

          for (Thread t : threads) {
              t.start();
          }

          try {
              for (Thread t : threads) {
                  t.join();
              }
          }
          catch (InterruptedException e) {
              e.printStackTrace();
          }

          int cError = columnThread.getColumnError();

          int rError = rowThread.getRowError();

          if(cError == -1 && rError == -1) {
              System.out.println("There were no errors in the sudoku grid.");
          }
          else {
              if(rowMessage != " ")
              {
                System.out.println(rowMessage);
              }
              if(columnMessage != " ")
              {
                System.out.println(columnMessage);
              }
              if(subGridMessage != " ")
              {
                System.out.println(subGridMessage);
              }
          }
      }
}
