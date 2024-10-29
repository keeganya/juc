import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * @Author xiaohu
 * @Date 2024/10/28 16:11
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestStampedLock
 * @Description: StampedLock 【不支持条件变量； 不支持可重入】
 * @Version 1.0
 */
@Slf4j(topic = "c.TestStampedLock")
public class TestStampedLock {
    public static void main(String[] args) {
        DataContainStamped dataContainer = new DataContainStamped(1);

        new Thread(() -> {
            try {
                dataContainer.read(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();

        new Thread(() -> {
            try {
                dataContainer.write(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}

@Slf4j(topic = "c.DataContainStamped")
class DataContainStamped {
    private int data;
    private final StampedLock lock = new StampedLock();
    public DataContainStamped(int data) {
        this.data = data;
    }

    public int read(int readTime) throws InterruptedException {
        long stamp = lock.tryOptimisticRead();
        log.debug("optimicstic read locking... {}",stamp);
        Thread.sleep(readTime);
        if (lock.validate(stamp)) {
            log.debug("read finish... {},data:{}",stamp, data);
            return data;
        }
        // 锁升级 - 读锁
        log.debug("updating to read lock....{}",stamp);
        try {
            stamp = lock.readLock();
            log.debug("read lock {}",stamp);
            Thread.sleep(readTime);
            log.debug("read finish...{},data:{}",stamp,data);
            return data;
        } finally {
            log.debug("read unlock {}",stamp);
            lock.unlockRead(stamp);
        }
    }

    public void write(int newDate) throws InterruptedException {
        long stamp = lock.writeLock();
        try {
            log.debug("write lock {}",stamp);
            Thread.sleep(2000);
            this.data = newDate;
        } finally {
            log.debug("write unlock {}",stamp);
            lock.unlockWrite(stamp);
        }
    }
}