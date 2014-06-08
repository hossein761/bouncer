package models.db;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Model for keeping registration token and its relation to the base
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
@Entity
@Table(name = "registration")
public class RegistrationToken extends Model {
    @Id
    @Column(name = "id")
    public String id;
    @OneToOne(targetEntity = User.class)
    public User user;

    private static Finder<String, RegistrationToken> find = new Finder<String, RegistrationToken>(String.class,
                                                                                                  RegistrationToken.class);
    public RegistrationToken() {
    }

    public static RegistrationToken findById(final String id){
        return find.byId(id);
    }
}
