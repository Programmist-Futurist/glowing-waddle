package ua.nure.levchenko.SummaryTask4.web.path;

public class RequestPath {
    //Request Parameters

    //User fields
    private final String id = "id";
    private final String login = "login";
    private final String oldLogin = "oldLogin";
    private final String password = "password";
    private final String oldPassword = "oldPassword";
    private final String roleId = "roleId";

    // Student/Teacher fields
    private final String firstName = "firstName";
    private final String lastName = "lastName";
    private final String patronymic = "patronymic";
    private final String firstNameRus = "firstNameRus";
    private final String lastNameRus = "lastNameRus";
    private final String patronymicRus = "patronymicRus";
    private final String experienceRus = "experienceRus";
    private final String experience = "experience";
    private final String email = "email";
    private final String phone = "phone";

    // app
    private final String command = "command";
    private final String action = "action";
    private final String button = "button";
    private final String locale = "locale";

    // Courses fields
    private final String courseTeacher = "courseTeacher";
    private final String courseTopic = "courseTopic";
    private final String courseName = "courseName";
    private final String description = "description";
    private final String duration = "duration";
    private final String startDate = "startDate";
    private final String endDate = "endDate";
    private final String courseNameRus = "courseNameRus";
    private final String descriptionRus = "descriptionRus";

    // Filter courses page
    private final String status = "status";
    private final String topic = "topic";
    private final String sort = "sort";
    private final String courses = "courses";

    // ManageStudentCommand page
    private final String students = "students";

    // Journals teacher page
    private final String studentMark = "studentMark";
    private final String studentId = "studentId";
    private final String courseId = "courseId";
    private final String journalId = "journalId";
    private final String courseShowId = "courseShowId";
    private final String currentJournal = "currentJournal";
    private final String teacherJournals = "teacherJournals";
    private final String teacherCourses = "teacherCourses";
    private final String studentCourses = "studentCourses";
    private final String totalStudentAmount = "totalStudentAmount";


    // ManageTeacher command
    private final String pageNum = "pageNum";

    // CourseForTeacherSetCommand
    private final String teacherId = "teacherId";
    private final String chosenTeacher = "chosenTeacher";
    private final String chosenCourse = "chosenCourse";


    // ManageCourses command
    private final String manageCourses = "manageCourses";
    private final String course = "course";


    //Request Attributes
    private final String errorMessage = "errorMessage";
    private final String infoMessage = "infoMessage";
    private final String filterApply = "filterApply";
    private final String journal = "journal";
    private final String teachers = "teachers";
    private final String blockedStudents = "blockedStudents";
    private final String unblockedStudents = "unblockedStudents";
    private final String topics = "topics";
    private final String statuses = "statuses";
    private final String roles = "roles";


    public String getLogin() {
        return login;
    }

    public String getOldLogin() {
        return oldLogin;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public String getAction() {
        return action;
    }

    public String getStatus() {
        return status;
    }

    public String getSort() {
        return sort;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCommand() {
        return command;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public String getExperience() {
        return experience;
    }

    public String getFirstNameRus() {
        return firstNameRus;
    }

    public String getLastNameRus() {
        return lastNameRus;
    }

    public String getPatronymicRus() {
        return patronymicRus;
    }

    public String getExperienceRus() {
        return experienceRus;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getId() {
        return id;
    }

    public String getCourseTeacher() {
        return courseTeacher;
    }

    public String getCourseTopic() {
        return courseTopic;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCourseNameRus() {
        return courseNameRus;
    }

    public String getDescriptionRus() {
        return descriptionRus;
    }

    public String getStudentMark() {
        return studentMark;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getJournalId() {
        return journalId;
    }

    public String getPageNum() {
        return pageNum;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getFilterApply() {
        return filterApply;
    }

    public String getButton() {
        return button;
    }

    public String getStudents() {
        return students;
    }

    public String getTopic() {
        return topic;
    }

    public String getJournal() {
        return journal;
    }

    public String getCourseShowId() {
        return courseShowId;
    }

    public String getCurrentJournal() {
        return currentJournal;
    }

    public String getManageCourses() {
        return manageCourses;
    }

    public String getCourse() {
        return course;
    }

    public String getChosenTeacher() {
        return chosenTeacher;
    }

    public String getTeacherJournals() {
        return teacherJournals;
    }

    public String getStudentCourses() {
        return studentCourses;
    }

    public String getTeacherCourses() {
        return teacherCourses;
    }

    public String getCourses() {
        return courses;
    }

    public String getChosenCourse() {
        return chosenCourse;
    }

    public String getTeachers() {
        return teachers;
    }

    public String getBlockedStudents() {
        return blockedStudents;
    }

    public String getUnblockedStudents() {
        return unblockedStudents;
    }

    public String getTopics() {
        return topics;
    }

    public String getStatuses() {
        return statuses;
    }

    public String getRoles() {
        return roles;
    }

    public String getLocale() {
        return locale;
    }

    public String getTotalStudentAmount() {
        return totalStudentAmount;
    }
}

