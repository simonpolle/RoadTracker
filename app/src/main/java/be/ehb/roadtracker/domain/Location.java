package be.ehb.roadtracker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties=true)
public class Location
{
    public Location(double latitude, double longitude, long route_id)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.route_id = route_id;
    }

    private long id;
    private double latitude;
    private double longitude;
    private long route_id;
}
