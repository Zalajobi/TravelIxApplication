package travelix.webapp.Service.implementation;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.*;
import travelix.webapp.Controller.LoginController;
import travelix.webapp.Model.*;
import travelix.webapp.Repository.TourOfferRepository;
import travelix.webapp.Repository.UserRepository;
import travelix.webapp.Service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import travelix.webapp.Repository.AvailableCountryRepository;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class HotelServiceImplementation implements HotelService {

    @Autowired
    private AvailableCountryRepository availableCountryRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TourOfferRepository tourOfferRepository;
    @Autowired
    private LoginController loginController;
    @Autowired
    private HttpSession session;

    Document hotelDetailDocument = null;
    List<HotelSearchResult> hotelSearchResultList = new ArrayList<>();

    //Get all available country from TboHolidays
    @Override
    public void fetchAvailableCountryFromTBOHolidays() {
        //XML query code
        String xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                "\t<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\"></hot:Credentials>\n" +
                "\t<wsa:Action>http://TekTravel/HotelBookingApi/CountryList</wsa:Action>\n" +
                "\t<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                "</soap:Header>\n" +
                "<soap:Body>\n" +
                "\t<hot:CountryListRequest/>\n" +
                "</soap:Body>\n" +
                "</soap:Envelope>";

        //Get All Countries and create a new Object from it
        if (availableCountryRepository.findAll().isEmpty()) {
            parseCountry(queryTBOHolidaysAPI(xml));
        }
    }

    @Override
    public void fetchTopDestination() {
        try {
            //XML query code
            String xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                    "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                    "\t<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\"></hot:Credentials>\n" +
                    "\t<wsa:Action>http://TekTravel/HotelBookingApi/TopDestinations</wsa:Action>\n" +
                    "\t<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                    "</soap:Header>\n" +
                    "<soap:Body>\n" +
                    "\t<hot:TopDestinationRequest/>\n" +
                    "</soap:Body>\n" +
                    "</soap:Envelope>";

            session.setAttribute("topDestination", parseTopDestinationList(queryTBOHolidaysAPI(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HotelSearchResult> searchHotel(String destination, String checkIn, String checkOut, String adult, String children) {
        hotelSearchResultList.clear();
        String destinationCountry = null;
        List<CountryList> countryLists = availableCountryRepository.findAll();
        String destinationCountryCode = null;
        String destinationCityName = null;
        String guessCountryCode = null;
        String destinationCityCode = null;
        String destinationList[] = destination.split(", ");

//        List<String> array =  Arrays.asList(countryLists.stream().filter(filter -> filter.getCountryCode().equalsIgnoreCase(destinationList[0]) && filter.getCountryName().contains(destinationList[1])).toArray());
//
//        List<String> stringList = (List<String>) Arrays.asList(destinationList (filter -> ))

        try {
            for (int country = 0; country < destinationList.length; country++) {
                for (int counter = 0; counter < countryLists.size(); counter++) {
                    if (countryLists.get(counter).getCountryName().equalsIgnoreCase(destinationList[country])) {
                        destinationCountry = countryLists.get(counter).getCountryName();
                        //If country name does not match the one in the database
                        destinationCountryCode = countryLists.get(counter).getCountryCode();
                        break;
                    }
                } if (destinationList[country].equalsIgnoreCase("USA") || destinationList[country].equalsIgnoreCase("United State") || destinationList[country].equalsIgnoreCase("US") || destinationList[country].equalsIgnoreCase("United state of America") || destinationList[country].equalsIgnoreCase("United States of America")) {
                    destinationCountry = "United States";
                    destinationCountryCode = "US";
                } else if (destinationList[country].equalsIgnoreCase("UAE") || destinationList[country].equalsIgnoreCase("United Arab Emirates")) {
                    destinationCountry = "United Arab Emirate";
                    destinationCityCode = "AE";
                }
            }
            //destinationCountry = countryLists.stream().filter(countryList -> countryList.getCountryName().equalsIgnoreCase(destinationList[country])).collect(Collectors.toList());
//            Get the Destination Country and Code
//            List<String> destinationCountryAndCode = getDestinationCountryCode(destinationList, countryLists);

            List<String> destinationCityDetails = getDestinationCityCode(destinationCountryCode, destination);
            if (destinationCityDetails.size() > 1) {
                destinationCityName = destinationCityDetails.get(0);
                destinationCityCode = destinationCityDetails.get(1);
            }

            //Get LoggedIn User
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            //get the user nationality code
            if(authentication.getName().equalsIgnoreCase("anonymousUser")) {
                guessCountryCode = "NG";
            } else {
                guessCountryCode = guessNationalityCountryCode(userRepository.findUserByUserName(authentication.getName()).getNationality());
            }


            //XML query
            String xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                    "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                    "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                    "</hot:Credentials>\n" +
                    "<wsa:Action>http://TekTravel/HotelBookingApi/HotelSearch</wsa:Action>\n" +
                    "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                    "</soap:Header>\n" +
                    "<soap:Body>\n" +
                    "<hot:HotelSearchRequest>\n" +
                    "<hot:CheckInDate>" + checkIn + "</hot:CheckInDate>\n" +
                    "<hot:CheckOutDate>" + checkOut + "</hot:CheckOutDate>\n" +
                    "<hot:CountryName>" + destinationCountry + "</hot:CountryName>\n" +
                    "<hot:CityName>" + destinationCityName + " </hot:CityName>\n" +
                    "<hot:CityId>" + destinationCityCode + "</hot:CityId>\n" +
                    "<hot:IsNearBySearchAllowed>false</hot:IsNearBySearchAllowed>\n" +
                    "<hot:NoOfRooms>1</hot:NoOfRooms>\n" +
                    "<hot:GuestNationality>" + guessCountryCode + "</hot:GuestNationality>\n" +
                    "<hot:RoomGuests>\n" +
                    "<hot:RoomGuest AdultCount=\"" + adult + "\" ChildCount=\"" + children + "\">\n" +
                    "</hot:RoomGuest>\n" +
                    "</hot:RoomGuests>\n" +
                    "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                    "<hot:ResultCount>0</hot:ResultCount>\n" +
                    "<hot:Filters>\n" +
                    "<hot:StarRating>All</hot:StarRating>\n" +
                    "<hot:OrderBy>PriceDesc</hot:OrderBy>\n" +
                    "</hot:Filters>\n" +
                    "<hot:ResponseTime>17</hot:ResponseTime>\n" +
                    "</hot:HotelSearchRequest>\n" +
                    "</soap:Body>\n" +
                    "</soap:Envelope>";

            //Get Hotel search results
            Document document = queryTBOHolidaysAPI(xml);
            hotelSearchResultList = parseHotelSearchList(document);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return hotelSearchResultList;
        }
    }

//    private List<String> getDestinationCountryCode(String[] destinationList, List<CountryList> countryLists) {
//
//    }

    @Override
    public void getHotelDetail(String hotelCode) {
        HotelDetail hotelDetail = new HotelDetail();
        //Get LoggedIn User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //get the user nationality codes
        if(authentication.getName().equalsIgnoreCase("anonymousUser")) {
//            session.setAttribute("redirectToHotelDetail", true);
//            session.setAttribute("hotelCode", hotelCode);
//            loginController.redirectToHotelDetail();
        }

        String xml = "<soap:Envelope xmlns:hot=\"http://TekTravel/HotelBookingApi\" xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "      <soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">\n" +
                "        <hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\"></hot:Credentials>\n" +
                "        <wsa:Action>http://TekTravel/HotelBookingApi/HotelDetails</wsa:Action>\n" +
                "        <wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                "      </soap:Header>\n" +
                "      <soap:Body>\n" +
                "        <hot:HotelDetailsRequest>\n" +
                "        \t<hot:HotelCode>" + hotelCode + "</hot:HotelCode>\n" +
                "        </hot:HotelDetailsRequest>\n" +
                "      </soap:Body>\n" +
                "    </soap:Envelope>";

        session.setAttribute("hotelDetails", parseHotelDetails(queryTBOHolidaysAPI(xml)));
    }

    @Override
    public List<RoomDetails> getRoomDetails(String hotelCode, int hotelResultIndex) {
        List<RoomDetails> roomDetails = new ArrayList<>();
        roomDetails.clear();

        String xmlRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                "</hot:Credentials>\n" +
                "<wsa:Action>http://TekTravel/HotelBookingApi/AvailableHotelRooms</wsa:Action>\n" +
                "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                "</soap:Header>\n" +
                "<soap:Body>\n" +
                "<hot:HotelRoomAvailabilityRequest>\n" +
                "<hot:SessionId>" + session.getAttribute("sessionId") + "</hot:SessionId>\n" +
                "<hot:ResultIndex>" + hotelResultIndex + "</hot:ResultIndex>\n" +
                "<hot:HotelCode>" + hotelCode + "</hot:HotelCode>\n" +
                "<hot:ResponseTime>0</hot:ResponseTime>\n" +
                "</hot:HotelRoomAvailabilityRequest>\n" +
                "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                "</soap:Body>\n" +
                "</soap:Envelope>";

        roomDetails = parseRoomDetails(queryTBOHolidaysAPI(xmlRequest));

        return roomDetails;
    }

    @Override
    public List<DestinationCity> getTopTour() {

        List<DestinationCity> destinationCityList = (List<DestinationCity>) session.getAttribute("topDestination");
        List<DestinationCity> tourOfferList = new ArrayList<>();

        for (int count = 0; count < 3; count++) {
            String cityName = destinationCityList.get(count).getDestinationCity();
            String country = destinationCityList.get(count).getCountryName();
            String cityCode = destinationCityList.get(count).getCityCode();
            String countryImage = tourOfferRepository.findByCountry(country).getImageUrl();

            tourOfferList.add(new DestinationCity(country, cityName, countryImage, cityCode));
        }

        return tourOfferList;
    }

    @Override
    public List<HotelSearchResult> searchHotelWithChildren(String destination, String checkIn, String checkOut, String adult, String children, int child1, int child2, int child3, int child4) {
        List<HotelSearchResult> hotelSearchResultList = new ArrayList<>();
        List<CountryList> countryLists = availableCountryRepository.findAll();
        hotelSearchResultList.clear();
        //get City Id
        String guessCountryCode = null;
        String destinationCountry = null;
        String destinationCountryCode = null;
        String destinationCityName = null;
        String destinationCityCode = null;

        String destinationList[] = destination.split(", ");

        for (int country = 0; country < destinationList.length; country++) {
            for (int counter = 0; counter < countryLists.size(); counter++) {
                if (countryLists.get(counter).getCountryName().equalsIgnoreCase(destinationList[country])) {
                    destinationCountry = countryLists.get(counter).getCountryName();
                    destinationCountryCode = countryLists.get(counter).getCountryCode();
                }
            }
        }

        List<String> destinationCityDetails = getDestinationCityCode(destinationCountryCode, destination);
        destinationCityName = destinationCityDetails.get(0);
        destinationCityCode = destinationCityDetails.get(1);

        //Get LoggedIn User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getName().equalsIgnoreCase("anonymousUser"))
        {
            guessCountryCode = "NG";
        } else {
            //get the user nationality code
            guessCountryCode = guessNationalityCountryCode(userRepository.findUserByUserName(authentication.getName()).getNationality());
        }

        switch (Integer.parseInt(children)) {
            case 1:
                //XML query
                String child1xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                        "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                        "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                        "</hot:Credentials>\n" +
                        "<wsa:Action>http://TekTravel/HotelBookingApi/HotelSearch</wsa:Action>\n" +
                        "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                        "</soap:Header>\n" +
                        "<soap:Body>\n" +
                        "<hot:HotelSearchRequest>\n" +
                        "<hot:CheckInDate>" + checkIn + "</hot:CheckInDate>\n" +
                        "<hot:CheckOutDate>" + checkOut + "</hot:CheckOutDate>\n" +
                        "<hot:CountryName>" + destinationCountry + "</hot:CountryName>\n" +
                        "<hot:CityName>" + destinationCityName + " </hot:CityName>\n" +
                        "<hot:CityId>" + destinationCityCode + "</hot:CityId>\n" +
                        "<hot:IsNearBySearchAllowed>false</hot:IsNearBySearchAllowed>\n" +
                        "<hot:NoOfRooms>1</hot:NoOfRooms>\n" +
                        "<hot:GuestNationality>" + guessCountryCode + "</hot:GuestNationality>\n" +
                        "<hot:RoomGuests>\n" +
                        "<hot:RoomGuest AdultCount=\"" + adult + "\" ChildCount=\"" + children + "\">\n" +
                        "<hot:ChildAge>" +
                        "<hot:int>" + child1 + "</hot:int>" +
                        "</hot:ChildAge>" +
                        "</hot:RoomGuest>\n" +
                        "</hot:RoomGuests>\n" +
                        "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                        "<hot:ResultCount>0</hot:ResultCount>\n" +
                        "<hot:Filters>\n" +
                        "<hot:StarRating>All</hot:StarRating>\n" +
                        "<hot:OrderBy>PriceDesc</hot:OrderBy>\n" +
                        "</hot:Filters>\n" +
                        "<hot:ResponseTime>17</hot:ResponseTime>\n" +
                        "</hot:HotelSearchRequest>\n" +
                        "</soap:Body>\n" +
                        "</soap:Envelope>";
                //Get Hotel search results
                Document child1Document = queryTBOHolidaysAPI(child1xml);
                hotelSearchResultList = parseHotelSearchList(child1Document);
                break;
            case 2:
                //XML query
                String child2xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                        "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                        "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                        "</hot:Credentials>\n" +
                        "<wsa:Action>http://TekTravel/HotelBookingApi/HotelSearch</wsa:Action>\n" +
                        "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                        "</soap:Header>\n" +
                        "<soap:Body>\n" +
                        "<hot:HotelSearchRequest>\n" +
                        "<hot:CheckInDate>" + checkIn + "</hot:CheckInDate>\n" +
                        "<hot:CheckOutDate>" + checkOut + "</hot:CheckOutDate>\n" +
                        "<hot:CountryName>" + destinationCountry + "</hot:CountryName>\n" +
                        "<hot:CityName>" + destinationCityName + " </hot:CityName>\n" +
                        "<hot:CityId>" + destinationCityCode + "</hot:CityId>\n" +
                        "<hot:IsNearBySearchAllowed>false</hot:IsNearBySearchAllowed>\n" +
                        "<hot:NoOfRooms>1</hot:NoOfRooms>\n" +
                        "<hot:GuestNationality>" + guessCountryCode + "</hot:GuestNationality>\n" +
                        "<hot:RoomGuests>\n" +
                        "<hot:RoomGuest AdultCount=\"" + adult + "\" ChildCount=\"" + children + "\">\n" +
                        "<hot:ChildAge>" +
                        "<hot:int>" + child1 + "</hot:int>" +
                        "<hot:int>" + child2 + "</hot:int>" +
                        "</hot:ChildAge>" +
                        "</hot:RoomGuest>\n" +
                        "</hot:RoomGuests>\n" +
                        "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                        "<hot:ResultCount>0</hot:ResultCount>\n" +
                        "<hot:Filters>\n" +
                        "<hot:StarRating>All</hot:StarRating>\n" +
                        "<hot:OrderBy>PriceDesc</hot:OrderBy>\n" +
                        "</hot:Filters>\n" +
                        "<hot:ResponseTime>17</hot:ResponseTime>\n" +
                        "</hot:HotelSearchRequest>\n" +
                        "</soap:Body>\n" +
                        "</soap:Envelope>";
                //Get Hotel search results
                Document child2Document = queryTBOHolidaysAPI(child2xml);
                hotelSearchResultList = parseHotelSearchList(child2Document);
                break;
            case 3:
                //XML query
                String child3xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                        "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                        "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                        "</hot:Credentials>\n" +
                        "<wsa:Action>http://TekTravel/HotelBookingApi/HotelSearch</wsa:Action>\n" +
                        "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                        "</soap:Header>\n" +
                        "<soap:Body>\n" +
                        "<hot:HotelSearchRequest>\n" +
                        "<hot:CheckInDate>" + checkIn + "</hot:CheckInDate>\n" +
                        "<hot:CheckOutDate>" + checkOut + "</hot:CheckOutDate>\n" +
                        "<hot:CountryName>" + destinationCountry + "</hot:CountryName>\n" +
                        "<hot:CityName>" + destinationCityName + " </hot:CityName>\n" +
                        "<hot:CityId>" + destinationCityCode + "</hot:CityId>\n" +
                        "<hot:IsNearBySearchAllowed>false</hot:IsNearBySearchAllowed>\n" +
                        "<hot:NoOfRooms>1</hot:NoOfRooms>\n" +
                        "<hot:GuestNationality>" + guessCountryCode + "</hot:GuestNationality>\n" +
                        "<hot:RoomGuests>\n" +
                        "<hot:RoomGuest AdultCount=\"" + adult + "\" ChildCount=\"" + children + "\">\n" +
                        "<hot:ChildAge>" +
                        "<hot:int>" + child1 + "</hot:int>" +
                        "<hot:int>" + child2 + "</hot:int>" +
                        "<hot:int>" + child3 + "</hot:int>" +
                        "</hot:ChildAge>" +
                        "</hot:RoomGuest>\n" +
                        "</hot:RoomGuests>\n" +
                        "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                        "<hot:ResultCount>0</hot:ResultCount>\n" +
                        "<hot:Filters>\n" +
                        "<hot:StarRating>All</hot:StarRating>\n" +
                        "<hot:OrderBy>PriceDesc</hot:OrderBy>\n" +
                        "</hot:Filters>\n" +
                        "<hot:ResponseTime>17</hot:ResponseTime>\n" +
                        "</hot:HotelSearchRequest>\n" +
                        "</soap:Body>\n" +
                        "</soap:Envelope>";
                //Get Hotel search results
                Document child3Document = queryTBOHolidaysAPI(child3xml);
                hotelSearchResultList = parseHotelSearchList(child3Document);
                break;
            case 4:
                //XML query
                String child4xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                        "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                        "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                        "</hot:Credentials>\n" +
                        "<wsa:Action>http://TekTravel/HotelBookingApi/HotelSearch</wsa:Action>\n" +
                        "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                        "</soap:Header>\n" +
                        "<soap:Body>\n" +
                        "<hot:HotelSearchRequest>\n" +
                        "<hot:CheckInDate>" + checkIn + "</hot:CheckInDate>\n" +
                        "<hot:CheckOutDate>" + checkOut + "</hot:CheckOutDate>\n" +
                        "<hot:CountryName>" + destinationCountry + "</hot:CountryName>\n" +
                        "<hot:CityName>" + destinationCityName + " </hot:CityName>\n" +
                        "<hot:CityId>" + destinationCityCode + "</hot:CityId>\n" +
                        "<hot:IsNearBySearchAllowed>false</hot:IsNearBySearchAllowed>\n" +
                        "<hot:NoOfRooms>1</hot:NoOfRooms>\n" +
                        "<hot:GuestNationality>" + guessCountryCode + "</hot:GuestNationality>\n" +
                        "<hot:RoomGuests>\n" +
                        "<hot:RoomGuest AdultCount=\"" + adult + "\" ChildCount=\"" + children + "\">\n" +
                        "<hot:ChildAge>" +
                        "<hot:int>" + child1 + "</hot:int>" +
                        "<hot:int>" + child2 + "</hot:int>" +
                        "<hot:int>" + child3 + "</hot:int>" +
                        "<hot:int>" + child4 + "</hot:int>" +
                        "</hot:ChildAge>" +
                        "</hot:RoomGuest>\n" +
                        "</hot:RoomGuests>\n" +
                        "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                        "<hot:ResultCount>0</hot:ResultCount>\n" +
                        "<hot:Filters>\n" +
                        "<hot:StarRating>All</hot:StarRating>\n" +
                        "<hot:OrderBy>PriceDesc</hot:OrderBy>\n" +
                        "</hot:Filters>\n" +
                        "<hot:ResponseTime>17</hot:ResponseTime>\n" +
                        "</hot:HotelSearchRequest>\n" +
                        "</soap:Body>\n" +
                        "</soap:Envelope>";
                //Get Hotel search results
                Document child4Document = queryTBOHolidaysAPI(child4xml);
                hotelSearchResultList = parseHotelSearchList(child4Document);
                break;
        }
        return hotelSearchResultList;
    }

    @Override
    public List<HotelSearchResult> searchHotelByGeoCodes(double longitude, double latitude, String destinationCity, String checkIn, String checkOut, String adult, String children) {
        List<HotelSearchResult> hotelSearchResultList = new ArrayList<>();
        hotelSearchResultList.clear();
        List<CountryList> countryLists = availableCountryRepository.findAll();
        String destinationCountryCode = null;
        String guessCountryCode = null;


        //Get LoggedIn User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //get the user nationality codes
        if(authentication.getName().equalsIgnoreCase("anonymousUser")) {
            guessCountryCode = "NG";
        } else {
            guessCountryCode = guessNationalityCountryCode(userRepository.findUserByUserName(authentication.getName()).getNationality());
        }


        //Get the CountryCode
        String[] destination = destinationCity.split(", ");

        for (int count = 0; count < destination.length; count++) {
            for (int country = 0; country < countryLists.size(); country++) {
                if (destination[count].equalsIgnoreCase(countryLists.get(country).getCountryName())) {
                    destinationCountryCode = countryLists.get(country).getCountryCode();
                }
            }
        }


        String xmlQuery = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                "</hot:Credentials>\n" +
                "<wsa:Action>http://TekTravel/HotelBookingApi/HotelSearch</wsa:Action>\n" +
                "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                "</soap:Header>\n" +
                "<soap:Body>\n" +
                "<hot:HotelSearchRequest>\n" +
                "<hot:CheckInDate>" + checkIn + "</hot:CheckInDate>\n" +
                "<hot:CheckOutDate>" + checkOut + "</hot:CheckOutDate>\n" +
                "<hot:IsNearBySearchAllowed>false</hot:IsNearBySearchAllowed>\n" +
                "<hot:NoOfRooms>1</hot:NoOfRooms>\n" +
                "<hot:GuestNationality>" + guessCountryCode + "</hot:GuestNationality>\n" +
                "<hot:RoomGuests>\n" +
                "<hot:RoomGuest AdultCount=\"" + adult + "\" ChildCount=\"" + children + "\">\n" +
                "</hot:RoomGuest>\n" +
                "</hot:RoomGuests>\n" +
                "<hot:ResultCount>0</hot:ResultCount>\n" +
                "<hot:Filters>\n" +
                "<hot:StarRating>All</hot:StarRating>\n" +
                "<hot:OrderBy>PriceAsc</hot:OrderBy>\n" +
                "</hot:Filters>\n" +
                "<hot:GeoCodes>\n" +
                "<hot:Latitude>" + latitude + "</hot:Latitude>\n" +
                "<hot:Longitude>" + longitude + "</hot:Longitude>\n" +
                "<hot:SearchRadius>20</hot:SearchRadius>\n" +
                "<hot:CountryCode>" + destinationCountryCode + "</hot:CountryCode>\n" +
                "</hot:GeoCodes>\n" +
                "<hot:ResponseTime>17</hot:ResponseTime>\n" +
                "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                "</hot:HotelSearchRequest>\n" +
                "</soap:Body>\n" +
                "</soap:Envelope>";

        hotelSearchResultList = parseHotelSearchList(queryTBOHolidaysAPI(xmlQuery));

        return hotelSearchResultList;
    }

    @Override
    public List<HotelSearchResult> searchHotelForTour(String checkIn, String checkOut, String country, String cityName, String cityCode, String adult, String child) {
        List<HotelSearchResult> hotelSearchResultList = new ArrayList<>();
        hotelSearchResultList.clear();
        String guessCountryCode = null;

        //Get LoggedIn User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //get the user nationality code
        if(authentication.getName().equalsIgnoreCase("anonymousUser")) {
            guessCountryCode = "NG";
        } else {
            guessCountryCode = guessNationalityCountryCode(userRepository.findUserByUserName(authentication.getName()).getNationality());
        }

        String xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                "</hot:Credentials>\n" +
                "<wsa:Action>http://TekTravel/HotelBookingApi/HotelSearch</wsa:Action>\n" +
                "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                "</soap:Header>\n" +
                "<soap:Body>\n" +
                "<hot:HotelSearchRequest>\n" +
                "<hot:CheckInDate>" + checkIn + "</hot:CheckInDate>\n" +
                "<hot:CheckOutDate>" + checkOut + "</hot:CheckOutDate>\n" +
                "<hot:CountryName>" + country + "</hot:CountryName>\n" +
                "<hot:CityName>" + cityName + "</hot:CityName>\n" +
                "<hot:CityId>" + cityCode + "</hot:CityId>\n" +
                "<hot:IsNearBySearchAllowed>false</hot:IsNearBySearchAllowed>\n" +
                "<hot:NoOfRooms>1</hot:NoOfRooms>\n" +
                "<hot:GuestNationality>" + guessCountryCode + "</hot:GuestNationality>\n" +
                "<hot:RoomGuests>\n" +
                "<hot:RoomGuest AdultCount=\"" + adult + "\" ChildCount=\"" + child + "\">\n" +
                "</hot:RoomGuest>\n" +
                "</hot:RoomGuests>\n" +
                "<hot:PreferredCurrencyCode>NGN</hot:PreferredCurrencyCode>\n" +
                "<hot:ResultCount>0</hot:ResultCount>\n" +
                "<hot:Filters>\n" +
                "<hot:StarRating>All</hot:StarRating>\n" +
                "<hot:OrderBy>PriceAsc</hot:OrderBy>\n" +
                "</hot:Filters>\n" +
                "<hot:ResponseTime>17</hot:ResponseTime>\n" +
                "</hot:HotelSearchRequest>\n" +
                "</soap:Body>\n" +
                "</soap:Envelope>";

        //Get Hotel search results
        Document document = queryTBOHolidaysAPI(xml);
        hotelSearchResultList = parseHotelSearchList(document);

        return hotelSearchResultList;
    }

    @Override
    public List<HotelCancellationPolicy> getHotelCancellationPolicyForAllRooms(int hotelResultIndex, int noOfRoomsAvailable) {
        List<HotelCancellationPolicy> hotelCancellationPolicyList = new ArrayList<>();
        hotelCancellationPolicyList.clear();

        try {
            for (int count = 1; count <= noOfRoomsAvailable; count++) {
                String xmlSoapRequest = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                        "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                        "<hot:Credentials UserName=\"quantum\" Password=\"Qua@83309425\">\n" +
                        "</hot:Credentials>\n" +
                        "<wsa:Action>http://TekTravel/HotelBookingApi/HotelCancellationPolicy</wsa:Action>\n" +
                        "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                        "</soap:Header>\n" +
                        "<soap:Body>\n" +
                        "<hot:HotelCancellationPolicyRequest>\n" +
                        "<hot:ResultIndex>" + hotelResultIndex + "</hot:ResultIndex>\n" +
                        "<hot:SessionId>" + session.getAttribute("sessionId") + "</hot:SessionId>\n" +
                        "<hot:OptionsForBooking>\n" +
                        "<hot:FixedFormat>false</hot:FixedFormat>\n" +
                        "<hot:RoomCombination>\n" +
                        "<hot:RoomIndex>" + count + "</hot:RoomIndex>\n" +
                        "</hot:RoomCombination>\n" +
                        "</hot:OptionsForBooking>\n" +
                        "</hot:HotelCancellationPolicyRequest>\n" +
                        "</soap:Body>\n" +
                        "</soap:Envelope>";

                hotelCancellationPolicyList.add(new HotelCancellationPolicy());
                hotelCancellationPolicyList.add(parseHotelCancellationPolicyForAllRooms(queryTBOHolidaysAPI(xmlSoapRequest)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return hotelCancellationPolicyList;
        }
    }

    private HotelCancellationPolicy parseHotelCancellationPolicyForAllRooms(Document document) {

        String lastCancellationDeadline = document.getDocumentElement().getElementsByTagName("LastCancellationDeadline").item(0).getTextContent();
        String roomTypeName = document.getDocumentElement().getElementsByTagName("CancelPolicy").item(0).getAttributes().getNamedItem("RoomTypeName").getNodeValue();
        String roomIndex = document.getDocumentElement().getElementsByTagName("CancelPolicy").item(0).getAttributes().getNamedItem("RoomIndex").getNodeValue();
        String checkInDate = document.getDocumentElement().getElementsByTagName("CancelPolicy").item(0).getAttributes().getNamedItem("FromDate").getNodeValue();
        String checkOutDate = document.getDocumentElement().getElementsByTagName("CancelPolicy").item(0).getAttributes().getNamedItem("ToDate").getNodeValue();
        String defaultPolicy = document.getDocumentElement().getElementsByTagName("DefaultPolicy").item(0).getTextContent();
        String autoCancellationText = null;
        if (document.getDocumentElement().getElementsByTagName("AutoCancellationText").item(0) != null)
            autoCancellationText = document.getDocumentElement().getElementsByTagName("AutoCancellationText").item(0).getTextContent();
        String hotelNorms = null;
        if (document.getDocumentElement().getElementsByTagName("string").item(0) != null)
            hotelNorms = document.getDocumentElement().getElementsByTagName("string").item(0).getTextContent();

        return new HotelCancellationPolicy(lastCancellationDeadline, roomTypeName, roomIndex, checkInDate, checkOutDate, defaultPolicy, autoCancellationText, hotelNorms);
    }

    //Parse Hotel Details
    private HotelDetail parseHotelDetails(Document document) {
        hotelDetailDocument = document;
        HotelDetail hotelDetail = null;
        List<String> facilities = new ArrayList<>();
        List<String> hotelImageUrl = new ArrayList<>();

        NodeList hotelDetailResultNodeList = document.getDocumentElement().getElementsByTagName("HotelDetailsResponse");
        NodeList hotelFacilitiesNodeList = document.getDocumentElement().getElementsByTagName("HotelFacility");
        NodeList hotelImagesNodeList = document.getDocumentElement().getElementsByTagName("ImageUrls");

        String hotelDescription = document.getDocumentElement().getElementsByTagName("Description").item(1).getTextContent();

        //Add the hotel facilities
        for (int count = 0; count < hotelFacilitiesNodeList.getLength(); count++) {
            Node hotelFacilityNode = hotelFacilitiesNodeList.item(count);

            if (hotelFacilityNode.getNodeType() == Node.ELEMENT_NODE) {

                for (int i = 0; i < document.getDocumentElement().getElementsByTagName("HotelFacility").getLength(); i++) {
                    facilities.add(document.getDocumentElement().getElementsByTagName("HotelFacility").item(i).getTextContent());
                }
            }
        }

        //Add the HotelImageURL
        for (int count = 0; count < hotelImagesNodeList.getLength(); count++) {
            Node hotelFacilityNode = hotelImagesNodeList.item(count);

            if (hotelFacilityNode.getNodeType() == Node.ELEMENT_NODE) {
                for (int i = 0; i < document.getDocumentElement().getElementsByTagName("ImageUrl").getLength(); i++) {
                    hotelImageUrl.add(document.getDocumentElement().getElementsByTagName("ImageUrl").item(i).getTextContent());
                }
            }
        }

        //get the hotel result index number
        int hotelIndexNumber = 0;

        for (int count = 0; count < hotelSearchResultList.size(); count++) {
            if (hotelSearchResultList.get(count).getHotelName().equalsIgnoreCase(document.getDocumentElement().getElementsByTagName("HotelDetails").item(0).getAttributes().getNamedItem("HotelName").getNodeValue())) {
                hotelIndexNumber = hotelSearchResultList.get(count).getHotelResultIndex();
            }
        }

        //get other hotelDetails information
        for (int count = 0; count < hotelDetailResultNodeList.getLength(); count++) {
            Node hotelDetailNode = hotelDetailResultNodeList.item(count);

            if (hotelDetailNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) hotelDetailNode;

                int rating = 0;
                String hotelCode = element.getElementsByTagName("HotelDetails").item(0).getAttributes().getNamedItem("HotelCode").getNodeValue();
                String hotelName = element.getElementsByTagName("HotelDetails").item(0).getAttributes().getNamedItem("HotelName").getNodeValue();
                String hotelRating = element.getElementsByTagName("HotelDetails").item(0).getAttributes().getNamedItem("HotelRating").getNodeValue();
                String hotelAddress = element.getElementsByTagName("Address").item(0).getTextContent();
                String countryName = element.getElementsByTagName("CountryName").item(0).getTextContent();
                String map = element.getElementsByTagName("Map").item(0).getTextContent();
                String longLang[] = map.split("\\|");
                double longitude = Double.parseDouble(longLang[0]);
                double latitude = Double.parseDouble(longLang[1]);
                String phoneNumber = element.getElementsByTagName("PhoneNumber").item(0).getTextContent();
                String pincode = element.getElementsByTagName("PinCode").item(0).getTextContent();
                String cityName = element.getElementsByTagName("CityName").item(0).getTextContent();
                double tripAdvisorRating = 0;
                if (element.getElementsByTagName("TripAdvisorRating").item(0) != null) {
                    tripAdvisorRating = Double.valueOf(element.getElementsByTagName("TripAdvisorRating").item(0).getTextContent());
                }

                //Set the Rating
                if (hotelRating.equalsIgnoreCase("OneStar"))
                    rating = 1;
                else if (hotelRating.equalsIgnoreCase("TwoStar"))
                    rating = 2;
                else if (hotelRating.equalsIgnoreCase("ThreeStar"))
                    rating = 3;
                else if (hotelRating.equalsIgnoreCase("FourStar"))
                    rating = 4;
                else if (hotelRating.equalsIgnoreCase("FiveStar"))
                    rating = 5;

                hotelDetail = new HotelDetail(hotelCode, hotelName, rating, hotelAddress, countryName, hotelDescription, facilities, hotelImageUrl, map, longitude, latitude, phoneNumber, pincode, cityName, hotelIndexNumber, tripAdvisorRating);
            }
        }
        return hotelDetail;
    }

    //Parse Room Details
    private List<RoomDetails> parseRoomDetails(Document document) {
        List<RoomDetails> roomDetailsList = new ArrayList<>();

        NodeList roomDetailsNodeList = document.getDocumentElement().getElementsByTagName("HotelRoom");

        for (int count = 0; count < roomDetailsNodeList.getLength(); count++) {
            Node roomDetailsNode = roomDetailsNodeList.item(count);

            if (roomDetailsNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) roomDetailsNode;

                int roomIndex = Integer.parseInt(element.getElementsByTagName("RoomIndex").item(0).getTextContent());
                String roomTypeName = element.getElementsByTagName("RoomTypeName").item(0).getTextContent();
                String roomTypeCode = element.getElementsByTagName("RoomTypeCode").item(0).getTextContent();
                String ratePlanCode = element.getElementsByTagName("RatePlanCode").item(0).getTextContent();
                double totalPriceDuringLodging = Math.round(Double.parseDouble(element.getElementsByTagName("RoomRate").item(0).getAttributes().getNamedItem("PrefPrice").getNodeValue()));
                double pricePerNight = Math.round(Double.parseDouble(element.getElementsByTagName("DayRate").item(0).getAttributes().getNamedItem("PrefBaseFare").getNodeValue()));
                String imageUrl = null;
                String ameneties = element.getElementsByTagName("Amenities").item(0).getTextContent();

                if (element.getElementsByTagName("URL").item(0) == null) {
                    imageUrl = "No Image";
                } else {
                    imageUrl = element.getElementsByTagName("URL").item(0).getTextContent();
                }

                roomDetailsList.add(new RoomDetails(roomIndex, roomTypeName, roomTypeCode, totalPriceDuringLodging, imageUrl, ameneties, ratePlanCode, pricePerNight));
            }
        }

        return roomDetailsList;
    }

//    Parse Countries
    private void parseCountry(Document document) {
        //List to hold Countries;
        List<CountryList> countryLists = new ArrayList<>();

        //Get the NodeList of the element <Country> from the Document
        NodeList nodeListOfCountries = document.getDocumentElement().getElementsByTagName("Country");


        //Loop through all the countries and get the countryName and CountryCode
        for (int count = 0; count < nodeListOfCountries.getLength(); count++) {
            Node nodeOfEachCountry = nodeListOfCountries.item(count);

            if (nodeOfEachCountry.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nodeOfEachCountry;

                //Get Country Name
                String countryName = element.getAttributes().getNamedItem("CountryName").getNodeValue();
                //Get Country Code
                String countryCode = element.getAttributes().getNamedItem("CountryCode").getNodeValue();
                //Add to the CountryLists Array
                countryLists.add(new CountryList(countryName, countryCode));
            }
        }

        //Save the CountryName and CountryCode in the Database
        if (availableCountryRepository.findAll().isEmpty()) {
            for (int count = 0; count < countryLists.size(); count++) {
                availableCountryRepository.save(countryLists.get(count));
            }
        }
    }

    private List<HotelSearchResult> parseHotelSearchList(Document document) {

        List<HotelSearchResult> hotelSearchResultList = new ArrayList<>();
        hotelSearchResultList.clear();

        try {
            //get hotel result List
            NodeList hotelResultList = document.getElementsByTagName("HotelResult");
            session.setAttribute("sessionId", document.getDocumentElement().getElementsByTagName("SessionId").item(0).getTextContent());
            System.out.println(session.getAttribute("sessionId"));

            for (int count = 0; count < hotelResultList.getLength(); count++) {
                Node hotelResult = hotelResultList.item(count);

                if (hotelResult.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) hotelResult;

                    int hotelRating = 0;
                    String hotelName = element.getElementsByTagName("HotelName").item(0).getTextContent();
                    String hotelPicture = null;
                    if (element.getElementsByTagName("HotelPicture").item(0) == null) {
                        hotelPicture = "No Image";
                    } else {
                        hotelPicture = element.getElementsByTagName("HotelPicture").item(0).getTextContent();
                    }
                    String hotelDescription = element.getElementsByTagName("HotelDescription").item(0).getTextContent();
                    String hotelAddress = element.getElementsByTagName("HotelAddress").item(0).getTextContent();
                    String rating = element.getElementsByTagName("Rating").item(0).getTextContent();
                    String hotelCode = element.getElementsByTagName("HotelCode").item(0).getTextContent();
                    String hotelPrice = element.getElementsByTagName("MinHotelPrice").item(0).getAttributes().getNamedItem("PrefPrice").getNodeValue();
                    boolean halalHotel = Boolean.parseBoolean(element.getElementsByTagName("IsHalal").item(0).getTextContent());
                    String longitude = element.getElementsByTagName("Longitude").item(0).getTextContent();
                    String latitude = element.getElementsByTagName("Latitude").item(0).getTextContent();
                    int hotelResultIndex = Integer.parseInt(element.getElementsByTagName("ResultIndex").item(0).getTextContent());
                    double tripAdvisorRating = 0;
                    if (element.getElementsByTagName("TripAdvisorRating").item(0) != null) {
                        tripAdvisorRating = Double.valueOf(element.getElementsByTagName("TripAdvisorRating").item(0).getTextContent());
                    }
                    String tripAdvisorReviewURL = "#";
                    if (element.getElementsByTagName("TripAdvisorReviewURL").item(0) != null) {
                        tripAdvisorReviewURL = element.getElementsByTagName("TripAdvisorReviewURL").item(0).getTextContent();
                    }

                    //Set the Rating
                    if (rating.equalsIgnoreCase("OneStar"))
                        hotelRating = 1;
                    else if (rating.equalsIgnoreCase("TwoStar"))
                        hotelRating = 2;
                    else if (rating.equalsIgnoreCase("ThreeStar"))
                        hotelRating = 3;
                    else if (rating.equalsIgnoreCase("FourStar"))
                        hotelRating = 4;
                    else if (rating.equalsIgnoreCase("FiveStar"))
                        hotelRating = 5;

                    //Add hotel result
                    hotelSearchResultList.add(new HotelSearchResult(hotelName, hotelPicture, hotelDescription, hotelAddress, hotelRating, hotelCode, hotelPrice, halalHotel, longitude, latitude, tripAdvisorRating, tripAdvisorReviewURL, hotelResultIndex));
                }
            }
        } catch (DOMException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            return hotelSearchResultList;
        }
    }

    private String guessNationalityCountryCode(String nationality) {
        List<CountryList> countryLists = availableCountryRepository.findAll();
        String countryCode = null;

        for (int count = 0; count < countryLists.size(); count++) {
            if (countryLists.get(count).getCountryName().equalsIgnoreCase(nationality))
                countryCode = countryLists.get(count).getCountryCode();
        }
        return countryCode;
    }


    private List<String> getDestinationCityCode(String destinationCountryCode, String destination) {
        List<String> returnResults = new ArrayList<>();
        returnResults.clear();
        String cityCode = null;
        String cityName = null;
        String[] destinationList = destination.split(", ");

        String xml = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hot=\"http://TekTravel/HotelBookingApi\">\n" +
                "<soap:Header xmlns:wsa='http://www.w3.org/2005/08/addressing' >\n" +
                "<hot:Credentials UserName=\"RoyalConnectTravelsTest\" Password=\"Roy@45262537\">\n" +
                "</hot:Credentials>\n" +
                "<wsa:Action>http://TekTravel/HotelBookingApi/DestinationCityList</wsa:Action>\n" +
                "<wsa:To>https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc</wsa:To>\n" +
                "</soap:Header>\n" +
                "<soap:Body>\n" +
                "<hot:DestinationCityListRequest>\n" +
                "<hot:CountryCode>" + destinationCountryCode + "</hot:CountryCode>\n" +
                "<hot:ReturnNewCityCodes>true</hot:ReturnNewCityCodes>\n" +
                "</hot:DestinationCityListRequest>\n" +
                "</soap:Body>\n" +
                "</soap:Envelope>";

        Document document = null;
        document = queryTBOHolidaysAPI(xml);
        NodeList destinationCitiesList = document.getElementsByTagName("City");

        for (int count = 0; count < destinationCitiesList.getLength(); count++) {
            Node node = destinationCitiesList.item(count);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String[] citiesList = element.getAttributes().getNamedItem("CityName").getNodeValue().split(",   ");

                cityName = element.getAttributes().getNamedItem("CityName").getNodeValue();
                cityCode = element.getAttributes().getNamedItem("CityCode").getNodeValue();

                for (int i = 0; i < destinationList.length; i++) {

                    for (int j = 0; j < citiesList.length ; j++) {
                        if (citiesList[j].equalsIgnoreCase(destinationList[i])) {
                            returnResults.add(cityName);
                            returnResults.add(cityCode);
                        }
                    }
                }
            }
        }
        return returnResults;
    }

    //Parse top Destination list
    private List<DestinationCity> parseTopDestinationList(Document document) {
        List<DestinationCity> topDestinationCities = new ArrayList<>();
        //Get the nodes
        NodeList nodeList = document.getDocumentElement().getElementsByTagName("City");

        for (int count = 0; count < nodeList.getLength(); count++) {
            Node node = nodeList.item(count);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                String cityName = element.getAttributes().getNamedItem("CityName").getNodeValue();
                String countryName = element.getAttributes().getNamedItem("CountryName").getNodeValue();
                TourOffer tourOffer = tourOfferRepository.findByCountry(countryName);
                String cityCode = element.getAttributes().getNamedItem("CityCode").getNodeValue();

                topDestinationCities.add(new DestinationCity(countryName, cityName, tourOffer.getImageUrl(), cityCode));
            }
        }
        return topDestinationCities;
    }

    //Query TboHolidays API and return Document objects
    public Document queryTBOHolidaysAPI(String xml) {

        Document document = null;
        try {
            String url = "https://api.tbotechnology.in/hotelapi_v7/hotelservice.svc";

            URL obj = new URL(url);
            //connect to the url
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            //Set Post Request method
            connection.setRequestMethod("POST");
            //Set request property
            connection.setRequestProperty("Content-Type","application/soap+xml; charset=UTF-8");

            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(xml);
            outputStream.flush();
            outputStream.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }

            //Get the String value of the Response
            String soapResponse = response.toString();
            System.out.println(soapResponse);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            //convert the value String to document
            document = documentBuilder.parse(new InputSource(new StringReader(soapResponse)));
            document.normalize();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            return document;
        }
    }
}