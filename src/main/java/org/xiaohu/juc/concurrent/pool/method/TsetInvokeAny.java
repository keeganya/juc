package org.xiaohu.juc.concurrent.pool.method;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 15:53
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TsetInvokeAny
 * @Description: invokeAny 提交执行所有任务 ，只会返回最先执行完成的任务的返回值，其他任务取消
 * @Version 1.0
 */
@Slf4j(topic = "c.TestInvokeAny")
public class TsetInvokeAny {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3, new ThreadFactory() {
            AtomicInteger i = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestInvokeAny_t" + i.getAndIncrement());
            }
        });

        String result = pool.invokeAny(Arrays.asList(
                () -> {
                    log.debug("begin1...");
                    Thread.sleep(100);
                    log.debug("end1...");
                    return "ok1";
                },
                () -> {
                    log.debug("begin2...");
                    Thread.sleep(50);
                    log.debug("end2...");
                    return "ok2";
                },
                () -> {
                    log.debug("begin3...");
                    Thread.sleep(200);
                    log.debug("end3...");
                    return "ok3";
                }
        ));

        log.debug("{}",result);

    }
}
