package be.butskri.playground.jpa.entitities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "my_simple_entity")
public class MySimpleEntity extends EqualByStateObject {

    @Id
    private UUID id;
    @Column
    private String myString;

    private MySimpleEntity() {
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
