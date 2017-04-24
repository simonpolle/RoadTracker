package be.ehb.roadtracker.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Simon Pollé on 26/02/2017.
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties=true)
public class AccessTokenResponse
{
    public String access_token;
    public String token_type;
    public String refresh_token;
    public Long expires_in;
}
