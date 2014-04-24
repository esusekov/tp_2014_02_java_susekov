package message;

import frontend.Frontend;

public class MsgUpdUserRegStatus extends MsgToFrontend {
    private String sessionId;
    private String regStatus;

    public MsgUpdUserRegStatus(Address from, Address to, String sessionId, String regStatus) {
        super(from, to);
        this.sessionId = sessionId;
        this.regStatus = regStatus;
    }

    void exec(Frontend frontend) {
        frontend.setUserRegStatus(sessionId, regStatus);
    }

}
