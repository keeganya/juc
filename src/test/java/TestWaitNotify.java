import ch.qos.logback.core.helpers.ThrowableToStringArray;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * @Author xiaohu
 * @Date 2024/10/23 15:11
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestWaitNotify
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestWaitNotify")
public class TestWaitNotify {
    /*
    static Object obj = new Object();
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            log.debug("执行");
            synchronized (obj){
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("其他代码");
        },"t1");

        Thread t2 = new Thread(() -> {
            log.debug("执行");
            synchronized (obj) {
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("其他代码");
        },"t2");

        t1.start();
        t2.start();

        Thread.sleep(1000);
        log.debug("唤醒obj上的其他线程");
        synchronized (obj){
            obj.notify();
        }
    }*/

    /**wait-notify实现顺序打印
     * 必须先打印 2 再打印 1 */
    /*static Object obj = new Object();
    static boolean t2Runned = false;

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (obj) {
                while (!t2Runned) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("1");
        }, "t1");

        t1.start();

        new Thread(()-> {
            synchronized (obj) {
                log.debug("2");
                t2Runned = true;
                obj.notify();
            }
        },"t2").start();
    }*/

    /**wait-notify交替输出
     * 线程1输出a 5次；
     * 线程2输出b 5次
     * 线程3输出c 5次
     * 最终输出abcabcabcabcabc*/

}

class SyncWaitNotify{
    private int flag;
    private int loopNumber;
    public SyncWaitNotify(int flag, int loopNumber){
        this.flag = flag;
        this.loopNumber = loopNumber;
    }


    /** waitFlag    nextFlag       str
     *     1            2           a
     *     2            3           b
     *     3            1           c
     * */
    public void print(int waitFlag,int nextFlag, String str) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while (this.flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        SyncWaitNotify syncWaitNotify = new SyncWaitNotify(1, 5);
        new Thread(() -> {
            syncWaitNotify.print(1,2,"a");
        }).start();

        new Thread(() -> {
            syncWaitNotify.print(2,3,"b");
        }).start();

        new Thread(() -> {
            syncWaitNotify.print(3,1,"c");
        }).start();
    }
}
