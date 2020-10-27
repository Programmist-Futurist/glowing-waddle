package ua.nure.levchenko.SummaryTask4.entity;


/**
 * Student entity.
 *
 * @author K.Levchenko
 */
public class Student extends User {
    private static final long serialVersionUID = -8207065867517931434L;

    private int firstNameId;
    private int lastNameId;
    private int patronymicId;
    private String email;
    private String phone;
    private boolean block;


    public Student(int firstNameId, int lastNameId, int patronymicId, String email, String phone, boolean block) {
        this.firstNameId = firstNameId;
        this.lastNameId = lastNameId;
        this.patronymicId = patronymicId;
        this.email = email;
        this.phone = phone;
        this.block = block;
    }

    public Student() {
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

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() + ", roleId=" + getRoleId() + ", login=" + getLogin() + ", pass=" + getPassword() +
                ", firstNameId=" + firstNameId +
                ", lastNameId=" + lastNameId +
                ", patronymicId=" + patronymicId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", block=" + block +
                '}';
    }
}
