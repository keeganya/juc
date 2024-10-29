import lombok.extern.slf4j.Slf4j;

/**
 * @Author xiaohu
 * @Date 2024/10/23 15:28
 * @PackageName:PACKAGE_NAME
 * @ClassName: TestCorrectPostureStep1
 * @Description: TODO
 * @Version 1.0
 */
@Slf4j(topic = "c.TestCorrectPostureStep1")
public class TestCorrectPostureStep1 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeOut = false;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            synchronized (room) {
                log.debug("有烟没？[{}]", hasCigarette);
                while (!hasCigarette) {
                    log.debug("没烟，先歇会！");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("可以开始干活了");
                } else {
                    log.debug("没干成活");
                }
            }
        },"小楠").start();

        new Thread(() -> {
            synchronized (room){
                log.debug("外卖到了吗？[{}]",hasTakeOut);
                while (!hasTakeOut){
                    log.debug("没外卖，先歇会");
                    try {
                        room.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("外卖到了吗？[{}]",hasTakeOut);
                if (hasTakeOut){
                    log.debug("外卖到了，可以开始干活了");
                } else {
                    log.debug("没干成活");
                }
            }
        },"小女").start();

        /*for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room){
                    log.debug("可以开始干活了");
                }
            },"其他人").start();
        }*/

        Thread.sleep(1000);

        new Thread(()->{
            synchronized (room) {
                hasTakeOut = true;
                log.debug("外卖到了！");
                room.notifyAll();
            }
        },"送外卖的").start();
    }
}
