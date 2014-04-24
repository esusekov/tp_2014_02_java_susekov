package message;

import frontend.Frontend;

public class MsgUpdateUserId extends MsgToFrontend {

	private String sessionId;
	private Long id;
    private String status;

	public MsgUpdateUserId(Address from, Address to, String sessionId, Long id, String status) {
		super(from, to);
		this.sessionId = sessionId;
        this.status = status;
		this.id = id;
	}

	void exec(Frontend frontend) {
        frontend.setId(sessionId, id, status);
	}

}
