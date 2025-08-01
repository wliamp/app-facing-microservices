package vn.chuot96.auth3rdapi.exception;

public class NoSupportedProviderException extends RuntimeException {
    public NoSupportedProviderException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoSupportedProviderException(String message) {
        super(message);
    }
}
