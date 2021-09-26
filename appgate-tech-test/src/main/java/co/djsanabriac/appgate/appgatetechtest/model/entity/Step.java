package co.djsanabriac.appgate.appgatetechtest.model.entity;

import co.djsanabriac.appgate.appgatetechtest.model.dto.StepDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries(
        @NamedQuery(name = "Step.getAllBySessionSid", query = "SELECT s FROM Step s, Session ses where s.sid = ses and ses.sid = :sid")
)
public class Step {

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    @Getter
    @Setter
    private String type;

    @Column
    @Getter
    @Setter
    private String value;

    @Column
    @Getter
    @Setter
    private Boolean executed;

    @ManyToOne
    @JoinColumn(name="sid")
    @Getter @Setter private Session sid;

    public Step fromStepDTO(StepDTO stepDTO){
        this.type=stepDTO.getType();
        this.value=stepDTO.getValue();
        this.executed=stepDTO.getExecuted();
        return this;
    }

}
