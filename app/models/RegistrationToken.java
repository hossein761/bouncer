package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * // TODO: Add class description here.
 *
 * @author Hossein Kazemi <a href="mailto:mrhosseinkazemi@gmail.com">mrhosseinkazemi@gmail.com</a>
 */
@Entity()
@Table(name = "registration")
public class RegistrationToken extends Model {
    public String id;
    public String name;
    public String lastName;
    public String email;
    public RegistrationToken() {
    }
}
