package me.zeuss.permissionsplus.utilities.task;

import me.zeuss.permissionsplus.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class APIScheduler {

    private static BukkitTask task;
    private static BukkitTask asyncTask;
    private static Set<ScheduledTask> tasks;
    private static Set<ScheduledTask> asyncTasks;
    private static Set<ScheduledTask> toRemove;

    private static void init() {
        if (!APIScheduler.tasks.isEmpty()) {
            APIScheduler.task = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
                if (!APIScheduler.toRemove.isEmpty()) {
                    APIScheduler.tasks.removeAll(new ArrayList<>(APIScheduler.toRemove));
                    APIScheduler.toRemove.clear();
                }
                if (APIScheduler.tasks.isEmpty()) {
                    APIScheduler.task.cancel();
                    APIScheduler.task = null;
                }
                for (final ScheduledTask task : new ArrayList<>(APIScheduler.tasks)) {
                    APIScheduler.tasks.remove(task);
                    if (task.aux == task.value * task.unit.getTicks()) {
                        if (task.runnable.iterations >= 0 && task.runnable.aux == task.runnable.iterations) {
                            task.runnable.run();
                            task.cancel();
                            continue;
                        }
                        task.aux = 1;
                        task.runnable.run();
                        task.runnable.aux++;
                    }
                    else {
                        task.aux++;
                    }
                    APIScheduler.tasks.add(task);
                }
            }, 0L, 1L);
        }
    }

    public static ScheduledTask startTask(final TaskRunnable runnable, final int value, final SchedulerUnit unit) {
        return startAutoTask(runnable, -1, value, unit, false);
    }

    public static ScheduledTask startDelayedTask(final TaskRunnable runnable, final int value, final SchedulerUnit unit) {
        return startAutoTask(runnable, 1, value, unit, false);
    }

    public static ScheduledTask startAutoTask(final TaskRunnable runnable, final int iterations, final int value, final SchedulerUnit unit) {
        return startAutoTask(runnable, iterations, value, unit, false);
    }

    private static ScheduledTask startAutoTask(final TaskRunnable runnable, final int iterations, final int value, final SchedulerUnit unit, final boolean async) {
        final ScheduledTask localTask = new ScheduledTask(runnable, value, unit);
        runnable.task = localTask;
        runnable.iterations = iterations;
        if (iterations != 1) {
            localTask.runnable.run();
        }
        if (async) {
            APIScheduler.asyncTasks.add(localTask);
            if (APIScheduler.asyncTask == null) {
                init();
            }
        }
        else {
            APIScheduler.tasks.add(localTask);
            if (APIScheduler.task == null) {
                init();
            }
        }
        return localTask;
    }

    static {
        APIScheduler.task = null;
        APIScheduler.asyncTask = null;
        APIScheduler.tasks = new HashSet<>();
        APIScheduler.asyncTasks = new HashSet<>();
        APIScheduler.toRemove = new HashSet<>();
    }

    public abstract static class TaskRunnable implements Runnable {
        private ScheduledTask task;
        private int aux;
        private int iterations;

        public TaskRunnable() {
            this.aux = 1;
            this.iterations = -1;
        }

        public void cancel() {
            this.task.cancel();
        }

        public int getAux() {
            return this.aux;
        }
    }

    public enum SchedulerUnit {
        TICK(1),
        SECOND(20),
        MINUTE(1200),
        HOUR(72000),
        DAY(1728000);

        private int ticks;

        private SchedulerUnit(final int multi) {
            this.ticks = multi;
        }

        public int getTicks() {
            return this.ticks;
        }
    }

    public static class ScheduledTask {
        private int aux;
        private int value;
        private SchedulerUnit unit;
        private TaskRunnable runnable;

        public ScheduledTask(final TaskRunnable r, final int v, final SchedulerUnit u) {
            this.aux = 1;
            this.runnable = r;
            this.value = v;
            this.unit = u;
        }

        public int getAux() {
            return this.aux;
        }

        public void cancel() {
            if (!APIScheduler.toRemove.contains(this)) {
                APIScheduler.toRemove.add(this);
            }
        }
    }

}
