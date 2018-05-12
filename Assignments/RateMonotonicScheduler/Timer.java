/*
 *  Name: Zachary Jagoda
 *  Student ID: 2274813
 *  Email: jagod101@mail.chapman.edu
 *  Course: CPSC 380  Section: 01
 *  Project 3: Rate Monotonic Scheduler
 *
 *  Functions/Classes: a) Timer, b) getTime, c) run
 *  a) Timer is an empty function based off the class
 *  b) getTime returns the system time
 *  c) run takes in the startTime and sets it to the systemTime, and then releases it for the Semaphore
 */

import java.util.concurrent.Semaphore;

public class Timer {
  public Timer() {

  }

  public double getTime() {
    return(System.nanoTime() / 1000000);
  }

  public void run(Semaphore s) {
    double startTime = System.nanoTime();

    while((System.nanoTime() / 1000000) < (startTime/1000000 + 10*16*100)) {
      if(((System.nanoTime() - startTime)/1000000)  % 100 == 0) {
        s.release();
      }
    }
  }
}
