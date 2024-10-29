import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/23 9:07
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestInterrupt
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestInterrupt")
public class TestInterrupt {

    public static void main(String[] args) throws InterruptedException {
//        test1();
//        test2();

        // 两阶段终止
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();

        Thread.sleep(3500);

        tpt.stop();


    }
    public static void test1() throws InterruptedException {
        Runnable t = () -> {
            log.debug("测试打断sleep线程状态");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread t1 = new Thread(t, "t1");
        t1.start();

        Thread.sleep(500);
        t1.interrupt();
        log.debug("打断状态：{}", t1.isInterrupted());
    }

    public static void test2() throws InterruptedException {
        Runnable t = () -> {
            log.debug("测试打断正常运行线程状态");
            while (true) {
                Thread current = Thread.currentThread();
                boolean interrupted = current.isInterrupted();
                if (interrupted) {
                    log.debug("打断状态：{}",interrupted);
                    break;
                }
            }
        };

        Thread t2 = new Thread(t, "t2");
        t2.start();

        Thread.sleep(500);
        t2.interrupt();
    }

}




    /**两阶段终止模式实现*/
    @Slf4j(topic = "c.TwoPhaseTermination")
    class TwoPhaseTermination {
        private Thread monitor;

        // 启动线程
        public void start() {
          monitor = new Thread(() -> {
                while (true) {
                    Thread current = Thread.currentThread();
                    if (current.isInterrupted()) {
                        log.debug("料理后事");
                        break;
                    }

                    try {
                        Thread.sleep(1000);
                        log.debug("执行监控记录");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        // current.interrupt() 如果没有，当sleep的线程被打断后，打断标记会被重新置为false
                        current.interrupt();
                    }
                }
          });

          monitor.start();
        }

        //停止监控线程
        public void stop(){
            monitor.interrupt();
        }

    }


