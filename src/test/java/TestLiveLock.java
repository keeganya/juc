import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/23 17:09
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestLiveLock
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestLiveLock")
public class TestLiveLock {
    static volatile int count = 10;
    static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            while(count > 0){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
                log.debug("{}",count);
            }
        },"t1").start();

        new Thread(() -> {
            while (count < 20){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            log.debug("{}",count);
        },"t2").start();
    }
}
