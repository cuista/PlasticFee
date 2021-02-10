package it.unical.computerscience.pfsociety.plasticfee.server;

import io.grpc.stub.StreamObserver;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.HelloRequest;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.HelloResponse;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.HelloServiceGrpc;

public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void hello(
            HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

        System.out.println(request);

        String greeting = new StringBuilder()
                .append("Hello, ")
                .append(request.getFirstName())
                .append(" ")
                .append(request.getLastName())
                .toString();

        HelloResponse response = HelloResponse.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
