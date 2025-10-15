package com.undeadlydev.UTA.utils;

import com.tcoded.folialib.impl.PlatformScheduler;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import com.undeadlydev.UTA.Main;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public abstract class Task {
    protected WrappedTask task = null;

    public abstract void run();

    public abstract void schedule();

    public void cancel() {
        if (task != null) {
            task.cancel();
        }
    }

    protected PlatformScheduler getScheduler() {
        return Main.get().getScheduler();
    }

    // These methods allow tasks to be scheduled in NMS modules without transitive dependencies.
    public static void runLater(Runnable runnable, long delay) {
        Main.get().getScheduler().runLater(t -> runnable.run(), delay);
    }

    public static void runAtEntityLater(Entity entity, Runnable runnable, long delay) {
        Main.get().getScheduler().runAtEntityLater(entity, t -> runnable.run(), delay);
    }

    public static void runAtLocationLater(Location location, Runnable runnable, long delay) {
        Main.get().getScheduler().runAtLocationLater(location, t -> runnable.run(), delay);
    }
}
