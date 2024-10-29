import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/23 16:24
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestMultiLock
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestMultiLock")
public class TestMultiLock {

    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();
        new Thread(() -> {
            try {
                bigRoom.sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"小南").start();

        new Thread(()->{
            try {
                bigRoom.study();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"小女").start();
    }


}
@Slf4j(topic = "c.BigRoom")
class BigRoom{
    private static Object studyRoom = new Object();
    private static Object bedRoom = new Object();

    public void sleep() throws InterruptedException {
        synchronized (bedRoom){
            log.debug("sleep 2 hours");
            Thread.sleep(2000);
        }
    }

    public void study() throws InterruptedException {
        synchronized (studyRoom){
            log.debug("study 1 hours");
            Thread.sleep(1000);
        }
    }
}
