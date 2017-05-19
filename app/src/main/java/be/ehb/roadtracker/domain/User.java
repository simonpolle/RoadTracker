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
public class User extends SugarRecord
{
    private String first_name;
    private String last_name;
    private String email;
    private int role_id;
    private int company_id;
}
