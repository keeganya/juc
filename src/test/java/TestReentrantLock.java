import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author xiaohu
 * @Date 2024/10/23 17:23
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestReentrantLock
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestReentrantLock")
public class TestReentrantLock {
    static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // ReentrantLock 锁可重入
        method1();
    }

    public static void method1(){
        lock.lock();
        try {
            log.debug("execute method1");
            method2(); // main 线程进入method2后，仍然可以获取锁
        } finally {
            lock.unlock();
        }
    }

    private static void method2() {
        lock.lock(); // main 线程进入后可以获取锁
        try {
            log.debug("execute method2");
            method3();
        } finally {
            lock.unlock();
        }
    }

    private static void method3() {
        lock.lock();
        try {
            log.debug("execute method3");
        } finally {
            lock.unlock();
        }
    }
}
