package main.java.cz2006project.mojojo.Entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;
import java.util.List;

/**
 * <h1>Appointment Class</h1>
 * This class is a ParseObject 'Appointment' in the Parse database that contains all the details
 * about a particular appointment.
 *
 * <p>
 *
 * @author  Maisurah Shuling
 * @version 1.0
 * @since   2014-03-31
 */
@ParseClassName("Appointment")
public class Appointment extends ParseObject {

    public Appointment() {

    }


    public void setDoctor(ParseObject doctor) {
        put("doctor", doctor);

    }

    public ParseObject getDoctor() {
        return getParseObject("doctor");
    }

    public void setClinic(ParseObject clinic) {
        put("clinic", clinic);
    }

    public ParseObject getClinic() {
        return getParseObject("clinic");
    }

    public void setPatient(ParseObject patient) {
        put("patient", patient);
    }

    public ParseObject getPatient() {
        return getParseObject("patient");
    }


}