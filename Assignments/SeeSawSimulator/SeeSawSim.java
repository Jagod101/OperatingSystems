/*
 *  Name: Zachary Jagoda
 *  Student ID: 2274813
 *  Email: jagod101@mail.chapman.edu
 *  Course: CPSC 380  Section: 01
 *  Project 2: See Saw Simulator
 *
 *  Functions/Methods/Threads/Classes: a) main, b) calcHeight, c) outputSem, d) RunFred, e) RunWilma
 *  a) Creates the Threads, Starts the Threads, and Joins the Threads
 *  b) Acquires and Releases the sem Semaphore. Does all the math for Fred and Wilma's Height
 *  c) Acquires and Releases the oSem Semaphore. Does all the console output for the See Saw Simulation
 *  d) Takes in both sem and oSem, and runs FredSee() through calcHeight and outputSem
 *  e) Takes in both sem and oSem, and runs WilmaSaw() through calcHeight and outputSem
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class SeeSawSim {
  static double fredH = 1.0;
  static double wilmaH = 7.0;
  static boolean fredUp = true;
  static int time = -0;
  static Semaphore sem = new Semaphore(1);
  static Semaphore oSem = new Semaphore(1);

  //Main Function
  public static void main(String args[]) {
    //ArrayList<Thread> threads = new ArrayList<>();

    Thread t1 = new Thread(new RunFred(SeeSawSim.sem, SeeSawSim.oSem));
    Thread t2 = new Thread(new RunWilma(SeeSawSim.sem, SeeSawSim.oSem));

    try {
      t1.start();
      t2.start();

      t1.join();
      t2.join();
    }
    catch(InterruptedException ex) {
      ex.printStackTrace();
    }
/*
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
*/
  }

  //Calculates the Height at each interval of the simulation
  public static void calcHeight(Semaphore sem) {
    try {
      sem.acquire();

      if(wilmaH == 1) {
        fredUp = false;
        System.out.println();
        System.out.println("Wilma is going up!");
      }
      else if(fredH == 1) {
        fredUp = true;
        System.out.println();
        System.out.println("Fred is going up!");
      }

      try {
        if(fredUp) {
          wilmaH = wilmaH - 1;
          fredH = fredH + 1;
        }
        else {
          wilmaH = wilmaH + 1.5;
          fredH = fredH - 1.5;
        }

        time++;
      }
      catch(Exception e) {
        e.printStackTrace();
      }

      sem.release();
    }
    catch(InterruptedException ex) {
      System.out.println("Interrupted Exception");
    }
  }

  //Outputs all updates on each interval to the console
  public static void outputSem(Semaphore oSem) {
    try {
      oSem.acquire();
      if(time == 1) {
        System.out.println("Time: 0 \t|\tFred's Current Height: 1.0 \t|\tWilma's Current Height: 7.0");
        System.out.println("Time: 1 \t|\tFred's Current Height: 2.0 \t|\tWilma's Current Height: 6.0");
      }
      System.out.println("Time: " + time + " \t|\tFred's Current Height: " + fredH+ " \t|\tWilma's Current Height: " + wilmaH);
      oSem.release();
    }
    catch(InterruptedException ex) {
      System.out.println("Interrupted Exception");
    }
  }
}

class RunFred extends Thread implements Runnable {
  Semaphore sem;
  Semaphore oSem;

  public RunFred(Semaphore sem, Semaphore oSem) {
    this.sem = sem;
    this.oSem = oSem;
  }

  public void run() {
    fredSee();
  }

  private void fredSee() {
    try {
      for(int x = -0; x < 50; x++) {
        SeeSawSim.calcHeight(sem);
        SeeSawSim.outputSem(oSem);
        Thread.sleep(1000);
      }
    }
    catch(InterruptedException ex) {
      ex.printStackTrace();
    }
  }
}


class RunWilma extends Thread implements Runnable {
  Semaphore sem;
  Semaphore oSem;

  public RunWilma(Semaphore sem, Semaphore oSem) {
    this.sem = sem;
    this.oSem = oSem;
  }

  public void run() {
    wilmaSaw();
  }

  private void wilmaSaw() {
    try {
      for(int y = -0; y < 50; y++) {
        SeeSawSim.calcHeight(sem);
        SeeSawSim.outputSem(oSem);
        Thread.sleep(1000);
      }
    }
    catch(InterruptedException ex) {
      ex.printStackTrace();
    }
  }
}
