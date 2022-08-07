package com.davidout.Utils;

import com.davidout.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.IntConsumer;

public class CountdownTask {

    private int counter;
    private int time;
    private final IntConsumer countdownAction;
    private final Runnable completionAction;
    private boolean cancelled;
    private BukkitTask schedular;



    public CountdownTask(final int limit, final IntConsumer countdownAction, final Runnable completionAction) {
        this.counter = limit;
        this.time = limit;
        this.countdownAction = countdownAction;
        this.completionAction = completionAction;
        startCounter();
    }

    public void stopCounter() {
        this.cancelled = true;
    }

    public void startCounter() {
        schedular = new BukkitRunnable() {

            @Override
            public void run() {
                --counter;
                if(cancelled) {
                    cancel();
                    return;
                }

                if (counter <= 0) {
                    completionAction.run();
                    cancel();
                    return;
                }

                countdownAction.accept(counter);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    public void restart() {
        this.schedular.cancel();
        this.counter = time;
        startCounter();
    }

    public void setTime(int timeInSeconds) {
        this.counter = timeInSeconds;
    }


}
