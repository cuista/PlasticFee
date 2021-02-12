package it.unical.computerscience.pfsociety.plasticfee.core.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.chat.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class GprcClient {

    public static final Metadata.Key<String> HEADER_ROLE = Metadata.Key.of("role_name", Metadata.ASCII_STRING_MARSHALLER);

    private String userAccessToken = "";
    private boolean loggedIn = false;
    private ChatServiceGrpc.ChatServiceBlockingStub stub;
    private final ManagedChannel channel;
    private Scanner s= new Scanner(System.in);
    private static final Logger LOGGER = LoggerFactory.getLogger(GprcClient.class);
    private StreamObserver<ChatRequest> chat;

    public GprcClient(String host, int port){
        this(ManagedChannelBuilder.forAddress(host,port).usePlaintext().build());
    }

    private GprcClient(ManagedChannel channel){
        this.channel = channel;
        stub = ChatServiceGrpc.newBlockingStub(channel);
    }

    public boolean login(){

        String username, password;

        System.out.println("Inserisci il tuo username: ");
        username = s.nextLine().strip();

        System.out.println("Inserisci la tua password: ");
        password = s.nextLine().strip();

        LoginRequest loginRequest = LoginRequest.newBuilder().setUsername(username).setPassword(password).build();

        LoginResponse loginResponse;

        try{
            loginResponse = stub.login(loginRequest);
        }catch (StatusRuntimeException e){
            LOGGER.error("access failed");
            return false;
        }
        LOGGER.info("login with username {} executed",username);
        userAccessToken= loginResponse.getToken();
        loggedIn = true;
        //startReceive();
        return true;
    }


    public static void main(String[] args) {

        GprcClient client = new GprcClient("localhost",8070);

        while(!client.loggedIn){
            client.login();
        }

        String msg = client.s.nextLine();

        while(!msg.equals("esco")){
            msg=client.s.nextLine();
        }

        client.channel.shutdown();
    }
}
