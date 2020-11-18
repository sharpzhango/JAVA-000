package org.example.bean;

import org.springframework.stereotype.Component;

@Component
public class Student {
    private String name = "sharpzhango";
    public void sayHello(){
        System.out.printf("myName is %s\n",name);
    }
}
