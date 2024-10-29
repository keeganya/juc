package org.xiaohu.juc.concurrent.pool.method;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 16:13
 * @PackageName:org.xiaohu.juc.concurrent.pool.method
 * @ClassName: TestShutDown
 * @Description: 线程池状态变为 SHUTDOWN
 *              - 不会接收新任务
 *              - 但已提交任务会执行完
 *              - 此方法不会阻塞调用线程的执行
 * @Version 1.0
 */
@Slf4j(topic = "c.TestShutDown")
public class TestShutDown {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            AtomicInteger i = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestShutdown_t" + i.getAndIncrement());
            }
        });

        /* 已提交任务会执行完
        * */
        Future<Integer> future1 = pool.submit(() -> {
            log.debug("task 1 running...");
            Thread.sleep(1000);
            log.debug("task 1 finish...");
            return 1;
        });

        Future<Integer> future2 = pool.submit(() -> {
                log.debug("task 2 running...");
                Thread.sleep(1000);
                log.debug("task 2 finish...");
                return 2;
        });

        Future<Integer> future3 = pool.submit(() -> {
                log.debug("task 3 running...");
                Thread.sleep(1000);
                log.debug("task 3 finish...");
                return 3;
        });

        pool.shutdown();

        // 在线程池调用shutdown后，不再接收新的任务
        /*
        Future<Integer> future4 = pool.submit(() -> {
            log.debug("task 4 running...");
            Thread.sleep(1000);
            log.debug("task 4 runnig...");
            return 4;
        });
        */


        // 此方法不会阻塞调用线程的执行
        // main线程调用 shutdow() 后，main线程后面的代码仍然会执行完
        log.debug("{}",pool.isShutdown());
        // main线程（调用者）不会等待所有任务都执行完成
        // main线程会立刻打印 ["other..."]
        /* 需要等待的话 加入 【pool.awaitTermination(3, TimeUnit.SECONDS);】方法
           或者 future.get() 方法
        * */
        pool.awaitTermination(3,TimeUnit.SECONDS);
        log.debug("{}",future1.get());
        log.debug("{}",future2.get());
        log.debug("{}",future3.get());

        log.debug("other...");


    }
}
