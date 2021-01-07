package it.unical.computerscience.pfsociety.plasticfee.controller;

import it.unical.computerscience.pfsociety.plasticfee.data.CommunityExampleRepository;
import it.unical.computerscience.pfsociety.plasticfee.data.CustomerExampleRepository;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.ExampleProto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommunityExampleController {
    @Autowired
    CommunityExampleRepository communityExampleRepository;

    @Autowired
    CustomerExampleRepository customerExampleRepository;

    @RequestMapping("/communities/{id}")
    ExampleProto.Community community(@PathVariable Integer id) {
        return communityExampleRepository.getCommunity(id);
    }

    @RequestMapping("/customers/{id}")
    ExampleProto.Customer customer(@PathVariable Integer id) {
        return customerExampleRepository.getCustomer(id);
    }
}
