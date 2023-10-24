package com.example.sch;

public class Helper {

    String name,username,password,subject;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Helper(String name,  String username, String password,String  subject) {
        this.name = name;

        this.username = username;

        this.password = password;
        this.subject = subject;
    }

    public Helper() {
    }
}
