import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author xiaohu
 * @Date 2024/10/28 13:52
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestAqs
 * @Description: AbstractQueueSynchronizer(AQS)实现不可重入锁
 * @Version 1.0
 */
@Slf4j(topic = "c.TestAqs")
public class TestAqs {
    public static void main(String[] args) {
        MyLock myLock = new MyLock();

        new Thread(() -> {
            myLock.lock();
            log.debug("第一次locking....");
            /* 不可重入锁 第二次无法重新获取锁
            myLock.lock();
            log.debug("第二次locking....");*/
            try {
                log.debug("locking1....");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                log.debug("unlocking1....");
                myLock.unlock();
            }
        },"t1").start();

        new Thread(() -> {
            myLock.lock();
            try {
                log.debug("locking2....");
            } finally {
                log.debug("unlocking....");
                myLock.unlock();
            }
        },"t2").start();
    }
}

// 自定义锁（不可重入锁）
class MyLock implements Lock{

    // 独占锁 同步器类
    class MySync extends AbstractQueuedSynchronizer {
        @Override
        // 尝试获取锁
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0,1)) {
                // 加上了锁，并设置 owner 为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        // 尝试释放锁
        protected boolean tryRelease(int arg) {
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        @Override
        // 是否持有独占锁
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        public Condition newCondition(){
            return new ConditionObject();
        }
    }

    private MySync sync = new MySync();

    @Override
    // 加锁（不成功会进入等待队列）
    public void lock() {
        sync.acquire(1);
    }

    @Override
    // 加锁 （可打断锁）
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    // 尝试加锁 （一次）
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    // 尝试加锁，待超时
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    // 解锁
    public void unlock() {
        sync.release(1);
    }

    @Override
    // 创建条件变量
    public Condition newCondition() {
        return sync.newCondition();
    }
}
