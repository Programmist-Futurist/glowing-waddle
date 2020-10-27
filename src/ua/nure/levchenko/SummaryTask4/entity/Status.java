package ua.nure.levchenko.SummaryTask4.entity;

/**
 * Enum of status entities.
 *
 * @author K.Levchenko
 */
public enum Status {
    // the order should be the same as in the database
    NOT_STARTED, PROCESSING, FINISHED;

    private int id;
    private int nameId;

    public static Status getStatus(Course course) {
        int statusId = course.getStatusId();
        return Status.values()[statusId];
    }

    public static int getIntValue(String status) {
        for (int i = 0; i < 3; i++) {
            if (status.equals(Status.values()[i].getName())) {
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
            if (this.equals(Status.values()[i])) {
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
