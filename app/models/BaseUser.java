package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import java.sql.Timestamp;

/**
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("BaseUser")
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
    @Column(name = "email" , unique = true)
    public String email;
    @Column(name = "user_name", unique = true)
    public String userName;
    @Column(name = "password_hash")
    public String passwordHash;
    @Column(name = "salt")
    public String salt; // see https://crackstation.net/hashing-security.htm
    @Column(name = "iterations")
    public int iterations;
    @Column(name = "last_login_time")
    public Timestamp lastLoginTime;
    @Column(name = "status")
    public Status status;
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

    public static BaseUser findById(final String id){
        return find.byId(id);
    }

    @Override
    public String toString() {
        return "BaseUser{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", salt='" + salt + '\'' +
                ", iterations=" + iterations +
                ", lastLoginTime=" + lastLoginTime +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                '}';
    }
}
