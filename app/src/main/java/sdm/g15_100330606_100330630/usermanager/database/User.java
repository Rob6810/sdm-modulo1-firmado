package sdm.g15_100330606_100330630.usermanager.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import sdm.g15_100330606_100330630.usermanager.Constant;

@Entity(tableName = Constant.DB_NAME)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "registered")
    private String registered;
    @ColumnInfo(name = "gender")
    private String gender;
    @ColumnInfo(name = "picture")
    private String picture;
    @ColumnInfo(name = "username")
    private String username;
    @ColumnInfo(name = "password")
    private String password;
    @ColumnInfo(name = "location")
    private String location;

    public User(String name, String registered, String gender, String picture, String username, String password, String location) {
        this.name       = name;
        this.registered = registered;
        this.gender     = gender;
        this.picture    = picture;
        this.username   = username;
        this.password   = password;
        this.location   = location;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
