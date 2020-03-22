package travelix.webapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import travelix.webapp.Model.CountryList;

@Repository
public interface AvailableCountryRepository extends JpaRepository<CountryList, String> {

//    boolean findByCountryName(String countryName);
}