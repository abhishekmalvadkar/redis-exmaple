package com.rushi;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class Calculator {

    @Cacheable("number")
    public int doubleIt(int number){
        System.out.println("call doiubleIt");
        return number* 2; // heavy logic
    }

    @CacheEvict("number")
    public void evict(int number){}
}
