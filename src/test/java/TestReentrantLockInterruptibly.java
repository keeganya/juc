import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author xiaohu
 * @Date 2024/10/23 17:32
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestReentrantLockInterruptibly
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestReentrantLockInterruptibly")
public class TestReentrantLockInterruptibly {

    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            log.debug("启动");
            try {
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("等锁的过程中被打断");
                return;
            }
            try {
                log.debug("获得了锁");
            } finally {
                lock.unlock();
            }
        },"t1");

        lock.lock();
        log.debug("main线程获取到锁");
        t1.start();
        try {
            Thread.sleep(1000);
            t1.interrupt();
            log.debug("t1线程的打断状态：{}",t1.isInterrupted());
            log.debug("执行打断");
        } finally {
            lock.unlock();
        }
    }


}
