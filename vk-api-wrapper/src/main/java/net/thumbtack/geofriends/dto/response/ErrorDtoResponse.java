package net.thumbtack.geofriends.dto.response;

import java.util.Objects;

public class ErrorDtoResponse {
    private final String errorCode;
    private final String description;

    public ErrorDtoResponse(String errorCode, String description) {
        this.errorCode = errorCode;
        this.description = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ErrorDtoResponse))
            return false;
        ErrorDtoResponse that = (ErrorDtoResponse) o;
        return Objects.equals(getErrorCode(), that.getErrorCode()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getErrorCode(), getDescription());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorDtoResponse{");
        sb.append("errorCode='").append(errorCode).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
