package it.unical.computerscience.pfsociety.plasticfee.core.service_grpc;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.MessageDto;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.UserDto;
import it.unical.computerscience.pfsociety.plasticfee.data.service.UserService;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.chat.*;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@GRpcService
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ChatServiceImpl.class);

    @Autowired
    private UserService userService;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<User> responseObserver) {

        try {
            UserDto userDto = userService.createNewUser(request.getUsername());
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
    }

    private static User toProtoUser(UserDto userDto) {
        return User.newBuilder()
                .setId(userDto.getId())
                .setUsername(userDto.getUsername())
                .build();
    }

}
