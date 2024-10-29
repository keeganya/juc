package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 14:25
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestNewCachedThreadPool
 * @Description: 全部都是救急线程，没有核心线程，最大线程数为 Integer.MAX_VALUE,救急线程存活时间为60s
 * @Version 1.0
 */

/**
 * public static ExecutorService newCachedThreadPool(){
            return new ThreadPoolExecutor(0,Integer.MAX_VALUE,
                                          60L,TimeUnit.SECONDS,
                                          new SynchronousQueue<Runnable>());
        }
 */
@Slf4j(topic = "c.TestNewCachedThreadPool")
public class TestNewCachedThreadPool {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool(new ThreadFactory() {
            AtomicInteger i = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"myNewCacheThreadPool_t" + i.getAndIncrement());
            }
        });

        pool.execute(() -> {
            log.debug("1");
        });

        pool.execute(() -> {
            log.debug("2");
        });

        pool.execute(() -> {
            log.debug("3");
        });
    }
}
