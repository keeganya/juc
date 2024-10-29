import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/22 17:18
 * @PackageName:PACKAGE_NAME
 * @ClassName: test2
 * @Description: 实现runable接口方式创建线程
 * @Version 1.0
 */
@Slf4j(topic = "c.test2")
public class Test2 implements Runnable{
    @Override
    public void run() {
        log.debug("实现Runable接口开启线程");
    }

    public static void main(String[] args) {
        Test2 test2 = new Test2();
        Thread t1 = new Thread(test2, "t1");
        t1.start();
        log.debug("主线程running");


        Runnable runnable = () -> {
            log.debug("lamabda开启线程");
        };
        Thread t2 = new Thread(runnable, "t2");
        t2.start();
    }
}
