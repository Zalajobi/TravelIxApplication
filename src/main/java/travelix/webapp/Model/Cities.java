package travelix.webapp.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cities {
    @Id
    String cityName;
    String countryName;
    String imageUrl;

    public Cities() {
    }

    public Cities(String cityName, String countryName, String imageUrl) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.imageUrl = imageUrl;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}