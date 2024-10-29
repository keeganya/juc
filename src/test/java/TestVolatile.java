import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/24 9:30
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestVolatile
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestVolatile")
public class TestVolatile {
    /** volatile 可以用来修饰【成员变量】和【静态成员变量】
     * 可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取值
     * 线程操作 volatile 变量都是直接操作主存
     * */
    // 保证可见性
    volatile static boolean run = true;

    /*
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (run){

            }
        },"t").start();

        Thread.sleep(1000);
        log.debug("停止t线程");
        run = false;

    }
    */

/*
    // synchronized 也可以保证可见性 既可以保证【原子性】 也可以保证 【可见性】 缺点： 性能比较低
    static boolean run = true;
    final static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (true){
                synchronized (lock){
                    if (!run) {
                        break;
                    }
                }
            }
        },"t").start();

        Thread.sleep(1000);
        log.debug("停止t线程");
        synchronized (lock){
            run = false;
        }
    }
    */


    /** volatile 两阶段终止模式实现*/
    private Thread monitor;
    private volatile boolean stop = false;


    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                if (stop) {
                    log.debug("料理后事");
                    break;
                }

                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        monitor.start();
    }

    public void stop() {
        stop = true;
        monitor.interrupt();// 打断正在睡眠的monitor线程 不再需要等他睡眠结束
    }

    public static void main(String[] args) {

        TestVolatile testVolatile = new TestVolatile();
        testVolatile.start();

        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        testVolatile.stop();
    }

}
