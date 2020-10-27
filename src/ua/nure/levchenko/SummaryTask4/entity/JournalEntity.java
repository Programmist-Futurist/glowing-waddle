package ua.nure.levchenko.SummaryTask4.entity;

import java.util.Objects;

/**
 * Journal entity.
 *
 * @author K.Levchenko
 */
public class JournalEntity extends Entity {
    private static final long serialVersionUID = -5548329124249045184L;

    private int courseId;
    private int studentId;
    private int mark;

    public JournalEntity(int id, int courseId, int studentId, int mark) {
        this.setId(id);
        this.courseId = courseId;
        this.studentId = studentId;
        this.mark = mark;
    }

    public JournalEntity(int courseId, int studentId, int mark) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.mark = mark;
    }

    public JournalEntity(int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.mark = 0;
    }

    public JournalEntity() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JournalEntity that = (JournalEntity) o;
        return getCourseId() == that.getCourseId() &&
                getStudentId() == that.getStudentId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseId(), getStudentId());
    }
}
