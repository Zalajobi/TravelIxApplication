package travelix.webapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import travelix.webapp.Model.HotelCancellationPolicy;
import travelix.webapp.Model.HotelDetail;
import travelix.webapp.Model.HotelSearchResult;
import travelix.webapp.Model.RoomDetails;
import travelix.webapp.Service.HotelService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/travelix.ng")
public class SearchController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HttpSession session;

    @PostMapping(path = "/hotelsearch")
    public String searchHotelPostRequest(Model model, @RequestParam(name = "destination") String destinationCity,
                                         @RequestParam(name = "checkIn") String checkIn,
                                         @RequestParam(name = "checkOut") String checkOut,
                                         @RequestParam(name = "adults") String adult,
                                         @RequestParam(name = "children") String children,
                                         @RequestParam(name = "child1") String child1,
                                         @RequestParam(name = "child2") String child2,
                                         @RequestParam(name = "child3") String child3,
                                         @RequestParam(name = "child4") String child4,
                                         @RequestParam(name = "longitude") String longitude,
                                         @RequestParam(name = "latitude") String latitude) throws Exception {

        System.out.println("Longitude,Latitude : \t(" + longitude + "," + latitude + ")");

        List<HotelSearchResult> listOfHotels = new ArrayList<>();
        List<String> searchParameters = new ArrayList<>();

        if (Integer.parseInt(children) < 1) {
            //Search for hotel based on arguments and return List of Hotels
            listOfHotels = hotelService.searchHotel(destinationCity, checkIn, checkOut, adult, children);

            //If listOfHotels is Empty
            if (listOfHotels.isEmpty() && !longitude.isBlank() && !latitude.isBlank()) {
                listOfHotels = hotelService.searchHotelByGeoCodes(Double.parseDouble(longitude), Double.parseDouble(latitude), destinationCity, checkIn, checkOut, adult, children);
                model.addAttribute("hotelSearchResult", listOfHotels);
            }
            model.addAttribute("hotelSearchResult", listOfHotels);
        } else {
            listOfHotels = hotelService.searchHotelWithChildren(destinationCity, checkIn, checkOut, adult, children, Integer.parseInt(child1), Integer.parseInt(child2), Integer.parseInt(child3), Integer.parseInt(child4));
            model.addAttribute("hotelSearchResult", listOfHotels);
        }

        if (listOfHotels.size() > 0 || listOfHotels.isEmpty()) {
            searchParameters.clear();
            searchParameters.add(destinationCity);
            searchParameters.add(checkIn);
            searchParameters.add(checkOut);
            searchParameters.add(adult);
            searchParameters.add(children);
            searchParameters.add(child1);
            searchParameters.add(child2);
            searchParameters.add(child3);
            searchParameters.add(child4);

            model.addAttribute("searchParameters", searchParameters);
        }
        //redirect the user to home page if no hotel is found
        if (listOfHotels.isEmpty()) {
            model.addAttribute("emptyHotel", true);
            model.addAttribute("searchParameters",searchParameters);
            return "redirect:";
        }
        model.addAttribute("currency", "#");

        return "hotelsearchresult";
    }

    @GetMapping(path = "/hoteldetail", params = "hotelCode")
    public String hotelDetail(Model model, @RequestParam(name = "hotelCode") String hotelCode) throws Exception {

        hotelService.getHotelDetail(hotelCode);
        HotelDetail hotelDetail = (HotelDetail) session.getAttribute("hotelDetails");
        List<RoomDetails> roomDetails = hotelService.getRoomDetails(hotelDetail.getHotelCode(), hotelDetail.getHotelResultIndex());

        //get the room cancellation policy for each room and display to the client
        List<HotelCancellationPolicy> hotelCancellationPolicy = hotelService.getHotelCancellationPolicyForAllRooms(hotelDetail.getHotelResultIndex(), roomDetails.size());

        //If there is no available room, display no hotel room available
        if (roomDetails.isEmpty()) {
            model.addAttribute("noRoomAvailable", true);
        }

        model.addAttribute("hotelDetails", session.getAttribute("hotelDetails"));
        model.addAttribute("hotelCancellationPolicy", hotelCancellationPolicy);
        model.addAttribute("roomDetails", roomDetails);

        return "hoteldetail";
    }
//
//    @GetMapping(path = "/hoteldetailredirect")
//    public String getHotelDetailRedirect(Model model) throws Exception {
//        hotelService.getHotelDetail((String) session.getAttribute("hotelCode"));
//        HotelDetail hotelDetail = (HotelDetail) session.getAttribute("hotelDetails");
//        List<RoomDetails> roomDetails = hotelService.getRoomDetails(hotelDetail.getHotelCode(), hotelDetail.getHotelResultIndex());
//
//        model.addAttribute("hotelDetails", session.getAttribute("hotelDetails"));
//        model.addAttribute("roomDetails", roomDetails);
//
//        return "hoteldetail";
//    }

//    @GetMapping(path = "/roomcancellationpolicy")
//    public String roomCancellationPolicy(Model model) {
//        HotelCancellationPolicy hotelCancellationPolicy = hotelService.hotelCancellationPolicy();
//    }
}
