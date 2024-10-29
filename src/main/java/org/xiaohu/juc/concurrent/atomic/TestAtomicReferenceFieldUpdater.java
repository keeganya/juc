package org.xiaohu.juc.concurrent.atomic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @Author xiaohu
 * @Date 2024/10/24 16:11
 * @PackageName:org.xiaohu.juc.concurrent.atomic
 * @ClassName: TestAtomicReferenceFieldUpdater
 * @Description: 字段更新器
 * @Version 1.0
 */
@Slf4j(topic = "c.TestAtomicReferenceFieldUpdater")
public class TestAtomicReferenceFieldUpdater {
    private volatile int field;

    public static void main(String[] args) throws NoSuchFieldException {
        AtomicReferenceFieldUpdater<Student, String> stuNameUpdater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

        // AtomicIntegerFieldUpdater 指定字段类型（Integer）的更新器 不需要在传字段类型
        AtomicIntegerFieldUpdater<TestAtomicReferenceFieldUpdater> fieldUpdater =
                AtomicIntegerFieldUpdater.newUpdater(TestAtomicReferenceFieldUpdater.class, "field");

        Student stu = new Student();
        stuNameUpdater.compareAndSet(stu,null,"张三");
        System.out.println(stu);

        TestAtomicReferenceFieldUpdater testAtomicReferenceFieldUpdater = new TestAtomicReferenceFieldUpdater();
        fieldUpdater.compareAndSet(testAtomicReferenceFieldUpdater,0,5);
        System.out.println(testAtomicReferenceFieldUpdater.field);

    }

}

class Student{
    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}