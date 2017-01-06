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

### AtomicLong / AtomicInteger
- How to use CAS for better performance than locks

### AtomicArrays


### LongAdder
- LongAdder as maintaining total
- LongAdder as maintaining counter in stats

In case of maintaining total/counter, LongAdder results in better 
performance than AtomicLong under heavy contention. 

Note: getting result is racy 


###### TODOs:

### LongAccumulators
In case of maintaining stats, this results in better
performance than AtomicLong under heavy contention.

### CloseableFuture
### StampedLocks

1. Show an example of deadlock.

2. Most textbook says to prevent deadlock, should take 
locks in the same order. Is it really true?
Can you implement a situation where this leads to 
a "deadlock" 

Hint: effect is same but it is called something else (not livelock :)


How to handle uncaught exceptions in the run() method of threads?

ThreadGroups

ThreadFactory


Separating launching of tasks and processing their results

### Phaser
### CyclicBarrier
### Exchanger
### ForkJoinPool
Running tasks asynchronously 

Difference between invoke(), invokeAll(), fork(), join() etc.

FutureTask - How to execute another function post the task
is complete.

TransferQueue
