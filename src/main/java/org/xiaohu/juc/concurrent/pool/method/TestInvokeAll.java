package org.xiaohu.juc.concurrent.pool.method;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 15:38
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestInvokeAll
 * @Description: invokeAll 提交所有任务，所有任务的返回值都会返回，并返回到范型为Future的List
 * @Version 1.0
 */
@Slf4j(topic = "c.TestInvokeAll")
public class TestInvokeAll {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {
            AtomicInteger i = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestInvokeAll_t" + i.getAndIncrement());
            }
        });

        List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    log.debug("begin1...");
                    Thread.sleep(1000);
                    return "1";
                },
                () -> {
                    log.debug("begin2...");
                    Thread.sleep(1000);
                    return "2";
                },
                () -> {
                    log.debug("begin3...");
                    Thread.sleep(1000);
                    return "3";
                }
        ));

        futures.forEach(f -> {
            try {
                log.debug("{}",f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
