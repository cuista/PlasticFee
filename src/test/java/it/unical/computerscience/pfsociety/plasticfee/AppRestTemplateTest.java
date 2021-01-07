package it.unical.computerscience.pfsociety.plasticfee;

import it.unical.computerscience.pfsociety.plasticfee.protobuf.ExampleProto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppRestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;

    private static final String COMMUNITY1_URL = "http://localhost:8080/communities/1";
    private static final String CUSTOMER1_URL = "http://localhost:8080/customers/1";

    //FIXME
    private void assertResponse(String response) {
        /*
        assertThat(response, containsString("id"));
        assertThat(response, containsString("course_name"));
        assertThat(response, containsString("REST with Spring"));
        assertThat(response, containsString("student"));
        assertThat(response, containsString("first_name"));
        assertThat(response, containsString("last_name"));
        assertThat(response, containsString("email"));
        assertThat(response, containsString("john.doe@baeldung.com"));
        assertThat(response, containsString("richard.roe@baeldung.com"));
        assertThat(response, containsString("jane.doe@baeldung.com"));
        assertThat(response, containsString("phone"));
        assertThat(response, containsString("number"));
        assertThat(response, containsString("type"));
        */
        System.out.println(response); //DEBUG
    }

    @Test
    public void whenUsingRestTemplate_thenSucceed() {
        ResponseEntity<ExampleProto.Community> community = restTemplate.getForEntity(COMMUNITY1_URL, ExampleProto.Community.class);
        assertResponse(community.toString());

        ResponseEntity<ExampleProto.Customer> customer = restTemplate.getForEntity(CUSTOMER1_URL, ExampleProto.Customer.class);
        assertResponse(customer.toString());

        assert (true); //FIXME
    }

}
