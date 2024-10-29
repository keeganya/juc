package org.xiaohu.juc.concurrent.pool.method;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 16:43
 * @PackageName:org.xiaohu.juc.concurrent.pool.method
 * @ClassName: TestShutDownNow
 * @Description: 线程池状态变为 STOP
 *              - 不会接收新任务
 *              - 会将队列中的任务返回
 *              - 并用 interrupt 的方式中断正在执行的任务
 * @Version 1.0
 */
@Slf4j(topic = "c.TestShutDownNow")
public class TestShutDownNow {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            AtomicInteger i = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestShutDown_t" + i.getAndIncrement());
            }
        });

        /* 正在执行的任务 task 1 ,task 2 会中断
        * */
        Future<Integer> future1 = pool.submit(() -> {
            log.debug("task 1 running...");
            Thread.sleep(1000);
            log.debug("task 1 ending...");
            return 1;
        });

        Future<Integer> future2 = pool.submit(() -> {
            log.debug("task 2 running...");
            Thread.sleep(1000);
            log.debug("task 2 ending...");
            return 2;
        });

        /* 在队列中的任务被返回
        * */
        Future<Integer> future3 = pool.submit(() -> {
            log.debug("task 3 running...");
            Thread.sleep(1000);
            log.debug("task 3 ending...");
            return 3;
        });

        List<Runnable> runnables = pool.shutdownNow(); // 返回的是在队列中的任务
        log.debug("{}",runnables);

    }
}
