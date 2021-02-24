package it.unical.computerscience.pfsociety.plasticfee.controller.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.*;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.ProposalServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@Component
public class GrpcClient {

    private static final String HOST="localhost";
    private static final int PORT=8070;

    private boolean loggedIn = false;
    private String loggedUsername;
    private ProposalServiceGrpc.ProposalServiceBlockingStub stub;
    private ManagedChannel channel;
    private Scanner scanner = new Scanner(System.in);
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcClient.class);

    public GrpcClient(){

    }

    private GrpcClient(ManagedChannel channel){
        this.channel = channel;
        stub = ProposalServiceGrpc.newBlockingStub(channel);
    }

    public GrpcClient(String host, int port){
        this(ManagedChannelBuilder.forAddress(host,port).usePlaintext().build());
    }

    public static void main(String[] args) {

        GrpcClient client = new GrpcClient(HOST,PORT);

        while(!client.loggedIn){
            client.login();
        }

        while(client.loggedIn){
            client.printProposals();
            client.printOptions();
        }

        client.channel.shutdown();
    }

    public boolean login(){

        String username, password;

        System.out.println("Insert your username: ");
        username = scanner.nextLine().strip();

        System.out.println("Insert your password: ");

        Console console = System.console();
        password = console==null?scanner.nextLine().strip():new String(console.readPassword()); // Hide password on real console

        LoginRequest loginRequest = LoginRequest.newBuilder().setUsername(username).setPassword(password).build();

        LoginResponse loginResponse;

        try{
            loginResponse = stub.login(loginRequest);
        }catch (StatusRuntimeException e){
            System.err.println("Access failed");
            return false;
        }
        System.out.printf("login with username %s executed \n", username);
        loggedIn = true;
        loggedUsername = username;
        return true;
    }

    private void printHeaders(){
        System.out.printf("\n%-24s %-32s %-32s %-32s %s\n","PROPOSAL TITLE","PROPOSAL DESCRIPTION","CREATOR","VOTES IN FAVOR","VOTES AGAINST");
    }

    public void printProposals(){

        printHeaders();

        List<Proposal> proposals = stub.retrieveAllActiveProposals(AllActiveProposalsRequest.newBuilder().build()).getProposalsList();

        proposals.stream().forEach(p -> {List<Vote> l=new LinkedList<>();p.getVotesListList().forEach(e -> {if(e.getIsInFavor())l.add(e);});List<Vote> m=new LinkedList<>();p.getVotesListList().forEach(e -> {if(!e.getIsInFavor())m.add(e);});
            System.out.printf("%-24s %-32s %-32s %-32s %s\n",p.getTitle(),p.getDescription(),p.getCreatorUsername(),l.size(),m.size());});
    }

    private void createNewProposal(){

        String title,description;

        System.out.println("Insert title of new proposal: ");
        title = scanner.nextLine().strip();

        System.out.println("\nInsert a description: ");
        description = scanner.nextLine().strip();

        CreateProposalRequest request = CreateProposalRequest.newBuilder().setTitle(title)
                .setDescription(description).setCreatorUsername(loggedUsername)
                .build();

        Proposal p = stub.createProposal(request);

        System.out.printf("New proposal with title %s correctly added \n", p.getTitle());

    }

    public void printOptions(){


        System.out.println("\n1) Insert a new proposal ");
        System.out.println("2) Show all proposals ");
        System.out.println("3) Vote for an active proposal ");
        System.out.println("4) Logout ");

        System.out.print("Type the corresponding number to pick one: ");

        String input = scanner.nextLine();

        switch (input){

            case "1":
                createNewProposal();
                break;
            case "2":
                printProposals();
                break;
            case "3":
                voteProposal();
                break;
            case "4":
                this.logout();
                this.loggedUsername=null;
                this.loggedIn=false;
                break;
            default:
                System.err.println("Wrong input! ");
                printOptions();
        }

    }

    public void voteProposal() {

        String title;

        System.out.print("Insert title of active proposal: ");
        title = scanner.nextLine().strip();

        System.out.println("\n1) Vote in favor ");
        System.out.println("2) Vote against ");
        System.out.println("3) Back on menu ");

        String input = scanner.nextLine();

        switch (input) {
            case "1":
                stub.voteActiveProposal(VoteActiveProposalRequest.newBuilder().setTitle(title).setUsername(loggedUsername).setIsInFavor(true).build());
                break;
            case "2":
                stub.voteActiveProposal(VoteActiveProposalRequest.newBuilder().setTitle(title).setUsername(loggedUsername).setIsInFavor(false).build());
                break;
            case "3":
                break;
            default:
                System.err.println("Wrong input! ");
                voteProposal();
        }



    }

    public void logout(){
        LogoutResponse response = stub.logout(LogoutRequest.newBuilder().setUsername(loggedUsername).build());
        System.out.println("logout executed for " + loggedUsername);
    }
}
