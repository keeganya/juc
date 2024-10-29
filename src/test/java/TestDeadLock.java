import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author xiaohu
 * @Date 2024/10/23 16:33
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestDeadLock
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestDeadLock")
public class TestDeadLock {


    public static void main(String[] args) {
        // 模拟死锁的情况
       /* Object A = new Object();
        Object B = new Object();
        new Thread(() -> {
            synchronized (A) {
                log.debug("lock A");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B){
                    log.debug("lock B");
                    log.debug("执行业务操作");
                }
            }
        },"t1").start();

        new Thread(() -> {
            synchronized (B) {
                log.debug("lock B");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A){
                    log.debug("lock A");
                    log.debug("执行业务操作");
                }
            }
        },"t2").start();*/

        // 哲学家就餐死锁问题
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");

        new Philosopher("苏格拉底",c1,c2).start();
        new Philosopher("柏拉图",c2,c3).start();
        new Philosopher("亚里士多德",c3,c4).start();
        new Philosopher("赫拉克利特",c4,c5).start();
        new Philosopher("阿基米德",c5,c1).start();
    }
}

@Slf4j(topic = "c.Philosopher")
class Philosopher extends Thread{
    Chopstick left;
    Chopstick right;
    public Philosopher(String name, Chopstick left,Chopstick right){
        super(name);
        this.left = left;
        this.right = right;
    }

    public void eat() throws InterruptedException {
        log.debug("eating ....");
        Thread.sleep(1000);
    }

    /*@Override
    public void run() {
        while (true) {
            // 获取左手的筷子
            synchronized (left){
                synchronized (right){
                    try {
                        eat();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 放下右手的筷子
            }
            // 放下左手的筷子
        }
    }*/

    @Override
    public void run() {
        while(true) {
            // 获取左手筷子
            if (left.tryLock()) {
                try {
                    // 获取右手的筷子
                    if (right.tryLock()) {
                        try {
                            try {
                                eat();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } finally {
                            right.unlock();
                        }
                    }
                } finally {
                    left.unlock();
                }
            }
        }
    }
}

class Chopstick extends ReentrantLock {
    String name;

    public Chopstick(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}
