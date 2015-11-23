package se.travappar.api.model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "hello")
public class HelloWorld implements Serializable{

    @Column(name = "id", nullable = false, unique = true)
    Long id;
    @Column(name = "field")
    String field;

    public HelloWorld() {
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "field")
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
