package travelix.webapp.Model;

public class HotelCancellationPolicy {

    private String cancellationDeadline;
    private String roomTypeName;
    private String roomIndex;
    private String checkInDate;
    private String checkOutDate;
//    private String cancellationCharge;
    private String defaultPolicy;
    private String autoCancellationText;
    private String hotelNorms;

    public HotelCancellationPolicy() {
    }

    public HotelCancellationPolicy(String cancellationDeadline, String roomTypeName, String roomIndex, String checkInDate, String checkOutDate, String defaultPolicy, String autoCancellationText, String hotelNorms) {
        this.cancellationDeadline = cancellationDeadline;
        this.roomTypeName = roomTypeName;
        this.roomIndex = roomIndex;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
//        this.cancellationCharge = cancellationCharge;
        this.defaultPolicy = defaultPolicy;
        this.autoCancellationText = autoCancellationText;
        this.hotelNorms = hotelNorms;
    }

    public String getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(String cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(String roomIndex) {
        this.roomIndex = roomIndex;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

//    public String getCancellationCharge() {
//        return cancellationCharge;
//    }
//
//    public void setCancellationCharge(String cancellationCharge) {
//        this.cancellationCharge = cancellationCharge;
//    }

    public String getDefaultPolicy() {
        return defaultPolicy;
    }

    public void setDefaultPolicy(String defaultPolicy) {
        this.defaultPolicy = defaultPolicy;
    }

    public String getAutoCancellationText() {
        return autoCancellationText;
    }

    public void setAutoCancellationText(String autoCancellationText) {
        this.autoCancellationText = autoCancellationText;
    }

    public String getHotelNorms() {
        return hotelNorms;
    }

    public void setHotelNorms(String hotelNorms) {
        this.hotelNorms = hotelNorms;
    }
}
