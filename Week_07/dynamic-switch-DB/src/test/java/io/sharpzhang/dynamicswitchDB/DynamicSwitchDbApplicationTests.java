package io.sharpzhang.dynamicswitchDB;

import io.sharpzhang.dynamicswitchDB.bean.Person;
import io.sharpzhang.dynamicswitchDB.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DynamicSwitchDbApplicationTests {

	@Autowired
	private PersonService personService;

	@Test
	public void contextLoads() {
		Person person = new Person();
		person.setName("wangwu");
		person.setAge(18);
		personService.add(person);
	}
	@Test
	public void testQuery() {
		List<Person> all = personService.findAll();
		all.forEach(System.out::println);
	}
}
