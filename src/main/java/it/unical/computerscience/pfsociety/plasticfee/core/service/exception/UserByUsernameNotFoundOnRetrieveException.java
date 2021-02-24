package it.unical.computerscience.pfsociety.plasticfee.core.service.exception;

import it.unical.computerscience.pfsociety.plasticfee.core.service.exception.classgeneric.UserException;

public class UserByUsernameNotFoundOnRetrieveException extends UserException {

    public UserByUsernameNotFoundOnRetrieveException(String username){super(String.format("No user found with username %s", username));}

}
