package co.djsanabriac.appgate.appgatetechtest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class StepDTO {

    @Getter @Setter private String type;
    @Getter @Setter private String value;
    @Getter @Setter private Boolean executed;
}
