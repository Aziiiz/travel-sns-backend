package io.noster.TravelSns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.noster.TravelSns.payload.LoginMethodEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class FacebookAuthModel {
    @JsonProperty("authToken")
    private String authToken;
}