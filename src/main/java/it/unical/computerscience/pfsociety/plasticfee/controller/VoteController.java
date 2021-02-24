package it.unical.computerscience.pfsociety.plasticfee.controller;

import it.unical.computerscience.pfsociety.plasticfee.core.service.VoteService;
import it.unical.computerscience.pfsociety.plasticfee.data.dto.VoteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/votes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @GetMapping("/all")
    public ResponseEntity<List<VoteDto>> getAllVotes() {
        return ResponseEntity.ok(voteService.retrieveAllVotes());
    }

}
