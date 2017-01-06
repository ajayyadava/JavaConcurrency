package misc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicVariables {

    private static final class Account {

        private final AtomicInteger balance;

        Account(int initialBalance) {
            this.balance = new AtomicInteger(initialBalance);
        }

        void deposit(int sum) {
            balance.addAndGet(sum);
        }

        void withdraw(int sum) {
            balance.addAndGet(-sum);
        }

        AtomicInteger getBalance() {
            return balance;
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
