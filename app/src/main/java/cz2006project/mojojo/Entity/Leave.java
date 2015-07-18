package main.java.cz2006project.mojojo.Entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * <h1>Leave Class</h1>
 * This class is a ParseObject 'Leave' in the Parse database that contains all the details
 * about a particular appointment.
 *
 * <p>
 *
 * @author  Maisurah Shuling
 * @version 1.0
 * @since   2014-03-31
 */
@ParseClassName("Leave")
public class Leave extends ParseObject {

    public Leave() {

    }


    public void setTeacher(ParseObject teacher) {
        put("Teacher", teacher);

    }
    public void setReason(ParseObject reason) {
        put("Reason", reason);

    }

    public void setType(ParseObject type) {
        put("LeaveType", type);

    }
    public void setDate(ParseObject date) {
        put("LeaveDate", date);

    }

    public ParseObject getteacher() {

        return getParseObject("Teacher");
    }

    public ParseObject getReason() {
        return getParseObject("Reason");
    }


    public ParseObject getType(){
        return getParseObject("LeaveType");}

    public ParseObject getdate(){return getParseObject("LeaveDate");}



}