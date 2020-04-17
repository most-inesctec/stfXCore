package stfXCore.Models.Employee;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Lombok annotation to create all the getters, setters, equals, hash, and toString methods, based on the fields
 */
@Data
/**
 * JPA annotation to make this object ready for storage in a JPA-based data store
 */
@Entity
public class Employee {

    /**
     * Primary key automatically populated by the JPA provider.
     */
    private @Id @GeneratedValue Long id;
    private String name;
    private String role;

    public Employee() {}

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }
}