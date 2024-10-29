import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author xiaohu
 * @Date 2024/10/23 16:10
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestParkUnPark
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestParkUnPark")
public class TestParkUnPark {
  /*  public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
            log.debug("start...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("park....");
            LockSupport.park();
            log.debug("resume....");
        },"t1");
        t1.start();
        log.debug("{}",t1.getState());


        Thread.sleep(2000);
        log.debug("unpark....");
        LockSupport.unpark(t1);
    }*/

    /**park-unpark 实现顺序打印
     * 必须先打印 2 再打印 1*/
    /*public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.debug("1");
        }, "t1");

        t1.start();

        new Thread(() -> {
            log.debug("2");
            LockSupport.unpark(t1);
        },"t2").start();
    }*/

}

    /**park-unpark交替输出
     * 线程1输出a 5次；
     * 线程2输出b 5次
     * 线程3输出c 5次
     * 最终输出abcabcabcabcabc*/
    class SyncPark{
        private int loopNumber;
        private Thread[] threads;

        public SyncPark(int loopNumber){
            this.loopNumber = loopNumber;
        }

        public void setThreads(Thread... threads){
            this.threads = threads;
        }

        public Thread nextThread(){
            Thread current = Thread.currentThread();
            int index = 0;
            for (int i = 0; i < threads.length; i++) {
                if (threads[i] == current) {
                    index = i;
                    break;
                }
            }
            if (index < threads.length - 1) {
                return threads[index + 1];
            } else {
                return threads[0];
            }
        }

        public void print(String str){
            for (int i = 0; i < loopNumber; i++) {
                LockSupport.park();
                System.out.print(str);
                LockSupport.unpark(nextThread());
            }
        }

        public void start() {
            for (Thread thread : threads) {
                thread.start();
            }
            LockSupport.unpark(threads[0]);
        }

        public static void main(String[] args) {
            SyncPark syncPark = new SyncPark(5);

            Thread t1 = new Thread(() -> {
                syncPark.print("a");
            }, "t1");

            Thread t2 = new Thread(() -> {
                syncPark.print("b");
            }, "t2");

            Thread t3 = new Thread(() -> {
                syncPark.print("c");
            }, "t3");

            syncPark.setThreads(t1,t2,t3);
            syncPark.start();
        }
    }
