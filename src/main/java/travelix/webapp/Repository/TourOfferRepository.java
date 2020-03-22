package travelix.webapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travelix.webapp.Model.TourOffer;

@Repository
public interface TourOfferRepository extends JpaRepository<TourOffer, String> {
    TourOffer findByCountry(String countryName);
}
