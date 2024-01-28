package com.java2e.martin.extension.ncnb.exception;

/**
 * @author 狮少
 * @version 1.0
 * @date 2020/12/15
 * @describtion CDException
 * @since 1.0
 */
public class CDException extends RuntimeException {
    private static final long serialVersionUID = 4913959406705554204L;

    public CDException() {
        super();
    }

    public CDException(String message) {
        super(message);
    }

    public CDException(String message, Throwable cause) {
        super(message, cause);
    }

    public CDException(Throwable cause) {
        super(cause);
    }

    protected CDException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
