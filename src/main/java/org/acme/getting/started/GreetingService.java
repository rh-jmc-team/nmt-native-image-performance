package org.acme.getting.started;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.concurrent.locks.LockSupport;
import sun.misc.Unsafe;
import java.lang.reflect.Field;

@ApplicationScoped
public class GreetingService {
    
    Unsafe unsafe;

    public GreetingService(){
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            this.unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            System.out.println("----------set up failed----------"+ e);
        }
    }

    public String greeting(String name) {
        return "hello " + name + "NMT TEST ";
    }
    
    private String getNextString(String text) throws InterruptedException {
        LockSupport.parkNanos(1);
        LockSupport.parkNanos(this,1);
        return Integer.toString(text.hashCode() % (int) (Math.random() * 100 + 1)); // +1 to avoid division by 0
    }
    
    public String work(String text) {
        String result = "";
        for (int i = 0; i < 1000; i++){
            try {
                long address = unsafe.allocateMemory(1024);
                result += getNextString(text);
                unsafe.freeMemory(address);
            } catch (Exception e) {
                System.out.println("----------work endpoint task failed----------"+ e);
            }
        }
        return result;
    }


    public String regular(String text) {
        long address = unsafe.allocateMemory(1024);
        String result = text;
        int count = (int) (Math.random() * (20)) + 10;

        String temp = Integer.toString(result.hashCode()).repeat(count);
        result = "";
        for (int j = 0; j < temp.length(); j += 2) {
            result += temp.charAt(j);
        }

        unsafe.freeMemory(address);
        return result;
    }
}
