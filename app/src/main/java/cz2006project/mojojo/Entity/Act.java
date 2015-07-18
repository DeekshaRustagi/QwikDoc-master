package main.java.cz2006project.mojojo.Entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;


@ParseClassName("Act")
public class Act extends ParseObject {

    public Act() {

    }


    public void setCoordinator(ParseObject coordinator) {
        put("Coordinator", coordinator);

    }
    public void setClassNo(ParseObject Class) {
        put("Class", Class);

    }
    public void setSection(ParseObject section) {
        put("Section", section);

    }
    public void setType(ParseObject type) {
        put("ActivityType", type);

    }
    public void setDate(ParseObject date) {
        put("ActivityDate", date);

    }

    public ParseObject getCoordinator() {

        return getParseObject("Coordinator");
    }

    public ParseObject getClassNo() {
        return getParseObject("Class");
    }

    

    public ParseObject getSection() {
        return getParseObject("Section");
    }

    public ParseObject getType(){
        return getParseObject("ActivityType");}

    public ParseObject getdate(){return getParseObject("ActivityDate");}


}