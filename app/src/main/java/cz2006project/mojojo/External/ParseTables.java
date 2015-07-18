package main.java.cz2006project.mojojo.External;

/**
 * <h1>Parse Tables</h1>
 * These tables serve as a hashmap between our database, www.parse.com and our application.
 *
 * <p>
 *
 * @author  Dhruv Sharma
 * @version 1.0
 * @since   2014-03-31
 */
public class ParseTables {

    /**
     * Users class in Parse that stores all user information
     */

    public static class Users {
        public static final String NAME = "name";
        public static final String USERNAME = "email";
        public static final String PASSWORD = "password";



    }


    public static class Activity {

        public static final String ACTIVITYTYPE = "ActivityType";
        public static final String CLASS = "Class";
        public static final String SECTION = "Section";
        public static final String ACTIVITYDATE = "ActivityDate";
        public static final String COORDINATOR = "Coordinator";


    }


    public static class Leave {
        public static final String TEACHER = "Teacher";
        public static final String LEAVEDATE = "LeaveDate";
        public static final String LEAVETYPE = "LeaveType";
        public static final String REASON = "Reason";
    }

    public static class TeacherSchedule {
        public static final String TEACHER = "Teacher";
        public static final String SUBJECT = "Subject";
        public static final String CLASS = "Class";
        public static final String SECTION = "Section";
        public static final String STRARTTIME = "startTime";
        public static final String ENDTIME = "endTime";
    }

}