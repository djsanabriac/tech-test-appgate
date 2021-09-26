package co.djsanabriac.appgate.appgatetechtest.model.entity;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
public class Session {

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Getter
    @Setter
    private String sid;
}
