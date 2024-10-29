import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * @Author xiaohu
 * @Date 2024/10/28 17:13
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestSemaphore
 * @Description: semaphore 【信号量】 用来限制能同时访问共享资源的线程上限
 * @Version 1.0
 */
@Slf4j(topic = "c.TestSemaphore")
public class TestSemaphore {
    public static void main(String[] args) {
        // 1. 创建 semaphore 对象
        Semaphore semaphore = new Semaphore(3);

        // 2. 10 个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
            try {
                // 3. 获取许可
                semaphore.acquire();
                log.debug("running....");
                Thread.sleep(1000);
                log.debug("ending....");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // 4. 释放
                semaphore.release();
            }
            }).start();
        }
    }
}
