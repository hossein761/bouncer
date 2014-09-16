package models.db;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import models.Status;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.sql.Timestamp;

/**
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
@Entity
@Table(name = "user")
public class User extends Model {

    @Id
    @Column(name = "id")
    public String id;
    @Column(name = "first_name", nullable = false)
    public String firstName;
    @Column(name = "last_name", nullable = false)
    public String lastName;
    @Column(name = "full_name")
    public String fullName;
    @Column(name = "email" , unique = true, nullable = false)
    public String email;
    @Column(name = "user_name", unique = true, nullable = false)
    public String userName;
    @Column(name = "phone_number")
    public String phoneNumber;
    @Column(name = "address")
    public String address;
    @Column(name = "postal_code")
    public String postalCode;
    @Column(name = "city")
    public String city;
    @Column(name = "country")
    public String country;
    @Column(name = "geo_location")
    public String geoLocation;
    @Column(name = "about")
    public String about;
    @Column(name = "profile_img_url", unique = true)
    public String profileImageUrl;
    @Column(name = "password_hash", nullable = false)
    public String passwordHash;
    @Column(name = "salt", nullable = false)
    public String salt; // see https://crackstation.net/hashing-security.htm
    @Column(name = "iterations", nullable = false)
    public int iterations;
    @Column(name = "user_type")
    public String userType; // an optional column
    @Column(name = "last_login_time")
    public Timestamp lastLoginTime;
    @Column(name = "status", nullable = false)
    public Status status;
    @CreatedTimestamp
    @Column(name = "created_time")
    public Timestamp createdTime;
    @UpdatedTimestamp
    @Column(name = "updated_time")
    public Timestamp updatedTime;

    public static Finder<String, User> find = new Finder<String, User>(String.class, User.class);

    public static User findByEmail(final String email) {
        return find.where().eq("email",email).findUnique();
    }

    public static User findByUserName(final String userName){
        return find.where().eq("user_name", userName).findUnique();
    }

    public static User findById(final String id){
        return find.byId(id);
    }

}
