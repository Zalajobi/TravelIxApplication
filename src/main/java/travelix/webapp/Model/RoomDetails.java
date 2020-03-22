package travelix.webapp.Model;

import java.util.List;

public class RoomDetails {

    private int roomIndex;
    private String roomTypeName;
    private String roomTypeCode;
    private double totalPriceDuringLodging;
    private String roomImage;
    private String ameneties;
    private String ratePlanCode;
    private double pricePerNight;

    public RoomDetails() {
    }

    public RoomDetails(int roomIndex, String roomTypeName, String roomTypeCode, double totalPriceDuringLodging, String roomImage, String ameneties, String ratePlanCode, double pricePerNight) {
        this.roomIndex = roomIndex;
        this.roomTypeName = roomTypeName;
        this.roomTypeCode = roomTypeCode;
        this.totalPriceDuringLodging = totalPriceDuringLodging;
        this.roomImage = roomImage;
        this.ameneties = ameneties;
        this.ratePlanCode = ratePlanCode;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public String getRoomTypeCode() {
        return roomTypeCode;
    }

    public void setRoomTypeCode(String roomTypeCode) {
        this.roomTypeCode = roomTypeCode;
    }

    public double getTotalPriceDuringLodging() {
        return totalPriceDuringLodging;
    }

    public void setTotalPriceDuringLodging(double totalPriceDuringLodging) {
        this.totalPriceDuringLodging = totalPriceDuringLodging;
    }

    public String getRoomImage() {
        return roomImage;
    }

    public void setRoomImage(String roomImage) {
        this.roomImage = roomImage;
    }

    public String getAmeneties() {
        return ameneties;
    }

    public void setAmeneties(String ameneties) {
        this.ameneties = ameneties;
    }

    public String getRatePlanCode() {
        return ratePlanCode;
    }

    public void setRatePlanCode(String ratePlanCode) {
        this.ratePlanCode = ratePlanCode;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}