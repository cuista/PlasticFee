package it.unical.computerscience.pfsociety.plasticfee.core.service.exception;

import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.classgeneric.UserException;

public class UserByIdNotFoundOnRetrieveException extends UserException {

    public UserByIdNotFoundOnRetrieveException(Long id){super(String.format("No user found with id %s", id));}

}
