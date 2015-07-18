package main.java.cz2006project.mojojo.Entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("TeacherSchedule")
public class Timetable extends ParseObject {

    public Timetable() {

    }
    public void setTeacher(ParseObject teacher) {
        put("Teacher", teacher);

    }

    public void setSubject(ParseObject subject) {
        put("Subject", subject);

    }

    public void setClassNo(ParseObject Class) {
        put("Class", Class);

    }

    public void setSection(ParseObject section) {
        put("Section", section);

    }
    public void setstartTime(ParseObject startTime) {

        put("startTime",startTime);
    }
    public ParseObject getTeacher() {
        return getParseObject("Teacher");
    }
    public ParseObject getClassNo() {
        return getParseObject("Class");
    }
    public ParseObject getSection() {
        return getParseObject("Section");
    }
    public ParseObject getSubject() {
        return getParseObject("Subject");
    }


    public ParseObject getstartTime() {
        return getParseObject("startTime");
    }

    public ParseObject getendTime() {
        return getParseObject("endTime");
    }


}