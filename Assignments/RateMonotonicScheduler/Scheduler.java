/*
 *  Name: Zachary Jagoda
 *  Student ID: 2274813
 *  Email: jagod101@mail.chapman.edu
 *  Course: CPSC 380  Section: 01
 *  Project 3: Rate Monotonic Scheduler
 *
 *  Functions/Classes: a) Scheduler, b) joinAll, c) schedule, d) killThreads, e) printOut
 *  a) Scheduler creates, starts, and calls to WorkThread
 *  b) joinAll attempts to join each Thread
 *  c) schedule checks each thread based off of time and period
 *  d) killThreads force quits the threads if they try and run longer than the provided time
 *  e) printOut displays all the info for DoWork, thread1, Thread2, Thread3, and Thread4 to the console
 */

import java.util.concurrent.Semaphore;

public class Scheduler {
  public int overrunCount_Thread1;
  public int overrunCount_Thread2;
  public int overrunCount_Thread3;
  public int overrunCount_Thread4;

  public int time;

  WorkThread RMS_t1;
  WorkThread RMS_t2;
  WorkThread RMS_t3;
  WorkThread RMS_t4;

  Thread schedulerThread;

  public Semaphore sem = new Semaphore(0);

  public Scheduler() {
    RMS_t1 = new WorkThread(1, 2);
    RMS_t2 = new WorkThread(2, 3);
    RMS_t3 = new WorkThread(4, 4);
    RMS_t4 = new WorkThread(16, 5);
    time = 0;

    RMS_t1.createThread();
    RMS_t2.createThread();
    RMS_t3.createThread();
    RMS_t4.createThread();

    RMS_t1.thread.start();
    RMS_t2.thread.start();
    RMS_t3.thread.start();
    RMS_t4.thread.start();

    this.schedulerThread = new Thread(new Runnable() {
      @Override
      public void run()
      {
         schedule();
         killThreads();
      }
    });

    this.schedulerThread.setPriority(Thread.MAX_PRIORITY);
  }

  public void joinAll() {
    try {
      RMS_t1.thread.join();
      RMS_t2.thread.join();
      RMS_t3.thread.join();
      RMS_t4.thread.join();
    }
    catch(Exception e) {
      e.printStackTrace();
      System.out.println("FAILED: Couldn't Join Threads");
    }
  }

  public void schedule() {
    RMS_t1.sem.release();
    RMS_t2.sem.release();
    RMS_t3.sem.release();
    RMS_t4.sem.release();

    for(int i = 0; i < 160; ++i) {
      try {
          Thread.sleep(10);
      }
      catch(Exception e) {
        System.out.println("FAILED: Scheduler Failed to Acquire Semaphore");
      }

      time++;

      if(!RMS_t1.isDone()) {
        overrunCount_Thread1++;
      }

      if(time > 0) {
        RMS_t1.sem.release();
      }

      if(time % 2 == 0) {
        if(!RMS_t2.isDone()) {
          overrunCount_Thread2++;
        }

        RMS_t2.sem.release();
      }

      if(time % 4 == 0) {
        if(!RMS_t3.isDone()) {
          overrunCount_Thread3++;
        }

        RMS_t3.sem.release();
      }

      if(time % 16 == 0) {
        if(!RMS_t4.isDone()) {
          overrunCount_Thread4++;
        }

        RMS_t4.sem.release();
      }
    }
  }

  public void killThreads() {
    //When Time is Up, Force Quit Threads
    RMS_t1.schedForceQuit();
    RMS_t2.schedForceQuit();
    RMS_t3.schedForceQuit();
    RMS_t4.schedForceQuit();
  }

  public void printOut() {
    System.out.println("RESULTS");
    System.out.println("RMS Thread1 completions: " + RMS_t1.numCompletions);
    System.out.println("RMS Thread1 overruns: \t" + overrunCount_Thread1);
    System.out.println();
    System.out.println("RMS Thread2 completions: " + RMS_t2.numCompletions);
    System.out.println("RMS Thread2 overruns: \t" + overrunCount_Thread2);
    System.out.println();
    System.out.println("RMS Thread3 completions: " + RMS_t3.numCompletions);
    System.out.println("RMS Thread3 overruns: \t" + overrunCount_Thread3);
    System.out.println();
    System.out.println("RMS Thread4 completions: " + RMS_t4.numCompletions);
    System.out.println("RMS Thread4 overruns: \t" + overrunCount_Thread4);
  }

}
