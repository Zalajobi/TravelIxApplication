package travelix.webapp.Model;

public class HotelSearchResult {

    private String hotelName;
    private String hotelPicture;
    private String hotelDescription;
    private String hotelAddress;
    private int hotelRatings;
    private String hotelCode;
    private String hotelPrice;
    private boolean isHotelHalal;
    private String longitude;
    private String latitude;
//    private String tagId;
    private double tripAdvisorRating;
    private String tripAdvisorReviewURL;
    private int hotelResultIndex;

    public HotelSearchResult() {
    }

    public HotelSearchResult(String hotelName, String hotelPicture, String hotelDescription, String hotelAddress, int hotelRatings, String hotelCode, String hotelPrice, boolean isHotelHalal, String longitude, String latitude, double tripAdvisorRating, String tripAdvisorReviewURL, int hotelResultIndex) {
        this.hotelName = hotelName;
        this.hotelPicture = hotelPicture;
        this.hotelDescription = hotelDescription;
        this.hotelAddress = hotelAddress;
        this.hotelRatings = hotelRatings;
        this.hotelCode = hotelCode;
        this.hotelPrice = hotelPrice;
        this.isHotelHalal = isHotelHalal;
        this.longitude = longitude;
        this.latitude = latitude;
//        this.tagId = tagId;
        this.tripAdvisorRating = tripAdvisorRating;
        this.tripAdvisorRating = tripAdvisorRating;
        this.hotelResultIndex = hotelResultIndex;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelPicture() {
        return hotelPicture;
    }

    public void setHotelPicture(String hotelPicture) {
        this.hotelPicture = hotelPicture;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public int getHotelRatings() {
        return hotelRatings;
    }

    public void setHotelRatings(int hotelRatings) {
        this.hotelRatings = hotelRatings;
    }

    public String getHotelCode() {
        return hotelCode;
    }

    public void setHotelCode(String hotelCode) {
        this.hotelCode = hotelCode;
    }

    public String getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(String hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public boolean isHotelHalal() {
        return isHotelHalal;
    }

    public void setHotelHalal(boolean hotelHalal) {
        isHotelHalal = hotelHalal;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

//    public String getTagId() {
//        return tagId;
//    }
//
//    public void setTagId(String tagId) {
//        this.tagId = tagId;
//    }

    public double getTripAdvisorRating() {
        return tripAdvisorRating;
    }

    public void setTripAdvisorRating(double tripAdvisorRating) {
        this.tripAdvisorRating = tripAdvisorRating;
    }

    public String getTripAdvisorReviewURL() {
        return tripAdvisorReviewURL;
    }

    public void setTripAdvisorReviewURL(String tripAdvisorReviewURL) {
        this.tripAdvisorReviewURL = tripAdvisorReviewURL;
    }

    public int getHotelResultIndex() {
        return hotelResultIndex;
    }

    public void setHotelResultIndex(int hotelResultIndex) {
        this.hotelResultIndex = hotelResultIndex;
    }
}
