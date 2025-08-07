package vn.chuot96.verify3rd.exception;

public class NoSupportedProviderException extends RuntimeException {
    public NoSupportedProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSupportedProviderException(String message) {
        super(message);
    }
}
