package message;


import accountService.AccountService;
import logic.UsersDataSet;

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
        if (accountService.addUser(user)) {
            String regStatus = "You're successfully registered!";
            accountService.getMessageSystem().sendMessage(new MsgUpdUserRegStatus(getTo(), getFrom(), sessionId, regStatus));
        } else {
            String regStatus = "Error! This login is occupied.";
            accountService.getMessageSystem().sendMessage(new MsgUpdUserRegStatus(getTo(), getFrom(), sessionId, regStatus));
        }
    }
}
