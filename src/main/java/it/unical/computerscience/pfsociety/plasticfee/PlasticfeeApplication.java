package it.unical.computerscience.pfsociety.plasticfee;

import it.unical.computerscience.pfsociety.plasticfee.data.CommunityExampleRepository;
import it.unical.computerscience.pfsociety.plasticfee.data.CustomerExampleRepository;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.ExampleProto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PlasticfeeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlasticfeeApplication.class, args);
	}

	@Bean
	ProtobufHttpMessageConverter protobufHttpMessageConverter() {
		return new ProtobufHttpMessageConverter();
	}

	//FOR TESTING, TO DELETE WITH AppHttpClientTest
	@Bean
	RestTemplate restTemplate(ProtobufHttpMessageConverter hmc) {
		return new RestTemplate(Arrays.asList(hmc));
	}

	@Bean
	public CommunityExampleRepository createTestCommunities() {
		Map<Integer, ExampleProto.Community> communities = new HashMap<>();
		ExampleProto.Community community1 = ExampleProto.Community.newBuilder()
				.setId(1)
				.setCommunityName("REST with Spring")
				.addAllCustomer(/*createTestCustomers()*/new ArrayList<ExampleProto.Customer>())
				.build();
		ExampleProto.Community community2 = ExampleProto.Community.newBuilder()
				.setId(2)
				.setCommunityName("Learn Spring Security")
				.addAllCustomer(new ArrayList<ExampleProto.Customer>())
				.build();
		communities.put(community1.getId(), community1);
		communities.put(community2.getId(), community2);
		return new CommunityExampleRepository(communities);
	}

	@Bean
	public CustomerExampleRepository createTestStudents(){
		Map<Integer, ExampleProto.Customer> customers = new HashMap<>();
		ExampleProto.Customer customer1 = ExampleProto.Customer.newBuilder()
				.setId(1)
				.setFirstName("John")
				.setLastName("Doe")
				.setEmail("john.doe@baeldung.com")
				.build();
		ExampleProto.Customer customer2 = ExampleProto.Customer.newBuilder()
				.setId(2)
				.setFirstName("Richard")
				.setLastName("Roe")
				.setEmail("richard.roe@baeldung.com")
				.build();
		ExampleProto.Customer customer3 = ExampleProto.Customer.newBuilder()
				.setId(3)
				.setFirstName("Jane")
				.setLastName("Doe")
				.setEmail("jane.doe@baeldung.com")
				.build();
		customers.put(customer1.getId(), customer1);
		customers.put(customer2.getId(), customer2);
		customers.put(customer3.getId(), customer3);
		return new CustomerExampleRepository(customers);
	}

}
