package message;

import accountService.AccountService;
import logic.UsersDataSet;
import util.resources.Messages;
import util.resources.ResourceFactory;

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
        Messages messages = (Messages) ResourceFactory.getInstance().get("data/messages.xml");
        if (user == null)
		    accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),
                    getFrom(), sessionId, null, messages.getNotRegistered()));
            //вынести текст сообщений в ресурсы
        else if (password.equals(user.getPassword()))
            accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),
                    getFrom(), sessionId, user.getId(), ""));
        else
            accountService.getMessageSystem().sendMessage(new MsgUpdateUserId(getTo(),
                    getFrom(), sessionId, null, messages.getWrongPassword()));
	}
}
