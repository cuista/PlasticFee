package it.unical.computerscience.pfsociety.plasticfee;

import it.unical.computerscience.pfsociety.plasticfee.core.service.ProposalService;
import it.unical.computerscience.pfsociety.plasticfee.core.service_grpc.ProposalGrpcServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class ProposalExpirationChecker implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private ProposalService proposalService;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ProposalGrpcServiceImpl.class);


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LOGGER.info("{}: Verifying all active proposal expiration Date and update if needed.", new Timestamp(System.currentTimeMillis()));
                proposalService.verifyProposalsExpirationAndUpdateIfNeeded();
            }
        }, 0, 30000);
    }
}
