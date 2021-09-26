package co.djsanabriac.appgate.appgatetechtest.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StepDTO {

    @Getter @Setter private String type;
    @Getter @Setter private String value;
    @Getter @Setter private Boolean executed;
}
