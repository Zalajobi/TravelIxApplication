package travelix.webapp.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "AvailableCountry")
public class CountryList
{
    @Id
    private String countryCode;
    private String countryName;

    public CountryList() {
    }

    public CountryList(String countryName, String countryCode) {
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}