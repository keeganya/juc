package org.xiaohu.juc.concurrent.pool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/25 17:14
 * @PackageName:org.xiaohu.juc.concurrent.pool
 * @ClassName: TestHunger
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestHunger")
public class TestHunger {
    static final List<String> MENU = Arrays.asList("地三鲜", "宫保鸡丁", "辣子鸡丁", "烤鸡翅");
    static Random RANDOM = new Random();
    public static String cooking(){
        return MENU.get(RANDOM.nextInt(MENU.size()));
    }
    public static void main(String[] args) {
        /*
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger i = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestHunger_t" + i.getAndIncrement());
            }
        });
        */

        // 点餐线程池
        ExecutorService orderPool = Executors.newFixedThreadPool(1, new ThreadFactory() {
            private AtomicInteger i = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestHunger_点餐线程" + i.getAndIncrement());
            }
        });

        // 做菜线程池
        ExecutorService cookPool = Executors.newFixedThreadPool(1, new ThreadFactory() {
            private AtomicInteger i = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TestHunger_做菜线程" + i.getAndIncrement());
            }
        });


        // 一个客人 两个线程可以忙的过来
        orderPool.execute(() -> {
            log.debug("处理点餐...");
            Future<String> future1 = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜：{}",future1.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        // 来了第二个客人 两个线程都去处理点餐了 没有线程做菜了，最后都会等待做完菜才能上菜
        /*  解决方案：1）增加线程数 ; 2）创建不同的线程池;(点餐线程池，做菜线程池) 各司其职
        * */
        orderPool.execute(() -> {
            log.debug("处理点餐");
            Future<String> future2 = cookPool.submit(() -> {
                log.debug("做菜");
                return cooking();
            });
            try {
                log.debug("上菜：{}",future2.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
