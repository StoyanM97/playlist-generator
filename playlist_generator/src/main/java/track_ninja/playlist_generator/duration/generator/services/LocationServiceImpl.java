package track_ninja.playlist_generator.duration.generator.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    private static final String API_KEY = "AhkpmnOX3icFulvHmEd1Tv6KKc3wdYq1dlSXOfHnp_ywjE9oi9hpbZitX98kZZzG";
    private static final String LOCATION_PATH = "http://dev.virtualearth.net/REST/v1/Locations/";
    private static final String ADD_SEARCH_KEY_PREFIX = "?key=";

    private static final String DISTANCE_PATH = "https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=";
    private static final String DESTINATIONS = "&destinations=";
    private static final String TRAVEL_MODE_DRIVING = "&travelMode=driving&key=";

    private static final String GEOCODE_POINTS_JSON_FIELD_NAME = "geocodePoints";
    private static final String USAGE_TYPES_JSON_FIELD_NAME = "usageTypes";
    private static final String ROUTE_JSON_VALUE_NAME = "Route";
    private static final String COORDINATES_JSON_FIELD_NAME = "coordinates";
    private static final String RESULTS_JSON_FIELD_NAME = "results";
    private static final String TRAVEL_DURATION_JSON_FIELD_NAME = "travelDuration";

    private static final String DELIMITER = ",";
    private static final String FAILED_TO_READ_THE_COORDINATES_FIELDS = "Failed to read the coordinates fields from the JSON objects with parameters: ";
    private static final String ERROR_DURING_PARSING_THE_JSON_OBJECT = "Error during parsing the Json object";

    private static final String RESPONSE = "Response: ";
    private static final String URL_INFO = "Query URL: ";
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);

    @Autowired
    public LocationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public long getTravelDuration(String startPoint, String endPoint){

        long[] travelDuration = new long[1];

        try{
            String origins = getCoordinatesAsString(startPoint);
            String destinations= getCoordinatesAsString(endPoint);

            String url = DISTANCE_PATH + origins + DESTINATIONS + destinations + TRAVEL_MODE_DRIVING + API_KEY;

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();

            if(response.getStatusCode() == HttpStatus.OK) {

                log.info(URL_INFO + url);
                log.info(RESPONSE + response.getStatusCode());

                try {
                    JsonNode rootNode = objectMapper.readTree(response.getBody());

                    List<JsonNode> results = rootNode.findValues(RESULTS_JSON_FIELD_NAME);
                    results.get(0).forEach(jsonNode ->
                            travelDuration[0] = jsonNode.path(TRAVEL_DURATION_JSON_FIELD_NAME).asLong());


                } catch (IOException e) {
                    log.error(ERROR_DURING_PARSING_THE_JSON_OBJECT);
                }

            }

        }
        catch (NoSuchFieldException e){
            log.error(e.getMessage());
        }

        return travelDuration[0];
    }

    private String getCoordinatesAsString(String location) throws NoSuchFieldException {

        String url = LOCATION_PATH + location + ADD_SEARCH_KEY_PREFIX + API_KEY;

        List<String> coordinates = new ArrayList<>();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper objectMapper = new ObjectMapper();

        if(response.getStatusCode() == HttpStatus.OK) {

            log.info(URL_INFO + url);
            log.info(RESPONSE + response.getStatusCode());

            try {
                JsonNode rootNode = objectMapper.readTree(response.getBody());

                List<JsonNode> points = rootNode.findValues(GEOCODE_POINTS_JSON_FIELD_NAME);

                if (points.get(0).size() > 1) {
                    points.get(0).forEach(jsonNode ->
                            jsonNode.path(USAGE_TYPES_JSON_FIELD_NAME).forEach(type -> {
                                if (type.asText().equals(ROUTE_JSON_VALUE_NAME)) {
                                    jsonNode.path(COORDINATES_JSON_FIELD_NAME).forEach(coordinate->
                                            coordinates.add(coordinate.asText()));
                                }
                            }));
                } else {
                    points.get(0).forEach(jsonNode ->
                                    jsonNode.path(COORDINATES_JSON_FIELD_NAME).forEach(coordinate->
                                            coordinates.add(coordinate.asText())));

                }


            } catch (IOException e) {
                log.error(ERROR_DURING_PARSING_THE_JSON_OBJECT);
            }
        }

        if(coordinates.isEmpty()){

            throw new NoSuchFieldException(FAILED_TO_READ_THE_COORDINATES_FIELDS + location);

        }
        return String.join(DELIMITER, coordinates);
    }

}


