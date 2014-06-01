package models;

import play.db.ebean.Model;

import javax.persistence.*;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
@Entity()
@Table(name = "registration")
public class RegistrationToken extends Model {
    @Id
    @Column(name = "id")
    public String id;
    @OneToOne(targetEntity = BaseUser.class)
    public BaseUser baseUser;

    private static Finder<String, RegistrationToken> find = new Finder<String, RegistrationToken>(String.class,
                                                                                                  RegistrationToken.class);
    public RegistrationToken() {
    }

    public static RegistrationToken findById(final String id){
        return find.byId(id);
    }
}
