package com.steelman.iot.platform.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * @author tang
 * date 2020-11-22
 */
public class ParamException extends AuthenticationServiceException {
    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(String message) {
        super(message);
    }
}
