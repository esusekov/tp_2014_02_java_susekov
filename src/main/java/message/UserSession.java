package message;

public class UserSession {
    private Address accountService;

    private String name;
    private String sessionId;
    private Long userId;
    private String status;

    public UserSession(String sessionId, String name, AddressService addressService) {
        this.sessionId = sessionId;
        this.name = name;
        this.status = "";
        this.accountService = addressService.getAccountService();
    }

    public Address getAccountService() {
        return accountService;
    }

    public String getName(){
        return name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getStatus() {
        return status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserStatus(String status) {
        this.status = status;
    }
}
