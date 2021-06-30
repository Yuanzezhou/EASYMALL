package com.harbin.mymallsearch.thread;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author Yuanzz
 * @creat 2021-03-03-10:30
 */

/**
 * 更多相关的见pdf：
 * 1、两任务都完成后执行第三个任务：runAfterBoth、thenAcceptBoth、thenCombine
 * 2、两任务完成一个后执行第三个任务：runAfterEither、acceptEither、applyToEither
 * 3、多任务组合：allOf、anyOf
 */
public class CompletableTest {

//    public static void main(String[] args) {
//        ExecutorService executors = Executors.newFixedThreadPool(10);
//        System.out.println("main。。。start...");
//        CompletableFuture.runAsync(()->{
//            System.out.println("当前线程："+Thread.currentThread().getId());
//            int i = 10/2;
//            System.out.println("结果为："+i);
//        },executors);
//        System.out.println("main....end.....");
//    }

//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ExecutorService executors = Executors.newFixedThreadPool(10);
//        System.out.println("main。。。start...");
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getName());
//            int i = 10 / 0;
//            return i;
//        }, executors).whenCompleteAsync((res,throwable)->{          //此处不加aysnc就是main线程执行里面的代码
//            System.out.println("当前线程1：" + Thread.currentThread().getName());
//            System.out.println("结果=="+res+",异常是："+throwable);
//        },executors).exceptionally((throwable)->{
//            //感知到异常后，返回结果为100.
//            return 100;
//        });
//        Integer integer = future.get();
//        System.out.println("结果为："+integer);     //结果为：100
//        System.out.println("main....end.....");
//    }

//    public static void main(String[] args) throws Exception {
//        System.out.println("本方法所用线程 " + Thread.currentThread().getName());
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);
//        //一个5秒任务
//        Supplier<String> supplier = () -> {
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            String s = "supplier " + Thread.currentThread().getName();
//            System.out.println(s);
//            return s;
//        };
//        //任务完成后的行为
//        BiConsumer<Object, Throwable> action = (result, exception) -> {
//            String s = "action " + Thread.currentThread().getName();
//            System.out.println(s);
//        };
//
//        //将任务交给线程池处理,任务结束会自动调用CompletableFuture.complete()方法。
//        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(supplier, threadPool);
//        //睡1秒，这时任务还没结束，这时调用whenComplete方法，将会和supplier的执行使用相同的线程。
//        Thread.sleep(1);
//        CompletableFuture future2 = future1.whenComplete(action);
//
//        //睡10秒，这时任务已结束，这时调用whenComplete方法。只能使用调用本方法的线程
//        Thread.sleep(10);
//        CompletableFuture future3 = future1.whenComplete(action);
//    }


//    public static void main(String[] args) throws Exception {
//        System.out.println("本方法所用线程 " + Thread.currentThread().getName());
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);
//        //一个5秒任务
//        Supplier<String> supplier = () -> {
//            try {
//                Thread.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            String s = "supplier " + Thread.currentThread().getName();
//            System.out.println(s);
//            return s;
//        };
//        //任务完成后的行为
//        BiConsumer<Object, Throwable> action = (result, exception) -> {
//            String s = "action " + Thread.currentThread().getName();
//            System.out.println(s);
//        };
//
//        //将任务交给线程池处理
//        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(supplier, threadPool);
//        //睡1秒，使用默认线程池
//        Thread.sleep(1);
//        CompletableFuture future2 = future1.whenCompleteAsync(action);
//
//        //睡10秒,whenCompleteAsync可以指定执行的线程池
//        Thread.sleep(10);
//        CompletableFuture future3 = future1.whenCompleteAsync(action,threadPool);
//    }

//    public static void main(String[] args) {
//        CompletableFuture<Void> ha = CompletableFuture.runAsync(() -> {
//            System.out.println("hahah");
//            for (int i = 0; i < 1000000; i++) {
//                System.out.print("");
//            }
//        });
//
//        CompletableFuture<Void> lo = CompletableFuture.runAsync(() -> {
//            System.out.println("loloolo");
//            for (int i = 0; i < 1000000; i++) {
//                System.out.print("");
//            }
//        });
//
//        ha.runAfterBoth(lo,()->{
//            System.out.println("gigiig");
//        });
//
//    }

}
