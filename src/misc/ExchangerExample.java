package misc;

import java.util.concurrent.Exchanger;

public class ExchangerExample {

    private static final class Task implements Runnable {
        private final String name;
        private final Exchanger<String> exchanger;
        private String message;

        Task(String name, String message, Exchanger exchanger) {
            this.message = message;
            this.name = name;
            this.exchanger = exchanger;
        }
        @Override
        public void run() {
            for (int i = 0; i < 3; i++) {
                System.out.println(name + " : " + message);
                try {
                    message = exchanger.exchange(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(new Task("thread1", "ping", exchanger)).start();
        new Thread(new Task("thread2", "pong", exchanger)).start();

    }
}
