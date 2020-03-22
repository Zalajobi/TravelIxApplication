package travelix.webapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import travelix.webapp.Model.DestinationCity;
import travelix.webapp.Model.HotelSearchResult;
import travelix.webapp.Model.TourOffer;
import travelix.webapp.Service.HotelService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


//All responses and function of the landing page is handled by this class
@Controller
@RequestMapping("travelix.ng")
public class IndexController {

    @Autowired
    private HotelService hotelService;
    @Autowired
    private HttpSession session;

    //Get the top holiday Destination
    @RequestMapping("/")
    public String index(Model model) throws Exception {
        hotelService.fetchTopDestination();
        List<DestinationCity> topDestination = (List<DestinationCity>) session.getAttribute("topDestination");
        List<DestinationCity> displayDestination = new ArrayList<>();

        for (int count = 0; count < 25; count++) {
            displayDestination.add(topDestination.get(count));
        }

        List<DestinationCity> bestTourOffer = hotelService.getTopTour();

        model.addAttribute("tourOffers", bestTourOffer);
        model.addAttribute("topDestinations", displayDestination);

        return "index";
    }
}
