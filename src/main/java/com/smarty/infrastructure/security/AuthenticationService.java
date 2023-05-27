package com.smarty.infrastructure.security;

import com.smarty.domain.account.model.LoginRequestDTO;
import com.smarty.domain.account.model.LoginResponseDTO;
import com.smarty.domain.account.service.AccountService;
import com.smarty.infrastructure.handler.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(AccountService accountService, JwtUtil jwtUtil) {
        this.accountService = accountService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalAccount = accountService.getAccountByEmail(username);

        if (optionalAccount.isEmpty()) {
            throw new NotFoundException("Email doesn't exist");
        }

        var account = optionalAccount.get();

        return new AuthenticatedUser(account);
    }

    public LoginResponseDTO authenticate(LoginRequestDTO loginDTO) {
        var authenticatedUser = (AuthenticatedUser) loadUserByUsername(loginDTO.email());

        if (!passwordEncoder.matches(loginDTO.password(), authenticatedUser.getPassword())) {
            throw new NotFoundException("Password isn't valid");
        }

        var accessToken = jwtUtil.createToken(authenticatedUser);

        return new LoginResponseDTO(accessToken);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

}
