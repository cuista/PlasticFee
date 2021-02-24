package it.unical.computerscience.pfsociety.plasticfee.core.service_grpc;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.google.protobuf.Timestamp;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.ProposalByTitleNotFoundOnRetrieveException;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.core.service.ProposalService;
import it.unical.computerscience.pfsociety.plasticfee.core.service.UserService;
import it.unical.computerscience.pfsociety.plasticfee.core.service.VoteService;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.VoteDto;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.ProposalEntity;
import it.unical.computerscience.pfsociety.plasticfee.data.entity.VoteEntity;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.*;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.ProposalServiceGrpc;
import org.apache.commons.lang3.RandomStringUtils;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@GRpcService
public class ProposalGrpcServiceImpl extends ProposalServiceGrpc.ProposalServiceImplBase {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ProposalGrpcServiceImpl.class);

    private static final int TOKEN_DIMENSION = 30;

    @Autowired
    private UserService userService;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private VoteService voteService;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<User> responseObserver) {

        try {
            UserDto userDto = userService.createNewUser(request.getUsername(), request.getPassword());
            responseObserver.onNext(toProtoUser(userDto));
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("username already present").withCause(e).asRuntimeException());
            return;
        }

        LOGGER.info("user {} creation OK!",request.getUsername());
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        Optional<UserDto> userDto = userService.findById(request.getId());
        if(userDto.isPresent()){
            responseObserver.onNext(toProtoUser(userDto.get()));
            responseObserver.onCompleted();
        }else{
            throw new RuntimeException("user not exist");
        }

        LOGGER.info("user get OK!");
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {

        try {
            Optional<UserDto> userDto = userService.verifyUserCredentials(request.getUsername(), request.getPassword());
        }catch (RuntimeException e){
            responseObserver.onError(Status.fromCode(Status.NOT_FOUND.getCode()).withDescription("wrong username or password").asRuntimeException());
            return;
        }

        responseObserver.onNext(LoginResponse.newBuilder().setToken(RandomStringUtils.randomAlphanumeric(TOKEN_DIMENSION)).build());
        responseObserver.onCompleted();

        LOGGER.info("user {} login OK!",request.getUsername());
    }


    @Override
    public void logout(LogoutRequest request, StreamObserver<LogoutResponse> responseObserver) {
        LOGGER.info("user logout: {}", request.getUsername());
        responseObserver.onNext(LogoutResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void createProposal(CreateProposalRequest request, StreamObserver<Proposal> responseObserver) {
        try {

            ProposalDto proposalDto = proposalService.createProposal(request.getTitle(),
                    request.getDescription(),
                    request.getCreatorUsername(), LocalDateTime.now(),
                    fromProtoToLocalDate(request.getExpirationTimestamp()),
                    request.getRewardReputation());

            responseObserver.onNext(toProtoProposal(proposalDto));

            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("create proposal error").withCause(e).asRuntimeException());
            LOGGER.info("FAILED to create new proposal {} ERROR!",request.getTitle());
        }

        LOGGER.info("New proposal with title {} creation OK!",request.getTitle());
    }

    @Override
    public void retrieveAllActiveProposals(AllActiveProposalsRequest request, StreamObserver<AllActiveProposalsResponse> responseObserver) {

        List<ProposalDto> proposals = proposalService.retrieveAllActiveProposals();

        AllActiveProposalsResponse.Builder responseBuilder = AllActiveProposalsResponse.newBuilder();

        for (ProposalDto prop: proposals){
            responseBuilder.addProposals(toProtoProposal(prop));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void voteActiveProposal(VoteActiveProposalRequest request, StreamObserver<VoteActiveProposalResponse> responseObserver) {

        boolean isVoteAdded = voteService.addVote(request.getIsInFavor(), request.getTitle(), request.getUsername());

        responseObserver.onNext(VoteActiveProposalResponse.newBuilder().build());
        responseObserver.onCompleted();

        LOGGER.info(isVoteAdded?"{} added a vote to proposal {}":"{} FAILED to add a vote to proposal {}",request.getUsername(),request.getTitle());

    }

    @Override
    public void retrieveAllProposals(RetrieveAllProposalsRequest request, StreamObserver<RetrieveAllProposalsResponse> responseObserver) {

        List<ProposalDto> proposals = proposalService.retrieveAllProposals();

        RetrieveAllProposalsResponse.Builder responseBuilder = RetrieveAllProposalsResponse.newBuilder();

        for (ProposalDto prop: proposals){
            responseBuilder.addProposals(toProtoProposal(prop));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void retrieveAllExpiredProposals(RetrieveAllExpiredProposalsRequest request, StreamObserver<RetrieveAllExpiredProposalsResponse> responseObserver) {

        List<ProposalDto> proposals = proposalService.retrieveAllExpiredProposals();

        RetrieveAllExpiredProposalsResponse.Builder responseBuilder = RetrieveAllExpiredProposalsResponse.newBuilder();

        for (ProposalDto prop: proposals){
            responseBuilder.addProposals(toProtoProposal(prop));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void retrievePersonalProposals(RetrievePersonalProposalsRequest request, StreamObserver<RetrievePersonalProposalsResponse> responseObserver) {
        List<ProposalDto> proposals = proposalService.retrievePersonalProposals(request.getUsername());

        RetrievePersonalProposalsResponse.Builder responseBuilder = RetrievePersonalProposalsResponse.newBuilder();

        for (ProposalDto prop: proposals){
            responseBuilder.addProposals(toProtoProposal(prop));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();

    }

    @Override
    public void updateProposalExpirationDate(ExpirationUpdateRequest request, StreamObserver<ExpirationUpdateResponse> responseObserver) {

        try {
            ProposalDto proposalDto = proposalService.retrieveActiveProposalByTitle(request.getProposalTitle());

            if (!request.getUsername().equals(proposalDto.getCreator().getUsername())){
                responseObserver.onNext(ExpirationUpdateResponse.newBuilder().setResponse("Cant' modify other user's proposal").build());
                responseObserver.onCompleted();
                LOGGER.error("Can't modify other user's proposal");
                return;
            }

            proposalService.updateProposalExpirationDate(fromProtoToLocalDate(request.getNewExpiration()),
                    request.getProposalTitle());

            responseObserver.onNext(ExpirationUpdateResponse.newBuilder()
                    .setResponse("The validity period of the proposal has been successfully updated").build());
            responseObserver.onCompleted();

            LOGGER.info("The validity period of the proposal with title {} has been successfully updated",
                    request.getProposalTitle());

        } catch(ProposalByTitleNotFoundOnRetrieveException e){
            responseObserver.onError(Status.NOT_FOUND.withDescription("create proposal error").asRuntimeException());
            LOGGER.error("No proposal found with the specified title");
        }
    }

    private static User toProtoUser(UserDto userDto) {
        return User.newBuilder()
                .setId(userDto.getId())
                .setPassword(userDto.getPassword())
                .setUsername(userDto.getUsername())
                .build();
    }

    private static Proposal toProtoProposal(ProposalDto proposalDto) {

        Proposal.Builder proposalBuilder = Proposal.newBuilder();
        proposalBuilder.setId(proposalDto.getId())
                .setTitle(proposalDto.getTitle())
                .setDescription(proposalDto.getDescription())
                .setCreatorUsername(proposalDto.getCreator().getUsername())
                .setCreationTimestamp(Timestamp.newBuilder().setSeconds(proposalDto.getCreationDateTime().getSecond())
                        .setNanos(proposalDto.getCreationDateTime().getNano()).build());

        for (VoteDto voteDto:proposalDto.getVotesList()) {
            proposalBuilder.addVotesList(toProtoVote(voteDto));
        }

        return proposalBuilder.build();
    }

    private static Vote toProtoVote(VoteDto voteDto) {
        return Vote.newBuilder()
                .setId(voteDto.getId())
                .setIsInFavor(voteDto.getInFavor())
                .build();
    }

    private static LocalDate fromProtoToLocalDate(Timestamp timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()), ZoneId.of("UTC"))
                .toLocalDate();
    }

}
