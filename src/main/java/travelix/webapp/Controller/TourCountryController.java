package travelix.webapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import travelix.webapp.Model.HotelSearchResult;
import travelix.webapp.Service.HotelService;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/travelix.ng")
public class TourCountryController {

    @Autowired
    private HotelService hotelService;

    @Autowired
    private HttpSession session;

    @RequestMapping(path = "/tourdestination", method = RequestMethod.GET)
    public String tourDestination(Model model, @RequestParam(name = "country", required = false) String country, @RequestParam(name = "cityName", required = false) String cityName, @RequestParam(name = "cityCode", required = false) String cityCode) throws Exception {
        List<HotelSearchResult> hotelSearchResultList = new ArrayList<>();

        //set the checkIn and checkOut date
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime checkInDate = currentDate.plusDays(01);
        LocalDateTime checkOutDate = currentDate.plusDays(02);
        String checkIn = dateTimeFormat.format(checkInDate);
        String checkOut = dateTimeFormat.format(checkOutDate);

        String child = "0";
        String adult = "1";

        hotelSearchResultList = hotelService.searchHotelForTour(checkIn, checkOut, country, cityName, cityCode, adult, child);

        //SearchParameters
        List<String> searchParameters = new ArrayList<>();

        if (hotelSearchResultList.size() > 0) {
            searchParameters.add(cityName + ", " + country);
            searchParameters.add(checkIn);
            searchParameters.add(checkOut);
            searchParameters.add(adult);
            searchParameters.add(child);
            searchParameters.add("0");
            searchParameters.add("0");
            searchParameters.add("0");
            searchParameters.add("0");
        }
        if (hotelSearchResultList.isEmpty()) {
            model.addAttribute("noHotelFound", true);
            return "redirect:";
        }
        model.addAttribute("searchParameters", searchParameters);
        model.addAttribute("currency", "#");
        model.addAttribute("hotelSearchResult", hotelSearchResultList);

        return "tourDestination";
    }
}