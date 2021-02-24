package it.unical.computerscience.pfsociety.plasticfee.core.service.exception;

import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.classgeneric.ProposalException;

public class ProposalByTitleNotFoundOnRetrieveException extends ProposalException {
    public ProposalByTitleNotFoundOnRetrieveException(String title){super(String.format("No proposal found with title %s",title));}
}
