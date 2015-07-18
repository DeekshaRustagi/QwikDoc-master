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
        put("COORDINATOR", coordinator);

    }
    public void setClassNo(ParseObject Class) {
        put("CLASS", Class);

    }
    public void setSection(ParseObject section) {
        put("SECTION", section);

    }
    public void setType(ParseObject type) {
        put("ACTIVITYTYPE", type);

    }
    public void setDate(ParseObject date) {
        put("ACTIVITYDATE", date);

    }

    public ParseObject getCoordinator() {

        return getParseObject("COORDINATOR");
    }

    public ParseObject getClassNo() {
        return getParseObject("CLASS");
    }

    

    public ParseObject getSection() {
        return getParseObject("SECTION");
    }

    public ParseObject getType(){
        return getParseObject("ACTIVITYTYPE");}

    public ParseObject getdate(){return getParseObject("ACTIVITYDATE");}


}