package brainflow.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 29, 2008
 * Time: 7:00:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OndeckTaskExecutor<T> {

    private ExecutorService service;

    private Task<T> runningTask;

    private Task<T> waitingTask;

    public OndeckTaskExecutor() {
        service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public OndeckTaskExecutor(ExecutorService service) {
        this.service = service;
    }

    private void submitPrivate(Task<T> task) {
        runningTask = task;

        service.submit(task);
    }

    public ExecutorService getExecutorService() {
        return service;

    }

    public synchronized void submitTask(Callable<T> task) {
        if (runningTask == null || runningTask.isDone()) {
            submitPrivate(new Task<T>(task));
        } else {
            waitingTask = new Task<T>(task);
        }
    }


    class Task<T> extends FutureTask {
        Callable<T> tCallable;

        public Task(final Callable<T> tCallable) {
            super(tCallable);
            this.tCallable = tCallable;
        }

        protected void done() {
            if (waitingTask != null) {
                 synchronized (this) {
                    submitPrivate(waitingTask);
                    waitingTask = null;
                }
            }
        }

        public String toString() {
            return tCallable.toString();
        }
    }

    private static Callable<Integer> createCallable(final int id) {
        return new Callable<Integer>() {
            public Integer call() throws Exception {
                long sleep = (int) Math.abs((Math.random() * 100));
                Thread.sleep(sleep);
                return id;
            }

            public String toString() {
                return "id: " + id;
            }
        };

    }

    public static void main(String[] args) throws Exception {
        OndeckTaskExecutor<Integer> runner = new OndeckTaskExecutor<Integer>();
        for (int i = 0; i < 100; i++) {
            long sleep = (int) Math.abs((Math.random() * 50));
            Thread.sleep(sleep);
            runner.submitTask(createCallable(i));
        }

    }
}
