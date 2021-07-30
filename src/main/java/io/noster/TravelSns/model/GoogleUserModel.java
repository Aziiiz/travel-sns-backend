package io.noster.TravelSns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleUserModel {

//    @JsonProperty("sub")
    private String id;

    @JsonProperty("given_name")
    private String firstName;

    @JsonProperty("family_name")
    private String lastName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private  String email;
}
