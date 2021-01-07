package it.unical.computerscience.pfsociety.plasticfee.data;

import it.unical.computerscience.pfsociety.plasticfee.protobuf.ExampleProto;

import java.util.Map;

public class CustomerExampleRepository {
    Map<Integer, ExampleProto.Customer> customers;

    public CustomerExampleRepository(Map<Integer, ExampleProto.Customer> customers) {
        this.customers = customers;
    }

    public ExampleProto.Customer getCustomer(int id) {
        return customers.get(id);
    }
}
