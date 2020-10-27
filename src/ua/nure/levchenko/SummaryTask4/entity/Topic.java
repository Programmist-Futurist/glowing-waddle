package ua.nure.levchenko.SummaryTask4.entity;

/**
 * Enum of topic entities.
 *
 * @author K.Levchenko
 */
public enum Topic {
    // the order should be the same as in the database
    PROGRAMMING, SELF_DEVELOPMENT;

    private int id;
    private int nameId;

    public static Topic getStatus(Course course) {
        int topicId = course.getStatusId();
        return Topic.values()[topicId];
    }

    public static int getIntValue(String topic) {
        for (int i = 0; i < 3; i++) {
            if (topic.equals(Topic.values()[i].getName())) {
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
            if (this.equals(Topic.values()[i])) {
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
