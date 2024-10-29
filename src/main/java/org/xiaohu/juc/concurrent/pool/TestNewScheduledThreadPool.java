package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/26 12:21
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestNewScheduledThreadPool
 * @Description: 任务调度线程池 schedule(); scheduleAtFixedRate(); scheduleWithFixedDelay()
 * @Version 1.0
 */

@Slf4j(topic = "c.TestNewScheduledThreadPool")
public class TestNewScheduledThreadPool {
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestNewScheduledThreadPool_t" + new AtomicInteger(1).getAndIncrement());
            }
        });

       /* pool.schedule(() -> {
            log.debug("task 1");
            int i = 1 / 0;
        },1, TimeUnit.SECONDS);

        pool.schedule(() -> {
            log.debug("task 2");
        },1,TimeUnit.SECONDS);*/

        /*log.debug("start...");
        // 一开始，延时 1s，接下来，由于任务执行时间 > 间隔时间，间隔被『撑』到了 2s （在上一次任务执行时 开始算间隔时间）
        pool.scheduleAtFixedRate(() -> {
            log.debug("running...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1,1,TimeUnit.SECONDS);*/

        // 从上次任务结束后开始按照间隔时间执行任务 3s执行一次 （在上一次任务执行结束后，开始算间隔时间）
        pool.scheduleWithFixedDelay(() -> {
            log.debug("running...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },1,1,TimeUnit.SECONDS);
    }
}
