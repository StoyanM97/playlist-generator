package track_ninja.playlist_generator.models.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import track_ninja.playlist_generator.models.commons.UserRole;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEditUserByAdminDTO extends RegistrationDTO{

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
