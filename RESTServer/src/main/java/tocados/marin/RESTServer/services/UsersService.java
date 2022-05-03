package tocados.marin.RESTServer.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tocados.marin.RESTServer.models.User;
import tocados.marin.RESTServer.repositories.UsersRepository;

@Service
public class UsersService {
    @Autowired
    UsersRepository usersRepository;

    public List<User> getUsers() {
        return (List<User>) usersRepository.findAll();
    }

    public User insertUser(User user) {
        return usersRepository.save(user);
    }
}
