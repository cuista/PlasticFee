package it.unical.computerscience.pfsociety.plasticfee;

import it.unical.computerscience.pfsociety.plasticfee.data.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void testService(){

        userService.createNewUser("aaaa1");

    }

}
