package com.smarty.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer";
    private final Set<String> excludedUrls = Set.of("/authenticate", "/v3/api-docs", "swagger-ui");

    private final JwtUtil jwtUtil;
    private final UserDetailsService authenticationService;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService authenticationService) {
        this.jwtUtil = jwtUtil;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!validAuthHeader(authHeader)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        var accessToken = authHeader.substring(BEARER.length()).trim();
        var username = jwtUtil.extractUsername(accessToken);

        try {
            var authenticatedUser = (AuthenticatedUser) authenticationService.loadUserByUsername(username);
            var authenticationToken = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static boolean validAuthHeader(String authHeader) {
        return authHeader != null
                && !authHeader.isBlank()
                && authHeader.contains(BEARER);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return excludedUrls.stream().anyMatch(url -> request.getRequestURI().contains(url));
    }

}
