package it.unical.computerscience.pfsociety.plasticfee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PlasticfeeApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(PlasticfeeApplication.class, args);


	}

	@Override
	public void run(String...args) throws Exception {

		ProposalExpirationChecker checker = new ProposalExpirationChecker();

		applicationContext.getAutowireCapableBeanFactory().autowireBean(checker);

		//checker.start();

	}


}
