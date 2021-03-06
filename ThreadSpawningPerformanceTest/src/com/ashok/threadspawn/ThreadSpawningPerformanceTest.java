package com.ashok.threadspawn;

/**
 * @author Ashok Goli
 * The Class ThreadSpawningPerformanceTest.
 */
public class ThreadSpawningPerformanceTest {
  
  /**
   * Test the threadcount & workAmountPerThread.
   *
   * @param threadCount the thread count
   * @param workAmountPerThread the work amount per thread
   * @return the long
   * @throws InterruptedException the interrupted exception
   */
  static long test(final int threadCount, final int workAmountPerThread)
      throws InterruptedException {
    Thread[] tt = new Thread[threadCount];
    final int[] aa = new int[tt.length];
    System.out.print("Creating " + tt.length + " Thread objects... ");
    long t0 = System.nanoTime(), t00 = t0;
    for (int i = 0; i < tt.length; i++) {
      final int j = i;
      tt[i] = new Thread() {
        public void run() {
          int k = j;
          for (int l = 0; l < workAmountPerThread; l++) {
            k += k * k + l;
          }
          aa[j] = k;
        }
      };
    }
    System.out.println(" Done in " + (System.nanoTime() - t0) * 1E-6 + " ms.");
    System.out.print("Starting " + tt.length + " threads with " + workAmountPerThread
        + " steps of work per thread... ");
    t0 = System.nanoTime();
    for (int i = 0; i < tt.length; i++) {
      tt[i].start();
    }
    System.out.println(" Done in " + (System.nanoTime() - t0) * 1E-6 + " ms.");
    System.out.print("Joining " + tt.length + " threads... ");
    t0 = System.nanoTime();
    for (int i = 0; i < tt.length; i++) {
      tt[i].join();
    }
    System.out.println(" Done in " + (System.nanoTime() - t0) * 1E-6 + " ms.");
    long totalTime = System.nanoTime() - t00;
    int checkSum = 0; // display checksum in order to give the JVM no chance to optimize out the
                      // contents of the run() method and possibly even thread creation
    for (int a : aa) {
      checkSum += a;
    }
    System.out.println("Checksum: " + checkSum);
    System.out.println("Total time: " + totalTime * 1E-6 + " ms");
    System.out.println();
    return totalTime;
  }

  /**
   * The main method. 
   * Pass no.of thread sets separated by spaces as arguments. If no arguments are passed, then the default set of 1, 2, 10, 100, 1000, 10000, 100000 will be used 
   * Creates threads in sets of 1, 2, 10, 100, 1000, 10000, 100000 and calculates the time required for spawning them.
   *
   * @param kr the arguments
   * @throws InterruptedException the interrupted exception
   */
  public static void main(String[] kr) throws InterruptedException {    
    int workAmount = 100000000;
    int[] threadCount;
    if(kr!=null && kr.length>0){      
      threadCount = new int[kr.length];
      try {
        for(int i=0;i<kr.length;i++){
          threadCount[i] = Integer.parseInt(kr[i]);
        }
      } catch (Exception e) {
        threadCount = new int[] {1, 2, 10, 100, 1000, 10000, 100000};
      }
    }
    else{
      threadCount = new int[] {1, 2, 10, 100, 1000, 10000, 100000};
    }
    int trialCount = 2;
    long[][] time = new long[threadCount.length][trialCount];
    for (int j = 0; j < trialCount; j++) {
      for (int i = 0; i < threadCount.length; i++) {
        time[i][j] = test(threadCount[i], workAmount / threadCount[i]);
      }
    }
    System.out.print("Number of threads ");
    for (long t : threadCount) {
      System.out.print("\t" + t);
    }
    System.out.println();
    for (int j = 0; j < trialCount; j++) {
      System.out.print((j + 1) + ". trial time (ms)");
      for (int i = 0; i < threadCount.length; i++) {
        System.out.print("\t" + Math.round(time[i][j] * 1E-6));
      }
      System.out.println();
    }
  }
}
