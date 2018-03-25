package it.pattystrolly.wedding.service;

import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Slf4j
public class RetryableService {

    protected static int multiplier = 500;
    protected static int timeout = 60;
    protected static int attempts = 10;
    protected static TimeUnit timeUnit = TimeUnit.SECONDS;

    protected static <T>T execute(final Callable<T> callable) throws Throwable {
        try {
            Retryer<T> retryer = getRetryer();
            return retryer.call(callable);
        } catch (ExecutionException | RetryException e) {
            throw e.getCause();
        }
    }

    protected static <T>Retryer<T> getRetryer(){
        return RetryerBuilder.<T>newBuilder()
                .withWaitStrategy(WaitStrategies.exponentialWait(
                        multiplier, timeout, timeUnit))
                .withStopStrategy(StopStrategies.stopAfterAttempt(attempts))
                .retryIfException(new Predicate<Throwable>() {

                    @Override
                    public boolean apply(Throwable t) {
                        if (t instanceof IOException) {
                            IOException e = (IOException) t;
                            log.warn("Retrying: " + e.getMessage(), e);
                            return true;
                        } else if (t instanceof RuntimeException) {
                            RuntimeException e = (RuntimeException) t;
                            log.warn("Retrying: " + e.getMessage(), e);
                            return true;
                        }
                        log.error(t.getMessage(), t);
                        return false;
                    }
                })
                .build();
    }
}
