package brainflow.utils;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Mar 14, 2009
 * Time: 5:16:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class StopWatch {


    private Map<String, ClockState> clockMap = new HashMap<String, ClockState>();


    public ClockState start(String name) {

        ClockState clock = new ClockState();
        clockMap.put(name, clock);
        clock.start();
        return clock;
    }

    public ClockState start(String name, String message) {
        System.out.println(message);
        ClockState clock = new ClockState();
        clockMap.put(name, clock);
        clock.start();
        return clock;
    }


    public final ClockState stop(String name) {
        //long curtime = System.nanoTime();

        ClockState clock = clockMap.get(name);
        clock.stop();
        return clock;
    }

    public final ClockState stopAndReport(String name) {
        //long curtime = System.nanoTime();

        ClockState clock = clockMap.get(name);
        clock.stop();
        System.out.println("task " + name + " took: " + clock.duration/1000000.0);
        return clock;
    }




    public final ClockState pause(String name) {
        ClockState clock = clockMap.get(name);
        clock.pause();
        return clock;
    }

    public final ClockState resume(String name) {
        ClockState clock = clockMap.get(name);
        clock.resume();
        return clock;

    }


    public static class ClockState {

        public long start = 0;

        public long end = Long.MIN_VALUE;

        public long duration = 0;

        public long current = 0;

        public boolean on = false;


        public void start() {
            start = System.nanoTime();
            current = start;
            end = Long.MIN_VALUE;
            duration = 0;
            on = true;
        }

        public void resume() {
            current = System.nanoTime();
            on = true;
        }

        public void pause() {
            if (on) {
                duration = duration + (System.nanoTime() - current);
                on = false;
            }
        }

        public void stop() {
            if (on) {
                end = System.nanoTime();
                duration = duration + (end - current);
                current = end;
            }
        }


    }


}
