package org.example.bean;
import org.springframework.stereotype.Component;

@Component
public class School {
    private String name = "SWPU";
    public void sayname(){
        System.out.printf("my school is %s",name);
    }
}
