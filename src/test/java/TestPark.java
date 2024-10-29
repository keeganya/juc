import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author xiaohu
 * @Date 2024/10/23 9:56
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestPark
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestPark")
public class TestPark {

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    public static void test1() throws InterruptedException {

        Thread t1 = new Thread(() -> {
            log.debug("park....");
            LockSupport.park();
            log.debug("unpark....");
            // isInterrupted 不会清除打断标记
//            log.debug("打断状态： {}", Thread.currentThread().isInterrupted());
            // interrupted 会清除打断标记
            log.debug("打断状态： {}",Thread.interrupted());

            LockSupport.park();
            log.debug("unpark....");
        },"t1");

        t1.start();

        Thread.sleep(1000);
        t1.interrupt();

    }

}
