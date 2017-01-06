package misc;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**

 When AtomicLong is used for maintaining a counter instead of as a synchronizing construct(CAS)
 then using LongAdder is much better;

 NOTE: retrieving value from LongAdder / LongAccumulator is not accurate if there are
 still threads updating the value, so it must be used only when all threads updating the value have stopped.
 */
public class LongAdderExample {
    private static final class Account {

        private final LongAdder balance;

        Account(int initialBalance) {
            this.balance = new LongAdder();
            this.balance.add(initialBalance);
        }

        void deposit(int sum) {
            balance.add(sum);
        }

        void withdraw(int sum) {
            balance.add(-sum);
        }

        long getBalance() {
            return balance.longValue();
        }
    }

    private static final class Task1 implements Runnable {
        private final Account account;

        Task1(Account account) {
            this.account = account;
        }
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                account.deposit(i);
            }
        }
    }

    private static final class Task2 implements Runnable {
        private final Account account;

        Task2(Account account) {
            this.account = account;
        }
        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                account.withdraw(i);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Account account = new Account(10);
        new Thread(new Task1(account)).start();
        new Thread(new Task2(account)).start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Final Balance in the account: " + account.getBalance());
    }

}
