import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.TestReentrantLockTryLock")
public class TestReentrantLockTryLock {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            log.debug("启动....");
            try {
                if (!lock.tryLock(2, TimeUnit.SECONDS)) {
                    log.debug("尝试获取锁失败，立刻返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("被打断，获取不到锁");
                return;
            }
            try {
                log.debug("t1获取到锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        log.debug("main线程获取到锁");
        t1.start();
//        t1.interrupt();

        try {
            Thread.sleep(1000);
        } finally {
            lock.unlock();
        }
    }

}
