package be.stijnhooft.portal.notifications.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor @NoArgsConstructor
public class NotificationReadStatus {

    @NonNull
    private Long id;

    @NonNull
    private Boolean read;

}
