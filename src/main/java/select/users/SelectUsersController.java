/*Task is to build an API which calls the API
https://bpdts-test-app.herokuapp.com/,
and return people who are listed as either living in London
or whose current coordinates are within 50 miles of London
*/
package select.users;

import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SelectUsersController {

    private static final double LONDON_LATITUDE = 51.509865;
    private static final double LONDON_LONGITUDE = -0.118092;
    private static final double METERS_PER_MILE = 1609.34;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/users/London/select")
    public List<User> selectLondonUsers() {

        List<User> selectedUsers = londonUsers();
        List<User> allOtherUsers =
                allUsersExcluding(selectedUsers)
                        .stream()
                        .filter(user -> withIn50MilesOfLondon(user))
                        .collect(Collectors.toList());

        selectedUsers.addAll(allOtherUsers);
        return selectedUsers;
    }

    private boolean withIn50MilesOfLondon(User user) {
        GeodesicData data = Geodesic.WGS84.Inverse(
                LONDON_LATITUDE,
                LONDON_LONGITUDE,
                user.getLatitude(),
                user.getLongitude());

        double distanceInMeters = data.s12;
        double distanceInMiles = distanceInMeters / METERS_PER_MILE;

        return distanceInMiles <= 50;

    }

    private List<User> londonUsers() {
        return collectUsersFromApi(
                "https://bpdts-test-app.herokuapp.com/city/London/users"
        );
    }

    private List<User> allUsersExcluding(List<User> excludeUsers) {
        List<User> allUsers = collectUsersFromApi("https://bpdts-test-app.herokuapp.com/users");
        allUsers.removeAll(excludeUsers);
        return allUsers;
    }

    private List<User> collectUsersFromApi(String url) {
        ResponseEntity<List<User>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                });
        return response.getBody();
    }
}