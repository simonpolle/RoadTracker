package be.ehb.roadtracker.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties=true)
public class Locations
{
    private List<Location> locations;
}
