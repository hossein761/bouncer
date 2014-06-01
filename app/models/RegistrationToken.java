package models;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    @Column(name = "name")
    public String name;
    @Column(name = "lastName")
    public String lastName;
    @Column(name = "email")
    public String email;
    @Column(name = "status")
    public RegistrationStatus status;

    private static Finder<String, RegistrationToken> find = new Finder<String, RegistrationToken>(String.class,
                                                                                                  RegistrationToken.class);
    public RegistrationToken() {
    }

    public static RegistrationToken findById(final String id){
        return find.byId(id);
    }
}
