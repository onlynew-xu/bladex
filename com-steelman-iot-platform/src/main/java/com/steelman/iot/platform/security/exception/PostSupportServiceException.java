package com.steelman.iot.platform.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * @author tang
 * date 2020-11-22
 */
public class PostSupportServiceException extends AuthenticationServiceException {

    public PostSupportServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostSupportServiceException(String message) {
        super(message);
    }

}
