package it.unical.computerscience.pfsociety.plasticfee.core.service_grpc;

import com.google.common.collect.Sets;
import com.google.protobuf.Timestamp;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.service.UserService;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.chat.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;

@GRpcService
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    public static final Context.Key<User> CONTEXT_ROLE = Context.key("role_name"); //PER RICAVARE L'UTENTE

    private final long millis = System.currentTimeMillis();

    private final Timestamp currentTimestamp = Timestamp.newBuilder().setSeconds(millis / 1000)
            .setNanos((int) ((millis % 1000) * 1000000)).build();

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ChatServiceImpl.class);

    private static final int TOKEN_DIMENSION = 30;

    private Set<StreamObserver<ChatResponse>> clients = Sets.newConcurrentHashSet(); //SET IN CUI CI SARA' UN ELENCO DI CLIENTS
    //COLLEGATI ALLO STESSO SERVER PER UTILIZZARE LA CHAT

    @Autowired
    private UserService userService;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<User> responseObserver) {

        try {
            UserDto userDto = userService.createNewUser(request.getUsername(), request.getPassword());
            responseObserver.onNext(toProtoUser(userDto));
            responseObserver.onCompleted();
        }catch (Exception e){
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("create user error").withCause(e).asRuntimeException());
        }
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
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {

        try {
            Optional<UserDto> userDto = userService.verifyUserRegistration(request.getUsername(), request.getPassword());
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

    /*@Override
    public void message(
            User userRequest, StreamObserver<Message> responseObserver) {

        LOGGER.info("message id:{} username:{}",userRequest.getId(),userRequest.getUsername());

        //FIXME L'ENTITA' MESSAGE E' INUTILE?
        MessageDto messageDto = new MessageDto();
        messageDto.setContent("Hello, " + userRequest.getId() + " " + userRequest.getUsername());
        messageDto.setTimestamp(LocalDateTime.of(LocalDate.now(),LocalTime.now()));

        Message messageResponse = Message.newBuilder()
                .setContent(messageDto.getContent())
                .setTimestamp(LocalDateTime.of(LocalDate.now(), LocalTime.now()).toString())
                .build();

        responseObserver.onNext(messageResponse);
        responseObserver.onCompleted();
    }*/

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

}
