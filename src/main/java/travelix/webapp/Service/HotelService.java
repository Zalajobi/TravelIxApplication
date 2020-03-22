package travelix.webapp.Service;

import travelix.webapp.Model.*;

import java.util.List;

public interface HotelService {

    void fetchAvailableCountryFromTBOHolidays() throws Exception;

    void fetchTopDestination() throws Exception;

    List<HotelSearchResult> searchHotel(String destinationCity, String checkIn, String checkOut, String adult, String children) throws Exception;

    void getHotelDetail(String hotelCode) throws Exception;

    List<RoomDetails> getRoomDetails(String hotelCode, int hotelResultIndex) throws Exception;

    List<DestinationCity> getTopTour();

    List<HotelSearchResult> searchHotelWithChildren(String destination, String checkIn, String checkOut, String adult, String children, int child1, int child2, int child3, int child4) throws Exception;

    List<HotelSearchResult> searchHotelByGeoCodes(double longitude, double latitude, String destinationCity, String checkIn, String checkOut, String adult, String children) throws Exception;

    List<HotelSearchResult> searchHotelForTour(String checkIn, String checkOut, String country, String cityName, String cityCode, String adult, String child) throws Exception;

    List<HotelCancellationPolicy> getHotelCancellationPolicyForAllRooms(int hotelResultIndex, int size);
}