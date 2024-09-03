package com.vodafone.exceptions;

public class ClientOneException extends Exception {

    private static String DEFAULT_FALLBACK = "Client 1 returned an error";

    public ClientOneException() {
        super(DEFAULT_FALLBACK);
    }
}
