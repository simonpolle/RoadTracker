package be.ehb.roadtracker.domain;

import com.orm.SugarRecord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties=true)
public class Route
{
    private long id;
    private long user_id;
    private double distance_travelled;
    private double total_cost;
}
