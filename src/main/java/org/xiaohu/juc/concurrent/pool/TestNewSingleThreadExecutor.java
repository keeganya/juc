package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 15:02
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestNewSingleThreadExecutor
 * @Description: 只有 1 个核心线程，没有救急线程
 * @Version 1.0
 */

/**
 * public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
            return new AutoShutdownDelegatedExecutorService
                    (new ThreadPoolExecutor(1, 1,
                                            0L, TimeUnit.MILLISECONDS,
                                            new LinkedBlockingQueue<Runnable>(),
                                            threadFactory));
            }
 * */

/** Executors.newSingleThreadExecutor() 线程个数始终为1，不能修改
    FinalizableDelegatedExecutorService 应用的是装饰器模式，只对外暴露了 ExecutorService 接口，因
    此不能调用 ThreadPoolExecutor 中特有的方法
    Executors.newFixedThreadPool(1) 初始时为1，以后还可以修改
    对外暴露的是 ThreadPoolExecutor 对象，可以强转后调用 setCorePoolSize 等方法进行修改
* */
@Slf4j(topic = "c.TestNewSingleThreadExecutor")
public class TestNewSingleThreadExecutor {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor(new ThreadFactory() {
            private AtomicInteger i = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"myNewSingleThreadExecutor_t" + i.getAndIncrement());
            }
        });
        pool.execute(() -> {
            log.debug("1");
            /*自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，
              而线程池还会新建一个线程，保证池的正常工作 * */
            int i = 1 / 0;
        });

        pool.execute(() -> {
            log.debug("2");
        });

        pool.execute(() -> {
            log.debug("3");
        });

    }
}
