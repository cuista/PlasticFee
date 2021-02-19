package it.unical.computerscience.pfsociety.plasticfee;

import it.unical.computerscience.pfsociety.plasticfee.data.service.ProposalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProposalTest {

    @Autowired
    ProposalService proposalService;

    @Test
    public void testSave(){
        proposalService.createProposal("ciao","ciao","manuel95");

        assert (proposalService.retrieveAllProposals().size() == 1);

    }

}
