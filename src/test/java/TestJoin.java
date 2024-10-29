import lombok.extern.slf4j.Slf4j;

import static java.lang.Thread.sleep;

/**
 * @Author xiaohu
 * @Date 2024/10/23 8:45
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestJoin
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestJoin")
public class TestJoin {
    static int r = 0;

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    public static void test1() throws InterruptedException {
        Runnable t1 = () -> {
            log.debug("开始");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("结束");
            r = 10;
        };

        Thread t = new Thread(t1, "t1");
        t.start();
        t.join();

        log.debug("结果为：{}",r);
        log.debug("结束");
    }


}
