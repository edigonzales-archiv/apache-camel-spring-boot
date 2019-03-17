package ch.so.agi.apachecamel;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApacheCamelApplicationTests {

    @Ignore("Problems with variables which are injected from properties file.")
	@Test
	public void contextLoads() {
	}

}
