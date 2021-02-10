package it.unical.computerscience.pfsociety.plasticfee.core.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.chat.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GprcClient {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GprcClient.class);

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8070)
                .usePlaintext()
                .build();

        ChatServiceGrpc.ChatServiceBlockingStub stub
                = ChatServiceGrpc.newBlockingStub(channel);

        User user1 = stub.createUser(CreateUserRequest.newBuilder().setUsername("ElonMusk123").setPassword("Tesla").build());
        Message messageResponse = stub.message(user1);

        //User user2 = stub.getUser(GetUserRequest.newBuilder().setId(20L).build());

        LOGGER.info("grpc server responded id:{} content:{} timestamp:{}",messageResponse.getId(),messageResponse.getContent(),messageResponse.getTimestamp());

        channel.shutdown();
    }
}
