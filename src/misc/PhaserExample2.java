package misc;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**


 If you want to control tasks according to phase number and not just by calling
 arriveAndAwaitAdvance() then you can register

 Simulate an exam of 3 hours.
 At the start all students get first exercise
 Once all present students finish first exercise, they start second exercise.
 Once all present students finish second exercise, they start third exercise.
and so on..

 Implement a late student and show that after he arrives all other students wait for him.

 Notice the behavior of last student. He joins after everyone finishes first exercise.
 Then every time he calls, arriveAndAwaitAdvance() all students together wait and finish one exercise.
 e.g. LateStudent finishes first when all others finish 2nd.
 LateStudent finishes second when all others finish 3rd.

 For the last exercise the phaser has already entered the terminal stage, so the late student doesn't wait
 and immediately finishes it.
 */
public class PhaserExample2 {

    private static final class MyPhaser extends Phaser {
        MyPhaser(int parties) {
            super(parties);
        }

        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            switch (phase) {
            case 0:
                System.out.println("Phase0: parties: " + registeredParties);
                return finishExercise(1);
            case 1:
                System.out.println("Phase1: parties: " + registeredParties);
                return finishExercise(2);
            case 2:
                System.out.println("Phase2: parties: " + registeredParties);
                return finishExercise(3);
            case 3:
                System.out.println("Phase3: parties: " + registeredParties);
                return finishExercise(4);
            default:
                return true;
            }
        }

        private boolean finishExercise(int exerciseNumber) {
            System.out.println("===========================================");
            System.out.println("All students finished exercise " + exerciseNumber);
            System.out.println("===========================================");
            // end phaser by returning true if it is last phase which has finished else return false
            return exerciseNumber == 4;
        }
    }

    private static final class Student implements Runnable {
        private final String name;
        private final Phaser phaser;

        Student(String name, Phaser phaser) {
            this.name = name;
            this.phaser = phaser;
        }
        @Override
        public void run() {
            System.out.println(name + " has arrived.");
            doExercise(1);
            phaser.arriveAndAwaitAdvance();
            doExercise(2);
            phaser.arriveAndAwaitAdvance();
            doExercise(3);
            phaser.arriveAndAwaitAdvance();
            doExercise(4);
            phaser.arriveAndAwaitAdvance();
        }

        private void doExercise(int i) {
            System.out.println(name + " is doing exercise " + i);
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + " finished exercise " + i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyPhaser phaser = new MyPhaser(5);
        for (int i = 0; i < 5; i++) {
            new Thread(new Student("Student" + i, phaser)).start();
        }
        // simulate the late student
        TimeUnit.MILLISECONDS.sleep(15);
        phaser.register();
        new Thread(new Student("Late Student", phaser)).start();
    }
}
