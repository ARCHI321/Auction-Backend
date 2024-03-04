package com.herovired.Auction.Management.System.exception;

public class UserNotFoundExceptionResponse {
    private Object exceptionCause;
    private String exceptionMessage;
    private String fileName;
    private String methodName;

    public Object getExceptionCause() {
        return exceptionCause;
    }

    public void setExceptionCause(Object exceptionCause) {
        this.exceptionCause = exceptionCause;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
