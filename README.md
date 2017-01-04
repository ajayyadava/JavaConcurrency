Play project with code examples to some toy problems
to refresh my knowledge of Java Concurrency API


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



### CountDownLatch
### Phaser
### Semaphore
### CyclicBarrier
### Exchanger
### ForkJoinPool
### Futures 
### Callables
### CloseableFuture






###### TODOs:

1. Show an example of deadlock.

2. Most textbook says to prevent deadlock, should take 
locks in the same order. Is it really true?
Can you implement a situation where this leads to 
a "deadlock" 

Hint: effect is same but it is called something else (not livelock :)



