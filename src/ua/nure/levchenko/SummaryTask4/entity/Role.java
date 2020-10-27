package ua.nure.levchenko.SummaryTask4.entity;

/**
 * Enum of role entities.
 *
 * @author K.Levchenko
 */
public enum Role {
    // the order should be the same as in the database
    ADMIN, NOT_ADMIN;

    private int id;
    private int nameId;

    public static Role getRole(User user) {
        int roleId = user.getRoleId();
        return Role.values()[roleId - 1];
    }

    public static int getIntValue(String role) {
        for (int i = 0; i < 2; i++) {
            if (role.equals(Role.values()[i].getName())) {
                return i + 1;
            }
        }
        return -1;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public int getIntKey() {
        for (int i = 0; i < 3; i++) {
            if (this.equals(Role.values()[i])) {
                return i + 1;
            }
        }
        return -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }
}
