package util;

import java.util.function.Consumer;

/** Single place every game class prints through. The GUI swaps the sink for the log panel. */
public final class GameLog {

    private static volatile Consumer<String> sink = System.out::println;
    private static final ThreadLocal<Consumer<String>> override = new ThreadLocal<>();

    private GameLog() { }

    public static void setSink(Consumer<String> newSink) {
        sink = newSink == null ? System.out::println : newSink;
    }

    /** Mutes the calling thread (used by background batch simulations). */
    public static void silenceCurrentThread() {
        override.set(m -> { });
    }

    public static void log(String msg) {
        Consumer<String> c = override.get();
        (c != null ? c : sink).accept(msg);
    }

    public static void logf(String fmt, Object... args) {
        log(String.format(fmt, args));
    }
}
