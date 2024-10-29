import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/22 16:04
 * @PackageName:PACKAGE_NAME
 * @ClassName: test1
 * @Description: 继承Thread类开启线程
 * @Version 1.0
 */
@Slf4j(topic = "c.Test1")
public class Test1 extends Thread{
    @Override
    public void run() {
        log.debug("running");
    }

    public static void main(String[] args) {
        Test1 t1 = new Test1();

        Thread t2 = new Thread() {
            @Override
            public void run() {
                log.debug("匿名内部类开启线程running");
            }
        };

        t1.setName("t1");
        t1.start();

        t2.setName("t2");
        t2.start();


        log.debug("main线程running");
    }
}
