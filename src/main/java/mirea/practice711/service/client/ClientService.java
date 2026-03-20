package mirea.practice711.service.client;

import lombok.RequiredArgsConstructor;
import mirea.practice711.dao.entity.User;
import mirea.practice711.dao.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public ClientService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll(){
        List<User> users = userRepository.findAll();
        System.out.println(users);
        return users;
    }

    public User getById(UUID id){
        return userRepository.getUserById(id).orElseThrow();
    }

    public void delete(UUID id){
        userRepository.delete(getById(id));
    }
}
