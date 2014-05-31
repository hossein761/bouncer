package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

/**
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
@MappedSuperclass
public class BaseUser extends Model {

    @Id
    @Column(name = "id")
    public String id;
    @Column(name = "name")
    public String name;
    @Column(name = "last_name")
    public String lastName;
    @Column(name = "full_name")
    public String fullName;
    @Column(name = "email")
    public String email;
    @Column(name = "user_name")
    public String userName;
    @Column(name = "password_hash")
    public String passwordHash;
    @Column(name = "salt")
    public String salt; // see https://crackstation.net/hashing-security.htm
    @Column(name = "iterations")
    public int iterations;
    @Column(name = "last_login_time")
    public Timestamp lastLoginTime;
    @CreatedTimestamp
    @Column(name = "created_time")
    public Timestamp createdTime;
    @UpdatedTimestamp
    @Column(name = "updated_time")
    public Timestamp updatedTime;

    private static Finder<String, BaseUser> find = new Finder<String, BaseUser>(String.class, BaseUser.class);

    public static BaseUser findByEmail(final String email) {
        return find.where().eq("email",email).findUnique();
    }

    public static BaseUser findByUserName(final String userName){
        return find.where().eq("user_name", userName).findUnique();
    }
}
