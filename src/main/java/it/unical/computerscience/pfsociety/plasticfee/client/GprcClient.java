package it.unical.computerscience.pfsociety.plasticfee.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.HelloRequest;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.HelloResponse;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.HelloServiceGrpc;

public class GprcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub stub
                = HelloServiceGrpc.newBlockingStub(channel);

        HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
                .setFirstName("PlasticFee")
                .setLastName("with gRPC")
                .build());

        System.out.println(helloResponse);

        channel.shutdown();
    }
}
