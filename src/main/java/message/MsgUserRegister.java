package message;


import accountService.AccountService;
import logic.UsersDataSet;
import util.resources.Messages;
import util.resources.ResourceFactory;

public class MsgUserRegister extends MsgToAS {
    private String name;
    private String password;
    private String sessionId;

    public MsgUserRegister(Address from, Address to, String name, String password, String sessionId) {
        super(from, to);
        this.name = name;
        this.password = password;
        this.sessionId = sessionId;
    }

    void exec(AccountService accountService) {
        UsersDataSet user = new UsersDataSet(name, password);
        Messages messages = (Messages) ResourceFactory.getInstance().get("data/messages.xml");
        if (accountService.addUser(user)) {
            String regStatus = messages.getSuccessReg();
            accountService.getMessageSystem().sendMessage(new MsgUpdUserRegStatus(getTo(), getFrom(), sessionId, regStatus));
        } else {
            String regStatus = messages.getLoginOccupied();
            accountService.getMessageSystem().sendMessage(new MsgUpdUserRegStatus(getTo(), getFrom(), sessionId, regStatus));
        }
    }
}
