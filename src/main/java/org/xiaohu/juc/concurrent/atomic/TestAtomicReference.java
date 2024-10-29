package org.xiaohu.juc.concurrent.atomic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author xiaohu
 * @Date 2024/10/24 14:22
 * @PackageName:org.xiaohu.juc.concurrent.atomic
 * @ClassName: TestAtomicReference
 * @Description: 原子引用
 * @Version 1.0
 */
public class TestAtomicReference {
    public static void main(String[] args) {
        DecimalAccount.demo(new DecimalAccountCas(new BigDecimal(10000)));
    }
}

class DecimalAccountCas implements DecimalAccount {

    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCas(BigDecimal balance){
//        this.balance = balance; //报错提示 Required type: AtomicReference <java.math.BigDecimal>   Provided: BigDecimal
        this.balance = new AtomicReference<>(balance);
    }
    @Override
    public BigDecimal getBanlance() {
        return balance.get();
    }

    @Override
    public void withDraw(BigDecimal amount) {
        while(true){
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);
            if (balance.compareAndSet(prev,next)) {
                break;
            }
        }
    }
}

interface DecimalAccount {
    // 获取余额
    BigDecimal getBanlance();
    void withDraw(BigDecimal amount);

    /**
     * 方法内会启动 1000个线程，每个线程做 -10 元的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     * */
    static void demo(DecimalAccount account){
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                account.withDraw(BigDecimal.TEN);
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        });
        System.out.println(account.getBanlance());
    }
}
