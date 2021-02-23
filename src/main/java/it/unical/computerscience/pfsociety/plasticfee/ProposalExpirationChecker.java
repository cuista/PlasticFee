package it.unical.computerscience.pfsociety.plasticfee;

import it.unical.computerscience.pfsociety.plasticfee.core.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProposalExpirationChecker extends Thread{

    @Autowired
    private ProposalService proposalService;

    @Override
    public void run() {

        while(true){

            try {

                proposalService.verifyProposalsExpiration();

                System.out.println("PORCOZEUS");

                Thread.sleep(1000 * System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
