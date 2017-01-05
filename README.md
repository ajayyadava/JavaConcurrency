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


### CountDownLatch
- Demonstrate that countdown is just a count and not necessarily number of 
threads which counted down.


### Phaser

### CyclicBarrier
### Exchanger
### ForkJoinPool
### Executors
- CachedThreadPool              (ThreadPoolExecutor)
- FixedSizeThreadPool           (ThreadPoolExecutor)
- SingleThreadExecutor          (Executor)
- ScheduledThreadpoolExecutors  (ThreadPoolExecutor)
    - Running Tasks Periodically
    - Running Tasks after a delay
 


- Running tasks which don't return results
- Running tasks which return result
    - Running multiple tasks and processing first result
    - Running multiple tasks and processing all results


### CloseableFuture
### StampedLocks
### AtomicLong / AtomicInteger
### AtomicLongAccumulators





###### TODOs:

1. Show an example of deadlock.

2. Most textbook says to prevent deadlock, should take 
locks in the same order. Is it really true?
Can you implement a situation where this leads to 
a "deadlock" 

Hint: effect is same but it is called something else (not livelock :)



How to handle uncaught exceptions in the run() method of threads?

ThreadGroups
ThreadFactory
Semaphore - Controlling a pool of resources

AtomicInteger
AtomicLong
AtomicArrays


Separating launching of tasks and processing their results

ForkJoin - Running tasks synchronously
Running tasks asynchronously 
Difference between invoke(), invokeAll(), fork(), join() etc.

TransferQueue
