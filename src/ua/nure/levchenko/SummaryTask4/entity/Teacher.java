package ua.nure.levchenko.SummaryTask4.entity;


/**
 * Teacher entity.
 *
 * @author K.Levchenko
 */
public class Teacher extends User {
    private static final long serialVersionUID = 8252449352133499709L;

    private int firstNameId;
    private int lastNameId;
    private int patronymicId;
    private int experienceId;
    private String email;
    private String phone;

    public Teacher(int firstNameId, int lastNameId, int patronymicId, int experienceId, String email, String phone) {
        this.firstNameId = firstNameId;
        this.lastNameId = lastNameId;
        this.patronymicId = patronymicId;
        this.experienceId = experienceId;
        this.email = email;
        this.phone = phone;
    }

    public Teacher() {
    }

    public void setUser(User user) {
        setLogin(user.getLogin());
        setPassword(user.getPassword());
        setRoleId(user.getRoleId());
    }

    public int getFirstNameId() {
        return firstNameId;
    }

    public void setFirstNameId(int firstNameId) {
        this.firstNameId = firstNameId;
    }

    public int getLastNameId() {
        return lastNameId;
    }

    public void setLastNameId(int lastNameId) {
        this.lastNameId = lastNameId;
    }

    public int getPatronymicId() {
        return patronymicId;
    }

    public void setPatronymicId(int patronymicId) {
        this.patronymicId = patronymicId;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(int experienceId) {
        this.experienceId = experienceId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstNameId=" + firstNameId +
                ", lastNameId=" + lastNameId +
                ", patronymicId=" + patronymicId +
                '}';
    }
}
