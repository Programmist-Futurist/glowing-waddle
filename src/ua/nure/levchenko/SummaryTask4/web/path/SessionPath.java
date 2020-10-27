package ua.nure.levchenko.SummaryTask4.web.path;

public class SessionPath {
    //Session Attributes
    private final String user = "user";
    private final String userRole = "userRole";
    private final String bundle = "bundle";
    private final String currentPage = "currentPage";


    public String getUser() {
        return user;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getBundle() {
        return bundle;
    }

    public String getCurrentPage() {
        return currentPage;
    }
}
