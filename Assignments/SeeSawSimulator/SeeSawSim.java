/*
 *  Name: Zachary Jagoda
 *  Student ID: 2274813
 *  Email: jagod101@mail.chapman.edu
 *  Course: CPSC 380  Section: 01
 *  Project 2: See Saw Simulator

 */

import java.io.*;
import java.util.*;

public class SeeSawSim {
  private static double fredH = 1.0;
  private static double wilmaH = 7.0;

  public static class MutEx {
    public int turn;
    public int howMany;

    public MutEx(int name, int times) {
      turn = name;
      howMany = times;
    }

    public synchronized void turnSwitch() {
      if(turn == 0) {
        turn++;
      }
      else if(turn == 1) {
        turn--;
      }
      notifyAll();
    }

    public synchronized void turnWait(int id) throws InterruptedException {
      while(turn != id) {
        wait();
      }
    }
  }

  static private class RunFred extends Thread implements Runnable {
    private static int MY_ID;
    private static MutEx MUT_EX;

    public RunFred(int id, MutEx MUT_EXTwo) {
      MY_ID = id;
      MUT_EX = MUT_EXTwo;
    }

    public void fredSee() {
      for(int x = 0; x < 10; ++x) {
        try {
          MUT_EX.turnWait(MY_ID);
          System.out.println("GOING UP: Fred");
          while(fredH < 7.0) {
            System.out.println("Fred's Current Height: " + fredH);
            System.out.println("Wilma's Current Height: " + wilmaH);
            fredH++;
            wilmaH--;
          }

          System.out.println("Fred's Current Height: " + fredH);
          System.out.println("Wilma's Current Height: " + wilmaH);
          Thread.sleep(1000);
          MUT_EX.turnSwitch();
        }
        catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public void run() {
      fredSee();
    }
  }

  static private class RunWilma extends Thread implements Runnable {
    private static int MY_ID;
    private static MutEx MUT_EX;

    public RunWilma(int num, MutEx m) {
      MY_ID = num;
      MUT_EX = m;
    }

    static private void wilmaSaw() {
      for(int y = 0; y < 10; ++y) {
        try {
          MUT_EX.turnWait(MY_ID);
          System.out.println("GOING UP: Wilma");
          while(wilmaH < 7.0) {
            System.out.println("Fred's Current Height: " + fredH);
            System.out.println("Wilma's Current Height: " + wilmaH);
            fredH -= 1.5;
            wilmaH += 1.5;
          }

          System.out.println("Fred's Current Height: " + fredH);
          System.out.println("Wilma's Current Height: " + wilmaH);
          Thread.sleep(1000);
          MUT_EX.turnSwitch();
        }
        catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    public void run() {
      wilmaSaw();
    }
  }

   //Main Function
   public static void main(String args[]) {
     MutEx myLock = new MutEx(0, 2);
     ArrayList<Thread> threads = new ArrayList<>();

     Thread t1 = new Thread(new RunFred(0, myLock));
     Thread t2 = new Thread(new RunWilma(1, myLock));

     threads.add(new Thread(t1));
     threads.add(new Thread(t2));

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
   }
}
