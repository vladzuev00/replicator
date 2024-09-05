package by.aurorasoft.replicator.util;

import lombok.experimental.UtilityClass;

import static java.lang.Runtime.getRuntime;

@UtilityClass
public final class ShutdownHookUtil {

    public static void addShutdownHook(Runnable task) {
        getRuntime().addShutdownHook(new Thread(task));
    }
}
