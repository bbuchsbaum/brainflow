package brainflow.core.rendering;

import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 28, 2008
 * Time: 3:21:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskQueue<T> {

    //private BlockingQueue<T> queue = new ArrayBlockingQueue<T>(10);

    private ExecutorService service;

    public TaskQueue() {
        service = Executors.newFixedThreadPool(1);
    }

    public void submit(FutureTask<T> task) {
        service.submit(task);
    }

    public static Callable<Integer> createTask(final int id) {
        return new Callable<Integer>() {
            public Integer call() throws Exception {
                long sleep = (long) (Math.random() * Math.abs(1000));
                Thread.sleep(sleep);
                return id;
            }

            
        };
    }


    public static void main(String[] args) {
        TaskQueue<Integer> queue = new TaskQueue<Integer>();

        for (int i = 0; i < 100; i++) {
            FutureTask<Integer> task = new FutureTask<Integer>(createTask(i)) {
                protected void done() {
                    super.done();
                    try {
                        //System.out.println("result : " + get());
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }



                protected void set(Integer integer) {
                    super.set(integer);
                    
                }
            };
            queue.submit(task);
        }
    }
}





