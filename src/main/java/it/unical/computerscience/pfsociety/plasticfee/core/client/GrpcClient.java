package it.unical.computerscience.pfsociety.plasticfee.core.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.chat.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import java.util.Scanner;

public class GrpcClient {

    private static final String HOST="localhost";
    private static final int PORT=8070;

    private String userAccessToken = "";
    private boolean loggedIn = false;
    private String loggedUsername;
    private ChatServiceGrpc.ChatServiceBlockingStub stub;
    private final ManagedChannel channel;
    private Scanner s= new Scanner(System.in);
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);
    private StreamObserver<ChatRequest> chat;


    private GrpcClient(ManagedChannel channel){
        this.channel = channel;
        stub = ChatServiceGrpc.newBlockingStub(channel);
    }

    public GrpcClient(String host, int port){
        this(ManagedChannelBuilder.forAddress(host,port).usePlaintext().build());
    }

    public boolean login(){

        String username, password;

        System.out.println("Insert your username: ");
        username = s.nextLine().strip();

        System.out.println("Insert your password: ");
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
        loggedUsername = username;
        startReceive();
        return true;
    }

    private void startReceive(){

        chat = ChatServiceGrpc.newStub(this.channel).chat(new StreamObserver<ChatResponse>() {
            @Override
            public void onNext(ChatResponse chatResponse) {
                switch (chatResponse.getEventCase()){
                    case LOGIN_EVENT:
                    {
                        LOGGER.info("user {}: login!!",chatResponse.getLoginEvent().getName());
                    }
                    break;
                    case MESSAGE_EVENT:
                    {
                        LOGGER.info("user {}:{}",chatResponse.getMessageEvent().getName(),
                                chatResponse.getMessageEvent().getMessage());
                    }
                    break;
                    case EVENT_NOT_SET:
                    {
                        LOGGER.error("receive event error: ", chatResponse);
                    }
                    break;
                    case LOGOUT_EVENT:
                    {
                        LOGGER.info("A user just logged out");
                    }
                    case PROPOSAL_EVENT:
                    {
                        LOGGER.info("New proposal added from {} : {}",chatResponse.getProposalEvent().getUsername(),
                                chatResponse.getProposalEvent().getTitle());
                    }
                    break;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                LOGGER.info("ERROR IN START RECEIVE METHOD");
            }

            @Override
            public void onCompleted() {
                LOGGER.info("ON COMPLETED INVOKED IN START RECEIVE");
            }
        });
    }

    private void printHeaders(){
        System.out.printf("\n%-24s %-32s %s\n","PROPOSAL TITLE","PROPOSAL DESCRIPTION","CREATOR");


    }

    public void printProposals(){

        printHeaders();

        List<Proposal> proposals = stub.retrieveAllProposals(AllProposalsRequest.newBuilder().build()).getProposalsList();

        proposals.stream().forEach(p ->
                System.out.printf("%-24s %-32s %s\n",p.getTitle(),p.getDescription(),p.getCreatorUsername()));
    }

    private void createNewProposal(){

        String title,description;

        System.out.print("Insert title: ");
        title = s.nextLine().strip();

        System.out.print("\nInsert description: ");
        description = s.nextLine().strip();

        CreateProposalRequest request = CreateProposalRequest.newBuilder().setTitle(title)
                .setDescription(description).setCreatorUsername(loggedUsername).build();

        Proposal p = stub.createProposal(request);
    }

    public void printOptions(){


        System.out.println("\n1) Insert a new proposal");
        System.out.println("2) Vote for a proposal");
        System.out.println("3) Send a message in general board");
        System.out.println("4) Logout");

        System.out.print("Type the corrisponding number to pick one: ");

        String input = s.nextLine();

        switch (input){

            case "1":
                createNewProposal();
                break;
            case "2": //TODO aggiungere opzione per votare
                break;
            case "3":
                this.chat.onCompleted();
                this.logout();
                this.loggedUsername=null;
                this.loggedIn=false;
                break;
        }
    }

    public void sendMessage(String msg){

        if (msg.equals("exit")){
            this.chat.onCompleted();
            this.logout();
            this.loggedUsername = null;
            this.loggedIn = false;
        }

        else if (this.chat!=null){
            this.chat.onNext(ChatRequest.newBuilder().setUsername(this.loggedUsername).setMessage(msg).build());
        }
    }

    public void logout(){
        LogoutResponse response = stub.logout(LogoutRequest.newBuilder().setUsername(loggedUsername).build());
        LOGGER.info("logout executed for {}" , loggedUsername);
    }

    public static void main(String[] args) {

        GrpcClient client = new GrpcClient(HOST,PORT);

        while(!client.loggedIn){
            client.login();
        }

        String msg;

        while(client.loggedIn){
            //msg=client.s.nextLine();
            //client.sendMessage(msg);
            client.printProposals();
            client.printOptions();
        }

        client.channel.shutdown();
    }
}
