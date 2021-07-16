package io.noster.TravelSns.model;

import io.noster.TravelSns.payload.LoginMethodEnum;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class FacebookAuthModel {
    private String authToken;
}