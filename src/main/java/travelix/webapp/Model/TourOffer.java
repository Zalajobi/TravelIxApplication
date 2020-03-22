package travelix.webapp.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TourOffer {
    @Id
    String country;
    String imageUrl;
    String cityNae;
    private String cityCode;

    public TourOffer() {
    }

    public TourOffer(String country, String imageUrl, String cityNae, String cityCode) {
        this.country = country;
        this.imageUrl = imageUrl;
        this.cityNae = cityNae;
        this.cityCode = cityCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityNae() {
        return cityNae;
    }

    public void setCityNae(String cityNae) {
        this.cityNae = cityNae;
    }
}
