package it.unical.computerscience.pfsociety.plasticfee.core.service_grpc;

import com.google.common.collect.Sets;
import com.google.protobuf.Timestamp;
import io.grpc.Context;
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
import java.util.List;

import java.util.Optional;
import java.util.Set;

@GRpcService
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    // To obtain the user
    public static final Context.Key<User> CONTEXT_ROLE = Context.key("role_name");

    private final long millis = System.currentTimeMillis();

    private final Timestamp currentTimestamp = Timestamp.newBuilder().setSeconds(millis / 1000)
            .setNanos((int) ((millis % 1000) * 1000000)).build();

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ChatServiceImpl.class);

    private static final int TOKEN_DIMENSION = 30;

    // A set where will be clients
    private Set<StreamObserver<ChatResponse>> clients = Sets.newConcurrentHashSet();

    // Connect to the same server to use the chat

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
            //responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("user not exist!").asRuntimeException());
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
        broadCast(ChatResponse.newBuilder().setTimestamp(currentTimestamp)
                .setLoginEvent(ChatResponse.Login.newBuilder().setName(request.getUsername()).build())
                .build());
    }

    @Override
    public StreamObserver<ChatRequest> chat(StreamObserver<ChatResponse> responseObserver) {

        clients.add(responseObserver);

        return new StreamObserver<ChatRequest>() {

            @Override
            public void onNext(ChatRequest chatRequest) {
                    LOGGER.info("got message from {} : {}",chatRequest.getUsername(),chatRequest.getMessage());

                    broadCast(ChatResponse.newBuilder()
                            .setTimestamp(currentTimestamp)
                            .setMessageEvent(
                                    ChatResponse.Message
                                    .newBuilder()
                                    .setMessage(chatRequest.getMessage())
                                    .setName(chatRequest.getUsername())
                                    .build()
                            )
                            .build());
            }

            @Override
            public void onError(Throwable throwable) {
                LOGGER.error("PROBLEMS IN THE CHAT SERVICE");
            }

            @Override
            public void onCompleted() {
                userLogout(responseObserver);
            }
        };
    }

    private void userLogout(StreamObserver<ChatResponse> responseObserver) {
        clients.remove(responseObserver);

        broadCast(ChatResponse.newBuilder()
                .setTimestamp(currentTimestamp)
                .setLogoutEvent(
                        ChatResponse.Logout
                                .newBuilder()
                                .setUsername("prova 1")
                        .build()
                )
                .build());
    }


    @Override
    public void logout(LogoutRequest request, StreamObserver<LogoutResponse> responseObserver) {
        LOGGER.info("user logout: {}; {} remaining in the chatroom", request.getUsername(),clients.size());
        responseObserver.onNext(LogoutResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void createProposal(CreateProposalRequest request, StreamObserver<Proposal> responseObserver) {
        try {
            ProposalDto proposalDto = proposalService.createProposal(request.getTitle(), request.getDescription(),request.getCreatorUsername());
            responseObserver.onNext(toProtoProposal(proposalDto));
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("create proposal error").withCause(e).asRuntimeException());
        }

        LOGGER.info("New proposal with title {} creation OK!",request.getTitle());
        broadCast(ChatResponse.newBuilder()
                .setTimestamp(currentTimestamp)
                .setProposalEvent(ChatResponse.Proposal.newBuilder()
                .setUsername(request.getCreatorUsername())
                .setTitle(request.getTitle())
                .build()).build());
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

    private void broadCast(ChatResponse msg){
        for(StreamObserver<ChatResponse> resp : clients){
            resp.onNext(msg);
        }
    }

    private static Proposal toProtoProposal(ProposalDto proposalDto){
        return Proposal.newBuilder()
                .setTitle(proposalDto.getTitle())
                .setDescription(proposalDto.getDescription())
                .setCreatorUsername(proposalDto.getCreator().getUsername())
                .build();
    }

}
