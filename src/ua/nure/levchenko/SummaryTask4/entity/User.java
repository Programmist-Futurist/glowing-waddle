package ua.nure.levchenko.SummaryTask4.entity;

import java.util.Objects;

/**
 * User entity.
 *
 * @author K.Levchenko
 */
public class User extends Entity {
    private static final long serialVersionUID = 8429947985516446844L;

    private String login;
    private String password;
    private int roleId;

    public User(int id, String login, String password, int roleId) {
        super(id);
        this.login = login;
        this.password = password;
        this.roleId = roleId;
    }

    public User(String login, String password, int roleId) {
        this.login = login;
        this.password = password;
        this.roleId = roleId;
    }

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getLogin().equals(user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin());
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                '}';
    }
}
