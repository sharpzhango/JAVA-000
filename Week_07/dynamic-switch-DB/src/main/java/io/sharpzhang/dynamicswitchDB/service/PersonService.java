package io.sharpzhang.dynamicswitchDB.service;

import io.sharpzhang.dynamicswitchDB.bean.Person;
import io.sharpzhang.dynamicswitchDB.config.Read;
import io.sharpzhang.dynamicswitchDB.config.Write;
import io.sharpzhang.dynamicswitchDB.dao.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonMapper personMapper;
    /**
     * 代表该方法对数据库的操作是一个写操作
     * @param person
     */
    @Write
    public void add(Person person) {
        personMapper.insert(person);
    }


    /**
     * 代表该方法对数据库的操作是一个读操作
     */
    @Read
    public List<Person> findAll() {
        return personMapper.selectAll();
    }
}
