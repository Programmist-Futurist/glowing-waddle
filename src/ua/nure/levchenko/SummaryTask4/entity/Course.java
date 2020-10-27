package ua.nure.levchenko.SummaryTask4.entity;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * Course entity.
 *
 * @author K.Levchenko
 */
public class Course extends Entity {
    private static final long serialVersionUID = 4960461981118206923L;

    private int teacherId;
    private int topicId;
    private int statusId;
    private int nameId;
    private int descriptionId;
    private int durationInMonths;
    private Date startDate;
    private Date endDate;


    public Course(int id, int teacherId, int topicId, int statusId, int nameId, int descriptionId, int durationInMonths, Date startDate, Date endDate) {
        super(id);
        this.teacherId = teacherId;
        this.topicId = topicId;
        this.statusId = statusId;
        this.nameId = nameId;
        this.descriptionId = descriptionId;
        this.durationInMonths = durationInMonths;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Course(int teacherId, int topicId, int statusId, int nameId, int descriptionId, int durationInMonths, Date startDate, Date endDate) {
        this.teacherId = teacherId;
        this.topicId = topicId;
        this.statusId = statusId;
        this.nameId = nameId;
        this.descriptionId = descriptionId;
        this.durationInMonths = durationInMonths;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Course(int teacherId, int topicId, int nameId, int descriptionId, Date startDate, Date endDate) {
        this.teacherId = teacherId;
        this.topicId = topicId;
        this.nameId = nameId;
        this.descriptionId = descriptionId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Course() {
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getStatusId() {
        // status defining
        Date date = new Date();

        Status status = date.compareTo(startDate) < 0 ? Status.NOT_STARTED
                : (date.compareTo(endDate) > 0 ? Status.FINISHED : Status.PROCESSING);

        return status.getIntKey();
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getNameId() {
        return nameId;
    }

    public void setNameId(int nameId) {
        this.nameId = nameId;
    }

    public int getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(int descriptionId) {
        this.descriptionId = descriptionId;
    }

    public int getDurationInMonths() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(endDate.getTime() - startDate.getTime());

        int mYear = calendar.get(Calendar.YEAR) - 1970;
        int mMonth = calendar.get(Calendar.MONTH);
        return mYear * 12 + mMonth;
    }

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return getNameId() == course.getNameId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNameId());
    }


}