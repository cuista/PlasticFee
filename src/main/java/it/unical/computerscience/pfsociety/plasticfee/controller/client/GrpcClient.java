package it.unical.computerscience.pfsociety.plasticfee.controller.client;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.ProposalByTitleNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.*;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.ProposalServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.time.*;
import java.time.format.DateTimeFormatter;
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
    private static Scanner scanner = new Scanner(System.in);
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

        boolean is_exited = false;

        while(!is_exited) {

            System.out.println("1) Register");
            System.out.println("2) Login");
            System.out.println("3) Exit");
            System.out.print("Type the corresponding number to choose the option: ");
            String input = scanner.nextLine();

            if (input.equals("1")){
                client.register();

            }else if (input.equals("2")){
                client.login();

                while (client.loggedIn){
                    client.printOptions();
                }
            }else if (input.equals("3")){
                is_exited=true;
            }
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

    public void register(){

        String username, password;

        System.out.print("Insert username: ");
        username = scanner.nextLine().strip();

        System.out.print("\nInsert password: ");
        password = scanner.nextLine().strip();

        try {
            User user = stub.createUser(CreateUserRequest.newBuilder().setUsername(username).setPassword(password).build());
        }catch (StatusRuntimeException e){
            System.err.println("Username already in use");
            return;
        }

        System.out.println("\nRegistration correctly executed, you can now login with your credentials");
    }

    private void printHeaders(){
        System.out.printf("\n%-24s %-32s %-32s %-32s %s\n","PROPOSAL TITLE","PROPOSAL DESCRIPTION","CREATOR","VOTES IN FAVOR","VOTES AGAINST");
    }

    public void printActiveProposals(){

        printHeaders();

        List<Proposal> proposals = stub.retrieveAllActiveProposals(AllActiveProposalsRequest.newBuilder().build()).getProposalsList();

        prettyPrint(proposals);
    }


    private void createNewProposal(){

        String title,description, expDate, reputationReward;

        System.out.print("Insert title of new proposal: ");
        title = scanner.nextLine().strip();

        System.out.print("\n\nInsert a description: ");
        description = scanner.nextLine().strip();

        System.out.print("\n\nInsert expiration date (yyyy-MM-dd): ");
        expDate = scanner.nextLine().strip();

        LocalDate expirationDate=null;

        try {
            expirationDate = LocalDate.parse(expDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }catch (DateTimeException e){
            System.err.println("The specified date format was not correct");
            return;
        }

        System.out.println("\n\nInsert the eventual reputation reward for the voters: ");
        reputationReward = scanner.nextLine().strip();

        CreateProposalRequest request = CreateProposalRequest.newBuilder()
                .setTitle(title)
                .setDescription(description)
                .setCreatorUsername(loggedUsername)
                .setRewardReputation(Integer.parseInt(reputationReward))
                .setExpirationTimestamp(fromLocalDateToProto(expirationDate))
                .build();

        Proposal p = stub.createProposal(request);

        System.out.printf("New proposal with title %s correctly added \n", p.getTitle());

    }

    public void printOptions(){


        System.out.println("\n1) Create a new proposal ");
        System.out.println("2) Show active and expired proposals: ");
        System.out.println("3) Show all active proposals ");
        System.out.println("4) Show all expired proposals ");
        System.out.println("5) Show your proposals ");
        System.out.println("6) Vote for an active proposal ");
        System.out.println("7) Logout ");

        System.out.print("Type the corresponding number to pick one: ");

        String input = scanner.nextLine();

        switch (input){

            case "1":
                createNewProposal();
                break;
            case "2":
                printActiveAndExpiredProposals();
                break;
            case "3":
                printActiveProposals();
                break;
            case "4":
                printExpiredProposals();
                break;
            case "5":
                printPersonalProposals();
                break;
            case "6":
                voteProposal();
                break;
            case "7":
                this.logout();
                this.loggedUsername=null;
                this.loggedIn=false;
                break;
            default:
                System.err.println("Wrong input! ");
        }

    }

    private void printPersonalProposals() {

        printHeaders();

        List<Proposal> proposals = stub.retrievePersonalProposals(RetrievePersonalProposalsRequest.newBuilder()
                .setUsername(loggedUsername).build()).getProposalsList();

        prettyPrint(proposals);

        System.out.println("\nDo you wish to update the expiration date for one of your proposals? y/n");
        String input = scanner.nextLine().strip();

        while ((!input.equals("y")) && (!input.equals("n"))){
            System.out.println("Wrong input: please retype a correct one ");
            input= scanner.nextLine();
        }

        if (input.equals("y")){

            String title, expiration;

            System.out.print("Insert proposal title: ");
            title = scanner.nextLine();

            System.out.print("\nInsert the new expiration date: ");
            expiration = scanner.nextLine();

            LocalDate expirationDate=null;

            try {
                expirationDate = LocalDate.parse(expiration, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }catch (DateTimeException e){
                System.err.println("The specified date format was not correct");
                return;
            }

            try{

                ExpirationUpdateResponse response = stub.updateProposalExpirationDate(ExpirationUpdateRequest.newBuilder()
                        .setNewExpiration(fromLocalDateToProto(expirationDate)).setUsername(loggedUsername)
                        .setProposalTitle(title).build());

                System.out.println(response.getResponse());

            } catch(RuntimeException e){
                System.err.println("No active proposal found with this title\n");
            }

        }
    }

    private void printExpiredProposals() {

        printHeaders();

        List<Proposal> proposals = stub.retrieveAllExpiredProposals(RetrieveAllExpiredProposalsRequest.newBuilder().build()).getProposalsList();

        prettyPrint(proposals);
    }

    private void printActiveAndExpiredProposals() {

        printHeaders();

        List<Proposal> proposals = stub.retrieveAllProposals(RetrieveAllProposalsRequest.newBuilder().build()).getProposalsList();

        prettyPrint(proposals);
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
        System.out.println("\nlogout executed for " + loggedUsername +"\n");
    }

    private static Timestamp fromLocalDateToProto(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    private void prettyPrint(List<Proposal> proposals) {
        proposals.stream().forEach(p -> {List<Vote> l=new LinkedList<>();p.getVotesListList().forEach(e -> {if(e.getIsInFavor())l.add(e);});List<Vote> m=new LinkedList<>();p.getVotesListList().forEach(e -> {if(!e.getIsInFavor())m.add(e);});
            System.out.printf("%-24s %-32s %-32s %-32s %s\n",p.getTitle(),p.getDescription(),p.getCreatorUsername(),l.size(),m.size());});
    }



}
