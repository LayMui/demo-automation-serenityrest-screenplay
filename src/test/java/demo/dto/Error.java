package demo.dto;

public class Error {
    private String[] msg;
    private int errorCode;

    public Error(String[] msg, int errorCode) {
        this.msg = msg;
        this.errorCode = errorCode;
    }
}
