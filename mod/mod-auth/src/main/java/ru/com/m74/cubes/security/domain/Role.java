package ru.com.m74.cubes.security.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {
    @Id
    private String id;

    private String title;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
