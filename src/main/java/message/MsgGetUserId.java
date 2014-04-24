package message;

import accountService.AccountService;
import logic.UsersDataSet;

public class MsgGetUserId extends MsgToAS {
	private String name;
    private String password;
    private String sessionId;
	
	public MsgGetUserId(Address from, Address to, String name, String password, String sessionId) {
		super(from, to);
		this.name= name;
        this.password = password;
        this.sessionId = sessionId;
	}

	void exec(AccountService accountService) {
		UsersDataSet user = accountService.getUserByLogin(name);
        if (user == null)
		    accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),
                    getFrom(), sessionId, null, "You're not registered!"));
        else if (password.equals(user.getPassword()))
            accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),
                    getFrom(), sessionId, user.getId(), ""));
        else
            accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),
                    getFrom(), sessionId, null, "Error! Wrong password."));
	}
}
