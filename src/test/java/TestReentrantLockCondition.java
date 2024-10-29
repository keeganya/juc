import ch.qos.logback.core.helpers.ThrowableToStringArray;
import lombok.extern.slf4j.Slf4j;

import javax.swing.plaf.TableHeaderUI;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.TestReentrantLockCondition")
public class TestReentrantLockCondition {

    static ReentrantLock lock = new ReentrantLock();
    static Condition waitCigaretteQueue = lock.newCondition();
    static Condition waitBreakfastQueue = lock.newCondition();
    static volatile boolean hasCigrette = false;
    static volatile boolean hasTakeOut = false;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            try {
                lock.lock();
                while (!hasCigrette) {
                    try {
                        waitCigaretteQueue.await(); // await执行后会释放锁
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("等到烟了");
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            try {
                lock.lock();
                while (!hasTakeOut) {
                    try {
                        waitBreakfastQueue.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("等到早餐了");
            } finally {
                lock.unlock();
            }
        },"t2").start();

        Thread.sleep(1000);
        sendBreakfast();
        Thread.sleep(1000);
        sendCigarette();

    }

    private static void sendCigarette(){
        try {
            lock.lock();
            log.debug("送烟来了");
            hasCigrette = true;
            waitCigaretteQueue.signal();
        } finally {
            lock.unlock();
        }
    }

    private static void sendBreakfast(){
        try {
            lock.lock();
            log.debug("送早餐来了");
            hasTakeOut = true;
            waitBreakfastQueue.signal();
        } finally {
            lock.unlock();
        }
    }
}

/**ReentrantLock 条件变量交替输出
 * 线程1输出a 5次；
 * 线程2输出b 5次
 * 线程3输出c 5次
 * 最终输出abcabcabcabcabc*/
/*
@Slf4j(topic = "c.AwaitSignal")
class AwaitSignal extends ReentrantLock{
    // 循环次数
    private int loopNumber;
    public AwaitSignal(int loopNumber){
        this.loopNumber = loopNumber;
    }

    public void start(Condition first){
        this.lock();
        try {
            log.debug("start");
            first.signal();
        } finally {
            this.unlock();
        }
    }

    public void print(String str,Condition current,Condition next){
        for (int i = 0; i < loopNumber; i++) {
            this.lock();
            try {
                current.await();
                log.debug(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                this.unlock();
            }
        }
    }


    public static void main(String[] args) {
        AwaitSignal as = new AwaitSignal(5);
        Condition aWaitSet = as.newCondition();
        Condition bWaitSet = as.newCondition();
        Condition cWaitSet = as.newCondition();

        new Thread(() -> {
            as.print("a",aWaitSet,bWaitSet);
        }).start();

        new Thread(() -> {
            as.print("b",bWaitSet,cWaitSet);
        }).start();

        new Thread(() -> {
            as.print("c",cWaitSet,aWaitSet);
        }).start();

        as.start(aWaitSet);
    }
}*/
