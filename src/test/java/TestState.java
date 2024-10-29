import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/23 10:20
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestState
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestState")
public class TestState {
    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            log.debug("runnig....");
        },"t1");

        Thread t2 = new Thread(() -> {
            while (true) {

            }
        },"t2");

        t2.start();

        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
                log.debug("running....");
            }
        };

        t3.start();

        Thread t4 = new Thread(() -> {
            synchronized (TestState.class) {
                try {
                    Thread.sleep(1000000000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t4");

        t4.start();

        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t5");

        t5.start();

        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
                synchronized (TestState.class){
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        t6.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.debug("t1 state {}",t1.getState());
        log.debug("t2 state {}",t2.getState());
        log.debug("t3 state {}",t3.getState());
        log.debug("t4 state {}",t4.getState());
        log.debug("t5 state {}",t5.getState());
        log.debug("t6 state {}",t6.getState());
    }

}
