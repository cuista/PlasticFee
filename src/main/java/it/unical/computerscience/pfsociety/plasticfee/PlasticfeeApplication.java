package it.unical.computerscience.pfsociety.plasticfee;

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

}
