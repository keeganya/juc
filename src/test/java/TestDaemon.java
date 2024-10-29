import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/23 10:07
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestDaemon
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestDaemon")
public class TestDaemon {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                log.debug("守护线程开始");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.debug("守护线程结束");
        },"t1");

        t1.setDaemon(true);
        t1.start();

        Thread.sleep(1000);
        log.debug("结束");
    }
}
