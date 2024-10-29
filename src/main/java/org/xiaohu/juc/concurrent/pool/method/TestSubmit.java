package org.xiaohu.juc.concurrent.pool.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 15:28
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestSubmit
 * @Description: submit(Callable<T> task)方法有返回值，execute方法没有返回值
 * @Version 1.0
 */
@Slf4j(topic = "c.TestSubmit")
public class TestSubmit {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            AtomicInteger i = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"TestSubmit_t" + i.getAndIncrement());
            }
        });

        Future<String> future = pool.submit(() -> {
            try {
                log.debug("running...");
                Thread.sleep(1000);
                return "ok";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        log.debug("{}",future.get());
    }
}
