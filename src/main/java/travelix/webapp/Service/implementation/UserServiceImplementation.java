package travelix.webapp.Service.implementation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import travelix.webapp.Model.User;
import travelix.webapp.Model.UserRole;
import travelix.webapp.Repository.UserRepository;
import travelix.webapp.Service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean userWithEmailExist(String email) {
        if (userRepository.findUserByEmail(email) == null)
            return false;
        else
            return true;
    }

    public boolean userWithUserNameExist(String username) {
        if (userRepository.findUserByUserName(username) == null)
            return false;
        else
            return true;
    }

    public void createUser(User user) {
        List<UserRole> userRoleList = new ArrayList<>();
        UserRole userRole = new UserRole("USER");
        userRoleList.add(userRole);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(userRoleList);

        userRepository.save(user);
    }

    public void createAdmin(User admin) {
        UserRole userRole = new UserRole("ADMIN");
        List<UserRole> userRoleList = new ArrayList<>();
        userRoleList.add(userRole);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setUserRole(userRoleList);
        userRepository.save(admin);
    }
}
