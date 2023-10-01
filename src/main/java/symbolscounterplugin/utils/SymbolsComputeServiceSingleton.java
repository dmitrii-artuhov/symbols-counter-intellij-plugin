package symbolscounterplugin.utils;

import com.intellij.util.concurrency.AppExecutorUtil;

import java.util.concurrent.Executor;

public class SymbolsComputeServiceSingleton {
    private SymbolsComputeServiceSingleton() {}

    private static class SingletonHelper {
        private static final Executor INSTANCE =
                AppExecutorUtil.createBoundedScheduledExecutorService(Constants.SYMBOLS_COMPUTING_THREAD_NAME, 1);
    }

    public static Executor getInstance() {
        return SingletonHelper.INSTANCE;
    }
}
