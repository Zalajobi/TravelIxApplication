package travelix.webapp.Model;

public class DestinationCity {

    private String countryName;
    private String destinationCity;
    private String imageUrl;
    private String cityCode;

    public DestinationCity() {
    }

    public DestinationCity(String countryName, String destinationCity, String imageUrl, String cityCode) {
        this.countryName = countryName;
        this.destinationCity = destinationCity;
        this.imageUrl = imageUrl;
        this.cityCode = cityCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
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
}
