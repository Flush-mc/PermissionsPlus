package me.zeuss.permissionsplus.utilities.task;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.zeuss.permissionsplus.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings({ "rawtypes", "unused" })
public class TaskManager {

    private static Table<String, BukkitTask, TaskData> tasks;

    public static void startTask(final String key, final Task task, final long delay, final long period) {
        if (TaskManager.tasks.containsRow(key)) {
            cancel(key);
        }
        TaskData put = TaskManager.tasks.put(key, Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            public void run() {
                TaskData data = null;
                final Iterator i$ = TaskManager.tasks.row(key).values().iterator();
                if (i$.hasNext()) {
                    final TaskData d = data = (TaskData) i$.next();
                }
                task.onTick(data);
                final TaskData taskData = data;
                ++taskData.aux;
            }
        }, delay, period), new TaskData(key, 0, delay, period));
    }

    public static void startTask(final String key, final DelayTask task, final long delay) {
        if (TaskManager.tasks.containsRow(key)) {
            cancel(key);
        }
        TaskManager.tasks.put(key, Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            TaskManager.cancel(key);
            task.onEnded();
        }, delay), new TaskData(key, 0, delay, 0L));
    }

    public static void cancel(final String key) {
        if (!TaskManager.tasks.containsRow(key)) {
            return;
        }
        final Set<BukkitTask> cache = new HashSet<>();
        for (final BukkitTask task : TaskManager.tasks.row(key).keySet()) {
            cache.add(task);
            task.cancel();
        }
        for (final BukkitTask t : cache) {
            TaskManager.tasks.remove(key, t);
        }
    }

    public static void cancelAll() {
        final Set<String> list = new HashSet<>(TaskManager.tasks.rowKeySet());
        for (final String s : list) {
            if (!s.endsWith("-bypass")) {
                cancel(s);
            }
        }
    }

    static {
        TaskManager.tasks = HashBasedTable.create();
    }

    public static class TaskData {
        private String key;
        public int aux;
        private long delay;
        private long period;

        public TaskData(final String key, final int aux, final long delay, final long period) {
            this.key = key;
            this.aux = aux;
            this.delay = delay;
            this.period = period;
        }

        public String getKey() {
            return this.key;
        }

        public int getAux() {
            return this.aux;
        }

        public long getDelay() {
            return this.delay;
        }

        public long getPeriod() {
            return this.period;
        }
    }

    public interface DelayTask {
        void onEnded();
    }

    public interface Task {
        void onTick(final TaskData p0);
    }

}
