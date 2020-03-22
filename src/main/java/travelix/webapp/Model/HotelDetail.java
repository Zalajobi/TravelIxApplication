package travelix.webapp.Model;

import java.util.List;

public class HotelDetail {

    private String hotelCode;
    private String hotelName;
    private int hotelRating;
    private String address;
    private String countryName;
    private String hotelDescription;
    private String faxNumber;
    private List<String> hotelFacility;
    private List<String> hotelImageUrl;
    private String map;
    private double longitude;
    private double latitude;
    private String phoneNumber;
    private String pincode;
    private String cityName;
    private int hotelResultIndex;
    private double tripAdvisorRating;

    public HotelDetail() {
    }

    public HotelDetail(String hotelCode, String hotelName, int hotelRating, String address, String countryName, String hotelDescription, List<String> hotelFacility, List<String> hotelImageUrl, String map, double longitude, double latitude, String phoneNumber, String pincode, String cityName, int hotelResultIndex, double tripAdvisorRating) {
        this.hotelCode = hotelCode;
        this.hotelName = hotelName;
        this.hotelRating = hotelRating;
        this.address = address;
        this.countryName = countryName;
        this.hotelDescription = hotelDescription;
        this.hotelFacility = hotelFacility;
        this.hotelImageUrl = hotelImageUrl;
        this.map = map;
        this.phoneNumber = phoneNumber;
        this.pincode = pincode;
        this.cityName = cityName;
        this.hotelResultIndex = hotelResultIndex;
        this.tripAdvisorRating = tripAdvisorRating;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getHotelRating() {
        return hotelRating;
    }

    public void setHotelRating(int hotelRating) {
        this.hotelRating = hotelRating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public List<String> getHotelFacility() {
        return hotelFacility;
    }

    public void setHotelFacility(List<String> hotelFacility) {
        this.hotelFacility = hotelFacility;
    }

    public List<String> getHotelImageUrl() {
        return hotelImageUrl;
    }

    public void setHotelImageUrl(List<String> hotelImageUrl) {
        this.hotelImageUrl = hotelImageUrl;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getHotelResultIndex() {
        return hotelResultIndex;
    }

    public void setHotelResultIndex(int hotelResultIndex) {
        this.hotelResultIndex = hotelResultIndex;
    }

    public double getTripAdvisorRating() {
        return tripAdvisorRating;
    }

    public void setTripAdvisorRating(double tripAdvisorRating) {
        this.tripAdvisorRating = tripAdvisorRating;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}