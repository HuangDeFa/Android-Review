package com.kenzz.reviewapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    //Java 多线程，线程池
   @Test public void ThreadTest(){
        //基本写法
       Thread t=new Thread(){
           @Override
           public void run() {
              System.out.println("Thread 1");
           }
       };
       t.start();

       new Thread(new Runnable() {
           @Override
           public void run() {
               System.out.println("Thread 1");
           }
       }).start();

       //线程池
       ExecutorService executorService = Executors.newFixedThreadPool(10);
       //带返回值
       Future<String> submit = executorService.submit(new Callable<String>() {
           @Override
           public String call() throws Exception {
               return "execute call 1";
           }
       });
       while (!submit.isDone()){
           System.out.println("wait...");
       }
       try {
           System.out.println(submit.get());
       } catch (InterruptedException e) {
           e.printStackTrace();
       } catch (ExecutionException e) {
           e.printStackTrace();
       }

       //没有返回值
       executorService.execute(new Runnable() {
           @Override
           public void run() {
               System.out.println("execute runnable 1");
           }
       });

       //Java中的集合
       List<String> list=new ArrayList<>();
       list = Collections.synchronizedList(list);
   }
}