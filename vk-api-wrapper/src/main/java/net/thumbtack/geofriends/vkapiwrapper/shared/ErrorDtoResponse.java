package net.thumbtack.geofriends.vkapiwrapper.shared;

import lombok.Data;

@Data
public class ErrorDtoResponse {
    private final String errorCode;
    private final String description;
}
