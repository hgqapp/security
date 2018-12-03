package com.hgq.security.config;

import com.hgq.security.model.Authorities;
import com.hgq.security.model.Users;
import com.hgq.security.repository.AuthorityRepository;
import com.hgq.security.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DbUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;

    public DbUserDetailsService(UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> usersOptional = userRepository.findByUsername(username);
        Users userModel = usersOptional.orElseThrow(() -> new UsernameNotFoundException(username));
        List<Authorities> authorities = authorityRepository.findAuthorityByUserId(userModel.getUserId());
        PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .passwordEncoder(encoder::encode)
                .authorities(authorities.stream().map(a -> new SimpleGrantedAuthority(a.getAuthority())).collect(Collectors.toList())).build();
    }
}
