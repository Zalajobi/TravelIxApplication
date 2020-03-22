package travelix.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import travelix.webapp.Model.User;
import travelix.webapp.Service.HotelService;
import travelix.webapp.Service.UserService;

@SpringBootApplication
public class TravelIxApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;
    @Autowired
    private HotelService hotelService;

    public static void main(String[] args) {
        SpringApplication.run(TravelIxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User admin = new User();
        admin.setEmail("admin@admin.com");
        admin.setPassword("admin");
        admin.setUsername("admin");
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setNationality("Nigeria");
        admin.setRegion("Lagos");
        userService.createAdmin(admin);

        hotelService.fetchAvailableCountryFromTBOHolidays();
    }

}
