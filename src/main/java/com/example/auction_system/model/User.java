import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data; // Optional: from Lombok

@Data // Optional: handles getters, setters, toString, etc.
@Entity
@Table(name = "users") // Use a plural name for the table
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
}