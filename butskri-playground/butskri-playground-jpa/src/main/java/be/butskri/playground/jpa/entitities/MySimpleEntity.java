package be.butskri.playground.jpa.entitities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "my_simple_entity")
public class MySimpleEntity extends EqualByStateObject {

    @Id
    private UUID id;
    @Column
    private String myString;

    protected MySimpleEntity() {
        // necessary for hibernate
    }

    public MySimpleEntity(UUID id, String myString) {
        this.id = id;
        this.myString = myString;
    }

    public UUID getId() {
        return id;
    }

    public String getMyString() {
        return myString;
    }


}
