package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 14:07
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestNewFixedThreadPool
 * @Description: 全部为核心线程，没有救急线程 的线程池
 * @Version 1.0
 */

/**
public static ExecutorService newFixedThreadPool(int nThreads){
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L,TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runable>());
        }
 */

@Slf4j(topic = "c.TestNewFixedThreadPool")
public class TestNewFixedThreadPool {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger i = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                    return new Thread(r,"myPool_t" + i.getAndIncrement());
            }
        });

        pool.execute(() -> {
            log.debug("1");
        });

        pool.execute(() ->{
            log.debug("2");
        });

        pool.execute(() -> {
            log.debug("3");
        });
    }
}
