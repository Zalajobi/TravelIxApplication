package travelix.webapp.Service;

import travelix.webapp.Model.User;

public interface UserService {

    public boolean userWithEmailExist(String email);

    public boolean userWithUserNameExist(String username);

    public void createUser(User user);

    public void createAdmin(User admin);
}
