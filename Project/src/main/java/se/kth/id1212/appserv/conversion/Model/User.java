// stores the amount of conversion done using the app
package se.kth.id1212.appserv.conversion.Model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Required by JPA, should not be used.
     */
    protected User() {
    }
}