package se.travappar.api.model;

import java.io.Serializable;

public class HelloWorld implements Serializable{
    String field;

    public HelloWorld() {
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
