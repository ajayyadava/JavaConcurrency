Play project with code examples to some toy problems
to refresh my knowledge of Java Concurrency API. 

**NOTE**: Some examples contain idioms not good for production code but are 
here just to remind me of oddities / gotchas / things to remember.


### How to Generate Random Numbers in Concurrent Applications

In a concurrent application if different threads generate
random number using ```Random``` class e.g. by doing  ```Random.nextInt(10)```
then it can cause heavy contention on the seed and poor
performance.


### Simulate Producer Consumer Problem

Variations:
  - Producer Consumer using ```synchronized - wait```
  - Using ```BlockedQueue```
  - Using ```Lock```
  

### Simulate ReaderWriter Problem


3 Variations:

1. **Unbiased** - Ensure that the reads and writes are in FIFO order and
fair

2. **Writer Biased** - Once a writer requests, no reads are
entertained.

3. **Reader Biased** - If a reader already has lock, another
reader can continue to read even if a Writer is waiting.



### Semaphore
- Semaphores and concept of lock ownership
- Semaphores like a CountdownLatch
- Controlling access to a pool of resources using Semaphores

### CountDownLatch
- Demonstrate that countdown is just a count and not necessarily number of 
threads which counted down.

### Executors
- CachedThreadPool              (ThreadPoolExecutor)
- FixedSizeThreadPool           (ThreadPoolExecutor)
- SingleThreadExecutor          (Executor)
- ScheduledThreadpoolExecutors  (ThreadPoolExecutor)
    - Running Tasks periodically at a fixed rate
    - Running Task Only once after an initial delay
    - Running tasks periodically with a fixed delay between 2 instances 
- Running tasks which don't return results
- Running tasks which return result
    - Running multiple tasks and processing first result
    - Running multiple tasks and processing all results
- ```FutureTask``` - How to execute another function post the task
  is complete.

### AtomicLong / AtomicInteger
- How to use CAS for better performance than locks

### AtomicArrays
- Using CAS operations on an array of atomic variables for better performance

### LongAdder
- LongAdder as maintaining total
- LongAdder as maintaining counter in stats

In case of maintaining total/counter, LongAdder results in better 
performance than AtomicLong under heavy contention. 

Note: getting result is racy 

### LongAccumulators
In case of maintaining stats, this results in better
performance than AtomicLong under heavy contention.

### Exchanger 
- Play ping pong between two threads using Exchanger

### CyclicBarrier
- Divide and Conquer using CyclicBarrier. 
- Demonstrate that the barrier is automatically reset.
- Demonstrate that CyclicBarrier takes into account the
number of threads and not the number of invocations

### CompletionService
Separating concerns of submitting tasks and processing results


### Phaser
- Sample program to illustrate register(), arriveAndAwaitAdvance(), and arriveAndDeregister() 
- Simulate a test where all students finish exercises together.
Print messages whenever phase changes.
Simulate a late student joining the exam.

### ForkJoinPool 
    1.  - Understanding ForkJoinTask, RecursiveAction, RecursiveTask
        - pool.invoke() vs. pool.execute() vs pool.submit()
    
    2. Divide and Conquer using ForkJoinPool
       - Using ForkJoinTask.invokeAll() [ Synchronous but better reuse of threads] 
       - Grouping results of sub-tasks
    3. Asynchronous methods - ForkJoinTask.fork() and ForkJoinTask.join()

###### TODOs: 



How to handle uncaught exceptions in the run() method of threads?
ThreadGroups
ThreadFactory

### CloseableFuture
### StampedLocks
### TransferQueue

1. Show an example of deadlock.

2. Most textbook says to prevent deadlock, should take 
locks in the same order. Is it really true?
Can you implement a situation where this leads to 
a "deadlock" 

Hint: effect is same but it is called something else (not livelock :)


