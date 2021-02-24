package it.unical.computerscience.pfsociety.plasticfee.core.service.exception;

import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.classgeneric.ProposalException;

public class ProposalByIdNotFoundOnRetrieveException extends ProposalException {
    public ProposalByIdNotFoundOnRetrieveException(Long id){super(String.format("No proposal found with id %s", id));}

}
