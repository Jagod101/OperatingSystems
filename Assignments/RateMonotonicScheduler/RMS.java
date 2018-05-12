/*
 *  Name: Zachary Jagoda
 *  Student ID: 2274813
 *  Email: jagod101@mail.chapman.edu
 *  Course: CPSC 380  Section: 01
 *  Project 3: Rate Monotonic Scheduler
 *
 *  Functions/Classes: a) Scheduler, b) printOut
 *  a) Scheduler starts off Scheduler.java by calling start() and joinAll(), along with trying to join all the threads with join()
 *  b) Scheduler calls printOut from Scheduler.java at the end of the file
 */

public class RMS {
  public static void main(String[] args) {
    System.out.println("DoWork Repititions: " + WorkThread.DOWORK_REPS);

    Scheduler sched = new Scheduler();
    sched.schedulerThread.start();
    sched.joinAll();

    try {
      sched.schedulerThread.join();
    }
    catch(Exception e) {
      System.out.println("FAILED: Scheduler Failed to Join Thread");
      e.printStackTrace();
    }

    sched.printOut();
  }
}
