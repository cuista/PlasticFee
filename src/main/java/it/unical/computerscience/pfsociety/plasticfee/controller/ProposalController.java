package it.unical.computerscience.pfsociety.plasticfee.controller;

import it.unical.computerscience.pfsociety.plasticfee.core.service.ProposalService;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.ProposalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/proposals")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;

    @GetMapping("/all-active")
    public ResponseEntity<List<ProposalDto>> retrieveAllActiveProposals() {
        return ResponseEntity.ok(proposalService.retrieveAllActiveProposals());
    }

    @GetMapping("/create")
    public ResponseEntity<ProposalDto> createProposal(@RequestParam String title, @RequestParam String description, @RequestParam String creatorUsername, @RequestParam LocalDateTime creationDateTime, @RequestParam LocalDate expirationDate, @RequestParam int reputationReward) {
        return ResponseEntity.ok(proposalService.createProposal(title, description,creatorUsername, creationDateTime, expirationDate, reputationReward));
    }

}
