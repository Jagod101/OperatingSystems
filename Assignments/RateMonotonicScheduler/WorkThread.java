/*
 *  Name: Zachary Jagoda
 *  Student ID: 2274813
 *  Email: jagod101@mail.chapman.edu
 *  Course: CPSC 380  Section: 01
 *  Project 3: Rate Monotonic Scheduler
 *
 *  Function/Classes: a) WorkThread, b) isDone, c) doWork, d) createThread, e) schedForcedQuit
 *  a) WorkThread sets up the doWork Matrix
 *  b) isDone sets the Thread to done
 *  c) doWork sets a temp matrix with the column list to the doWork matrix
 *  d) createThread calls doWork and counts the numCompletions, releases the Semaphore
 *  e) schedForcedQuit sets it to true if necessary
 */

import java.util.concurrent.Semaphore;

public class WorkThread {
  public static final int DOWORK_REPS = 10000000;

  public int runCount;
  public boolean done = false;
  public int[][] doWorkMatrix = new int[10][10];
  public Semaphore sem = new Semaphore(0);
  public int numCompletions;
  public int priority;
  private int[] colList = {1,3,5,7,9,0,2,4,6,8};
  private boolean schedForcedQuit = false;

  Thread thread;

  public WorkThread(int rc, int p) {
    this.numCompletions = 0;
    this.runCount = rc;
    this.priority = Thread.MAX_PRIORITY - p;

    for(int i = 0; i < 10; ++i) {
      for(int j = 0; j < 10; ++j) {
        doWorkMatrix[i][j] = 1;
      }
    }

    this.schedForcedQuit = false;
  }

  public boolean isDone() {
    return this.done;
  }

  public void doWork() {
    int temp = 0;

    for(int rep = 0; rep < DOWORK_REPS; ++rep) {
      for(int k = 0; k < 10; ++k) {
        temp = colList[k];

        for(int j = 0; j < 10; ++j) {
          doWorkMatrix[j][temp] = doWorkMatrix[k][j];
        }
      }
    }
  }

  public void createThread() {
    this.thread = new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          while(!Thread.currentThread().isInterrupted()) {
            while(!schedForcedQuit) {
              try {
                sem.acquire();

                done = false;

                for(int i = 0; i < runCount; ++i) {
                  doWork();
                  numCompletions++;
                }

                done = true;
              }
              catch(Exception e) {
                e.printStackTrace();
              }
            }
            if(schedForcedQuit) {
              break;
            }
          }
        }
        catch(Exception e) {
          System.out.println("HARD INTERRUPTED");
       }
      }
    });

    this.thread.setPriority(this.priority);
  }

  public void schedForceQuit() {
    this.schedForcedQuit = true;
  }
}
