package track_ninja.location_generator.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import track_ninja.location_generator.models.GeocodePoint;
import track_ninja.location_generator.models.Location;
import track_ninja.location_generator.models.Resource;
import track_ninja.location_generator.models.Resources;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    private static final String API_KEY = "AhkpmnOX3icFulvHmEd1Tv6KKc3wdYq1dlSXOfHnp_ywjE9oi9hpbZitX98kZZzG";
    private static final String LOCATION_URL = "http://dev.virtualearth.net/REST/v1/Locations/";
    private static final String ADD_API_KEY = "?key=";
    private static final String GET_DISTANCE_URL = "https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=${longitudeStart},${latitudeStart}&destinations=${longitudeEnd},${latitudeEnd}&travelMode=driving&key=${bingMapsKey}";


//    http://dev.virtualearth.net/REST/v1/Locations/${addressFrom}?key=${bingMapsKey},
//    http://dev.virtualearth.net/REST/v1/Locations/${addressTo}?key=${bingMapsKey},

    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    public LocationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public boolean getLocation() {

        Location result = restTemplate.getForObject("http://dev.virtualearth.net/REST/v1/Locations?countryRegion=Bulgaria&adminDistrict=Sofia City\n" +
                "&locality=Sofia&postalCode=1784&addressLine=Bulevard Aleksandar Malinov 31\n" +
                "&key=AhkpmnOX3icFulvHmEd1Tv6KKc3wdYq1dlSXOfHnp_ywjE9oi9hpbZitX98kZZzG", Location.class);

        if(result!=null){
            List<Resources> resourcesSet = result.getResourceSets();
            List<Resource>  resources = resourcesSet.get(0).getResources();
            List<GeocodePoint> geocodePoints = resources.get(0).getGeocodePoints();
            
            geocodePoints.forEach(System.out::println);
            
            log.info("");
            return true;
        }
        else{
            log.error("");
            return false;
        }

    }
}


