package track_ninja.location_generator.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodePoint {

    private List<Double> coordinates;
    private List<String> usageTypes;

    @Override
    public String toString() {
        return "GeocodePoint{" +
                "coordinates=" + coordinates +
                ", usageTypes=" + usageTypes +
                '}';
    }
}
