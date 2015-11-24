package se.travappar.api.model;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;


@Entity
@Table(name = "Users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, unique = true)
    Long id;
    @Column(name = "email")
    String email;
    @Column (name = "password")
    String password;
    @Column (name = "device_id")
    String device_id;

    public User() {

    }
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "device_id")
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}