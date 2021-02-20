package it.unical.computerscience.pfsociety.plasticfee.core.service_grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.service.ProposalService;
import it.unical.computerscience.pfsociety.plasticfee.data.service.UserService;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.chat.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@GRpcService
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ChatServiceImpl.class);

    private static final int TOKEN_DIMENSION = 30;

    @Autowired
    private UserService userService;

    @Autowired
    private ProposalService proposalService;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<User> responseObserver) {

        try {
            UserDto userDto = userService.createNewUser(request.getUsername(), request.getPassword());
            responseObserver.onNext(toProtoUser(userDto));
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("create user error").withCause(e).asRuntimeException());
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
            ProposalDto proposalDto = proposalService.createProposal(request.getTitle(), request.getDescription(),
                    request.getCreatorUsername(),new Timestamp(System.currentTimeMillis()));
            responseObserver.onNext(toProtoProposal(proposalDto));
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("create proposal error").withCause(e).asRuntimeException());
        }

        LOGGER.info("New proposal with title {} creation OK!",request.getTitle());
    }

    @Override
    public void retrieveAllProposals(AllProposalsRequest request, StreamObserver<AllProposalsResponse> responseObserver) {

        List<ProposalDto> proposals = proposalService.retrieveAllProposals();

        AllProposalsResponse.Builder responseBuilder = AllProposalsResponse.newBuilder();

        for (ProposalDto prop: proposals){
            responseBuilder.addProposals(toProtoProposal(prop));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    private static User toProtoUser(UserDto userDto) {
        return User.newBuilder()
                .setId(userDto.getId())
                .setUsername(userDto.getUsername())
                .build();
    }

    private static Proposal toProtoProposal(ProposalDto proposalDto){
        return Proposal.newBuilder()
                .setTitle(proposalDto.getTitle())
                .setDescription(proposalDto.getDescription())
                .setCreatorUsername(proposalDto.getCreator().getUsername())
                .build();
    }

}
