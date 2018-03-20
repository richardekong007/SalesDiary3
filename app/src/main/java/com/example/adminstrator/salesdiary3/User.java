package com.example.adminstrator.salesdiary3;

import java.io.Serializable;

/**
 * Created by Adminstrator on 6/15/2016.
 */
public class User implements Serializable

{
    //instance variable declaration
    private int id;
    private String username;
    private String email;
    private String password;

    public User()
    {

    }
    public User(int id, String username, String password, String email)
    {
        this.id=id;
        this.username=username;
        this.email=email;
        this.password=password;
    }
    public User(int id, String username, String email)
    {
        this.id=id;
        this.username=username;
        this.email=email;
    }
    public User(String username, String email, String password)
    {

        this.username=username;
        this.email=email;
        this.password=password;
    }
    public User(String username, String password)
    {
        this.username=username;
        this.password=password;
    }
    public User(String password)
    {
        this.password=password;
    }

    public int getId()
    {
        return this.id;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public String getEmail()
    {
        return this.email;
    }
    public void setemail(String email)
    {
        this.email=email;
    }
    public String getUsername()
    {
        return this.username;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }
    public String getPassword()
    {
        return this.password;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }


}
