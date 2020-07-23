package select.users;

import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class)
public class SelectUsersControllerTest {

    @Autowired
    private SelectUsersController sut;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }




    @Test
    public void contextLoads() {

    }


    @Test
    public void shouldReturnNoUsersForEmptyApiResponse() throws Exception {
        List<User> emptyUserList = new ArrayList<>();

        expectApiResponse(
                "https://bpdts-test-app.herokuapp.com/city/London/users",
                emptyUserList
        );
        expectApiResponse(
                "https://bpdts-test-app.herokuapp.com/users",
                emptyUserList
        );

        List<User> users = sut.selectLondonUsers();
        mockServer.verify();
        assertThat(users.isEmpty(), is(true));
    }



    @Test
    public void shouldReturnCorrectUsersIfBothApisReturnSameUsers() throws Exception {
        List<User> fiveLondonUsers = IntStream.iterate(0, i -> i + 1)
                .limit(5)
                .mapToObj(i -> makeRandomUser())
                .collect(Collectors.toList());

        expectApiResponse(
                "https://bpdts-test-app.herokuapp.com/city/London/users",
                fiveLondonUsers
        );

        expectApiResponse(
                "https://bpdts-test-app.herokuapp.com/users",
                fiveLondonUsers
        );

        List<User> users = sut.selectLondonUsers();
        mockServer.verify();
        assertThat(users, is(fiveLondonUsers));
    }

    private void expectApiResponse(String apiUrl, List<User> expectedUsers) throws Exception {
        mockServer.expect(ExpectedCount.once(),
                requestTo(new URI(apiUrl)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(expectedUsers)));
    }


    private User makeRandomUser() {
        Random random = new Random();
        return new User(
                random.nextInt(),
                "FirstName:-" + random.nextInt(),
                "LastName:-" + random.nextInt(),
                "email:-" + random.nextInt(),
                "ipAddress:-" + random.nextInt(),
                random.nextDouble(),
                random.nextDouble()
        );
    }
}
