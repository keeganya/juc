import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author xiaohu
 * @Date 2024/10/28 15:08
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestReadWriteLock
 * @Description: 读写锁 读锁-读锁可以并发；读锁-写锁相互阻塞（竞争）；写锁-写锁相互阻塞（竞争）；读锁不支持条件变量
 * @Version 1.0
 */
@Slf4j(topic = "c.TestReadWriteLock")
public class TestReadWriteLock {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

        new Thread(() -> {
            try {
                dataContainer.read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();

        new Thread(() -> {
            try {
                dataContainer.write();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();
    }
}

@Slf4j(topic = "c.DataContainer")
class DataContainer {
    private Object data;
    //读写锁对象
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    // 读锁
    private ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();
    // 写锁
    private ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();

    public Object read() throws InterruptedException {
        log.debug("获取读锁....");
        readLock.lock();
        try {
            log.debug("读取");
            Thread.sleep(1000);
            return data;
        } finally {
            log.debug("释放读锁");
            readLock.unlock();
        }
    }

    public void write() throws InterruptedException {
        log.debug("获取写锁....");
        writeLock.lock();
        try {
            log.debug("写入");
            Thread.sleep(1000);
        } finally {
            log.debug("释放写锁....");
            writeLock.unlock();
        }
    }
}
