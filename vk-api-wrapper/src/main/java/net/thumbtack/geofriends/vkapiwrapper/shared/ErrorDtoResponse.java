package net.thumbtack.geofriends.vkapiwrapper.shared;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class ErrorDtoResponse {
    private final String errorCode;
    private final String description;
}
