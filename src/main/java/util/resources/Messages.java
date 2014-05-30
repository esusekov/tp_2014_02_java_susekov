package util.resources;

public class Messages implements Resource{
    private String authError;
    private String waitForAuth;
    private String waitForReg;
    private String emptyFieldsError;
    private String successReg;
    private String loginOccupied;
    private String notRegistered;
    private String wrongPassword;

    public String getAuthError() {
        return authError;
    }

    public String getWaitForAuth() {
        return waitForAuth;
    }

    public String getWaitForReg() {
        return waitForReg;
    }

    public String getEmptyFieldsError() {
        return emptyFieldsError;
    }

    public String getSuccessReg() {
        return successReg;
    }

    public String getLoginOccupied() {
        return loginOccupied;
    }

    public String getNotRegistered() {
        return notRegistered;
    }

    public String getWrongPassword() {
        return wrongPassword;
    }
}

