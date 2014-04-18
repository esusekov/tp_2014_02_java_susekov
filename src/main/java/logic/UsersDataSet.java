package logic;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="users")
public class UsersDataSet {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    private final Long id;
    @Column(name="login")
    private final String login;
    @Column(name="password")
    private final String password;

    public UsersDataSet() {
        this.id = Long.valueOf(-1);
        this.login = null;
        this.password = null;
    }

    public UsersDataSet(String login, String password) {
        this.id = Long.valueOf(-1);
        this.login = login;
        this.password = password;
    }

    public UsersDataSet(Long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
