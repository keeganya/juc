import lombok.extern.slf4j.Slf4j;
import org.graalvm.nativeimage.hosted.Feature;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * @Author xiaohu
 * @Date 2024/10/23 8:45
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestJoin
 * @Description: 1.join方式实现同步 （缺点：1.需要外部共享变量，不符合面向对象封装的思想；2.必须等待线程结束，不能配合线程池使用）
 *               2.Future方式实现同步；规避了join方式的缺点，main线程接收结果，get方法是让调用线程同步等待；
 * @Version 1.0
 */
@Slf4j(topic = "c.TestJoin")
public class TestJoin {
    static int r = 0;
    static int r1 = 0;
    static int r2 = 0;
    static int r3 = 0;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        test1();
//        test2();
//        test3();
        testFuture();
    }

    public static void test1() throws InterruptedException {
        Runnable t1 = () -> {
            log.debug("开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("结束");
            r = 10;
        };

        Thread t = new Thread(t1, "t1");
        t.start();
        t.join();

        log.debug("结果为：{}",r);
        log.debug("结束");
    }

    public static void test2() throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        Runnable t1 = () -> {
          log.debug("t1开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r1 = 10;
            log.debug("t1结束");
        };

        Runnable t2 = () -> {
            log.debug("t2开始");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r2 = 20;
            log.debug("t2结束");
        };
        long endTime = System.currentTimeMillis();

        Thread thread1 = new Thread(t1, "t1");
        Thread thread2 = new Thread(t2, "t2");

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        log.debug("r1的值：{}; r2的值：{}; 耗时 {}",r1,r2, (endTime - beginTime));
    }

    public static void test3() throws InterruptedException {
        Runnable t3 = () -> {
            log.debug("t3线程开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            r3 = 30;
            log.debug("t3线程结束");
        };

        long beginTime = System.currentTimeMillis();
        Thread thread3 = new Thread(t3, "t3");
        thread3.start();
        // 带超时时间的join同步
        thread3.join(600);
        long endTime = System.currentTimeMillis();

        log.debug("r3的值 {}, 耗时 {}", r3, endTime - beginTime);
    }

    public static void testFuture() throws ExecutionException, InterruptedException {
        log.debug("main线程开始");
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            log.debug("开始");
            Thread.sleep(1000);
            log.debug("结束");
            return 10;
        });

        Thread futureThread = new Thread(futureTask, "t1");
        futureThread.start();

        log.debug("结果为 {}",futureTask.get());
    }
}
