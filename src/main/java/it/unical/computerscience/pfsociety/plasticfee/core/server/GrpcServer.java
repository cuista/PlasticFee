package it.unical.computerscience.pfsociety.plasticfee.core.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.unical.computerscience.pfsociety.plasticfee.core.service_grpc.ChatServiceImpl;
import org.lognet.springboot.grpc.GRpcService;

import java.io.IOException;

@GRpcService
public class GrpcServer {

    /*
        private Server server;

    private void start() throws IOException {
        int port = 8000;
        server = ServerBuilder.forPort(port)
                .addService(ServerInterceptors.intercept(new ChatRoomServiceImpl(),new RoleServerInterceptor()))
                .build()
                .start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.err.println("*** shutting down grpc server since JVM is shutting down");
            ChatServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }
    private void stop(){
        if(server != null){
            server.shutdown();
        }
    }
    private void blockUntilShutdown() throws  InterruptedException{
        if(server != null){
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        final ChatServer server = new ChatServer();
        server.start();
        server.blockUntilShutdown();
    }
    */


        public static void main(String[] args) throws IOException, InterruptedException {
            Server server = ServerBuilder
                    .forPort(8070)
                    .addService(new ChatServiceImpl()).build();

            server.start();
            server.awaitTermination();
        }

}
