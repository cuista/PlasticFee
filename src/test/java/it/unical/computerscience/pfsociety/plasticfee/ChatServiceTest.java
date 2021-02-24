package it.unical.computerscience.pfsociety.plasticfee;

import io.grpc.ManagedChannelBuilder;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.*;
import it.unical.computerscience.pfsociety.plasticfee.protobuf.proposal.ProposalServiceGrpc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

//FIXME separate tests from main server and db

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatServiceTest {

    private static final String HOST="localhost";
    private static final int PORT=8070;
    private ProposalServiceGrpc.ProposalServiceBlockingStub stub;

    @Before
    public void init()
    {
        stub = ProposalServiceGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(HOST,PORT).usePlaintext().build());
    }

    /*
    @Test
    public void test_createUser(){
        User user = stub.createUser(CreateUserRequest.newBuilder().setUsername("ElonMusk123").setPassword("Tesla").build());
        assert user.getUsername().equals("ElonMusk123");
    }

    @Test
    public void test_getUser(){
        User user = stub.getUser(GetUserRequest.newBuilder().setId(31L).build());
        assert user.getUsername().equals("ElonMusk123");
    }

    @Test
    public void test_login(){
        LoginResponse loginResponse = stub.login(LoginRequest.newBuilder().setUsername("ElonMusk123").setPassword("Tesla").build());
        assert !loginResponse.getToken().isEmpty();
    }
    */

}
