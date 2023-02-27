package br.com.matheusslr.restwithspringandjava.service;

import br.com.matheusslr.restwithspringandjava.domain.User;
import br.com.matheusslr.restwithspringandjava.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("[INFO] Find one user by name " + username);
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username " + username + "not found!");
        }
    }
}
