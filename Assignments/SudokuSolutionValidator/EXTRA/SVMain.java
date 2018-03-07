import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SVMain {
  public static void main(String[] args) {
    String line;
    int[][] SBoard = new int[9][9];

    public static int RRow;
    public static int RColumn;

    public static int CRow;
    public static int CColumn;

    List<Integer> subGrid1 = new ArrayList<Integer>();
    List<Integer> subGrid2 = new ArrayList<Integer>();
    List<Integer> subGrid3 = new ArrayList<Integer>();
    List<Integer> subGrid4 = new ArrayList<Integer>();
    List<Integer> subGrid5 = new ArrayList<Integer>();
    List<Integer> subGrid6 = new ArrayList<Integer>();
    List<Integer> subGrid7 = new ArrayList<Integer>();
    List<Integer> subGrid8 = new ArrayList<Integer>();
    List<Integer> subGrid9 = new ArrayList<Integer>();

    try {
      System.out.print("INPUT File Name: ");
      //Set the Scanner
      Scanner sc = new Scanner(System.in);
      String fileName = sc.next();
      //Set the FileReader
      FileReader fr = new FileReader(fileName);
      //Set BufferedReader
      BufferedReader br = new BufferedReader(fr);

      int x = 0;
      while((line = br.readLine()) != null) {
        String lineRow[] = line.split(",");
        for(int y = 0; y < 9; y++) {
          SBoard[x][y] = Integer.parseInt(lineRow[y]);
        }
        x++;
      }

      //Print out Sudoku Board Input
      //USE THE BOARD TO ADD UP THE SUBGRID ARRAYS
      System.out.println();
      int SGCCounter = 0;
      int SGRCounter = 0;

      System.out.print("-----------------------");
      System.out.println();

      for(int a = 0; a < 9; a++) {
        for(int b = 0; b < 9; b++) {
          if(SGCCounter < 2) {
            System.out.print(SBoard[a][b] + " ");
            SGCCounter++;
          }
          else {
            System.out.print(SBoard[a][b] + " | ");
            SGCCounter = 0;
          }
        }
        System.out.println();
        SGRCounter++;
        if(SGRCounter > 2) {
          System.out.print("-----------------------");
          System.out.println();
          SGRCounter = 0;
        }
      }
      System.out.println();

      //Row Checker Thread Start
      RowChecker RC = new RowChecker(SBoard);
      Thread RCThread = new Thread(RC);
      RCThread.start();

      //Column Checker Thread Start
      ColumnChecker CC = new ColumnChecker(SBoard);
      Thread CCThread = new Thread(CC);
      CCThread.start();
/*
      //SubGrid Checker Thread Start
      SubGridChecker SGC = new SubGridChecker(SBoard);
      Thread SGCThread = new Thread(SGC);
      SGCThread.start();
*/


    }
    catch(Exception e) {
      System.out.println("File Does Not Exist");
    }
  }
}

class RowChecker implements Runnable {
  public int[][] boardCheck;
  public RowChecker(int[][] boardCheck) {
    this.boardCheck = boardCheck;
  }

  public void run() {
    rowCheck(boardCheck);
  }

  public void rowCheck(int[][] SBoard) {
    int[] row = new int[9];
    for(int y = 0; y < 9; y++) {
      for(int x = 0; x < 9; x++) {
        row[x] = SBoard[x][y];
      }
      for(int w = 0; w < 9; w++) {
        for(int z = w + 1; z < 9; z++) {
          if(row[w] == row[z]) {
            System.out.println("ROW ERROR: Row " + (y + 1) + ", Column " + (z + 1));
            SVMain.RRow = y + 1;
          }
        }
      }
    }
  }
}

class ColumnChecker implements Runnable {
  public int[][] boardCheck;
  public ColumnChecker(int[][] boardCheck) {
    this.boardCheck = boardCheck;
  }

  public void run() {
    columnCheck(boardCheck);
  }

  public void columnCheck(int[][] SBoard) {
    int[] column = new int[9];
    for(int y = 0; y < 9; y++) {
      for(int x = 0; x < 9; x++) {
        column[x] = SBoard[x][y];
      }
      for(int w = 0; w < 9; w++) {
        for(int z = w + 1; z < 9; z++) {
          if(column[w] == column[z]) {
            System.out.println("COLUMN ERROR: Row " + (z + 1) + ", Column " + (y + 1));


          }
        }
      }
    }
  }
}
/*
class SubGridChecker implements Runnable {

}
*/
