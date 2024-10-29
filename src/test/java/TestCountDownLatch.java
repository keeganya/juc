import lombok.extern.slf4j.Slf4j;

import java.sql.Array;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xiaohu
 * @Date 2024/10/28 17:43
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestCountDownLatch
 * @Description: 【CountDownLatch】 用来进行线程同步协作，等待所有线程完成倒计时
 *                其中构造参数用来初始化等待计数值，await() 用来等待计数归零，countDown() 用来让计数减一
 * @Version 1.0
 */
@Slf4j(topic = "c.TestCountDownLatch")
public class TestCountDownLatch {
    public static void main(String[] args) throws InterruptedException {
//        test1();
//        test2();
        test3();
    }

    public static void test1() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            try {
                log.debug("begin...");
                Thread.sleep(1000);
                latch.countDown();
                log.debug("end... {}", latch.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                log.debug("begin...");
                Thread.sleep(1500);
                latch.countDown();
                log.debug("end... {}", latch.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                log.debug("begin...");
                Thread.sleep(2000);
                latch.countDown();
                log.debug("end... {}", latch.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        log.debug("main waitting...");
        latch.await();
        log.debug("main end....");
    }

    public static void test2() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        ExecutorService pool = Executors.newFixedThreadPool(4);

        pool.submit(() -> {
            log.debug("begin...");
            try {
                Thread.sleep(1000);
                latch.countDown();
                log.debug("end...{}",latch.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.submit(() -> {
            log.debug("begin...");
            try {
                Thread.sleep(1500);
                latch.countDown();
                log.debug("end...{}",latch.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.submit(() -> {
           log.debug("begin...");
            try {
                Thread.sleep(2000);
                latch.countDown();
                log.debug("end...{}",latch.getCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        pool.submit(() -> {
           log.debug("waitting...");
            try {
                latch.await();
                log.debug("wait end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static void test3() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10, new ThreadFactory() {
            AtomicInteger i = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "t" + i.getAndIncrement());
            }
        });

        CountDownLatch latch = new CountDownLatch(10);
        String[] all = new String[10];
        Random r = new Random();

        for (int j = 0; j < 10; j++) {
            int x = j;
            pool.submit(() -> {
                for (int i = 0; i <= 100; i++) {
                    try {
                        Thread.sleep(r.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    all[x] = Thread.currentThread().getName() + "(" + (i + "%") + ")";
                    System.out.print("\r" + Arrays.toString(all));
                }
                latch.countDown();
            });
        }
        latch.await();
        System.out.println("\n游戏开始...");
        pool.shutdown();
    }
}
