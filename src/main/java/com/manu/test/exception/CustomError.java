package com.manu.test.exception;

import java.io.Serializable;
import java.util.List;

/**
 * @author emmanuel.mura
 *
 */
public class CustomError implements Serializable {

    private static final long serialVersionUID = 8758384134742359393L;

    private final List<String> errorMessages;

    public CustomError(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }
}
