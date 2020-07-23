package select.users;
//will use this to convert the objects into Strings
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class User {

    private int id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String email;
    @JsonProperty("ip_address")
    private String ipAddress;
    private double latitude;
    private double longitude;

    public User(){}

    public User(int id,
                String firstName,
                String lastName,
                String email,
                String ipAddress,
                double latitude,
                double longitude) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.ipAddress = ipAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                firstName,
                lastName,
                email,
                ipAddress,
                latitude,
                longitude
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User that = (User) obj;
            return this.id == that.id &&
                    Objects.equals(this.firstName, that.firstName) &&
                    Objects.equals(this.lastName, that.lastName) &&
                    Objects.equals(this.email, that.email) &&
                    Objects.equals(this.ipAddress, that.ipAddress) &&
                    this.latitude == that.latitude &&
                    this.longitude == that.longitude;
        }
        return false;
    }
}
