package ru.com.m74.cubes.springboot.example.jdbc.domain;


import ru.com.m74.cubes.jdbc.Link;
import ru.com.m74.cubes.jdbc.annotations.Column;
import ru.com.m74.cubes.jdbc.annotations.Id;
import ru.com.m74.cubes.jdbc.annotations.Table;

import java.util.Date;

@Table("REMOTE_USER usr")
public class User {
    @Id
    @Column("ID")
    private Long id;

    @Column("LOGIN")
    private String login;

    @Column("PASSWORD")
    private String password;

    @Column("NAME")
    private String name;

    @Column("CREATE_AT")
    private Date createAt;

    @Column("LAST_ACCESS")
    private Date lastAccess;

    @Column("COMMENTS")
    private String comments;

    /**
     * Действующий или нет
     */
    @Column("IS_ACTIVE")
    private boolean active = false;

//    @LinkTo(table = "USER_TYPE", titleQuery = "type.TITLE || '-' || type.ID")
//    @Column("TYPE_ID")
    private Link type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Link getType() {
        return type;
    }

    public void setType(Link type) {
        this.type = type;
    }
}
