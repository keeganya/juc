import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author xiaohu
 * @Date 2024/10/29 9:21
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestCyclicBarrier
 * @Description: 实现 task1,task2 每次执行完 最后由main线程进行汇总（task1 task2 finsh...)
 * @Version 1.0
 */
@Slf4j(topic = "c.TestCyclicBarrier")
public class TestCyclicBarrier {
    public static void main(String[] args) throws InterruptedException {
        // CountDownLatch 方式
//        test1();

        // CyclicBarrier 方式
        /** CyclicBarrier 计数 在每次执行完时会重新恢复（从 0 -> 2,还是原来的对象）
         * 解决方案 ：由于CyclicBarrier在计数到零后会恢复，所以对象只需要创建一次，放在循环外面创建就行
        * */
       /* 线程数要和CyclicBarrier的计数值保持一致
       *  假如线程数是 3，CyclicBarrier的计数是 2，则执行的时候，task1需要1s,task2需要2s,
       *  此时是第一次循环的task1 和 第二次循环的 task1 把 计数2 消耗完，
       *  执行的顺序和预期顺序就不一致了
       * */
        ExecutorService pool = Executors.newFixedThreadPool(2);

        /** 第二个参数 是一个函数，表示在达到计数后会执行的逻辑
        * */
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
            log.debug("task1 task2 finish....");
        });
        for (int i = 0; i < 3; i++) {
            pool.submit(() -> {
               log.debug("task1 begin ....");
                try {
                    Thread.sleep(1000);
                    barrier.await(); // 每次会加一 （0 + 1 = 1）
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });

            pool.submit(() -> {
               log.debug("task2 begin ....");
                try {
                    Thread.sleep(2000);
                    barrier.await(); // 1 + 1 = 2 等于2后退出
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();
    }


    /** task1,task2 每次执行完 最后由main线程进行汇总（task1 task2 finsh...）
     * 存在问题：CountDownLatch 计数 每次执行完毕会清零，需要重新生成对象，
     * 所以每次for循环都需要重新new一个countDownLatch对象
    * */
    public static void test1() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 3; i++) {
            CountDownLatch latch = new CountDownLatch(2);
            pool.submit(() ->{
               log.debug("task1 statrt...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });

            pool.submit(() -> {
               log.debug("task2 start...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });

            latch.await();
            log.debug("task1 task2 finish....");
        }
        pool.shutdown();
    }
}
