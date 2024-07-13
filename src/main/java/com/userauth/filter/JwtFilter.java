package com.userauth.filter;



import com.userauth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class JwtFilter extends OncePerRequestFilter {


    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");

        if (authHeader != null || authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
        }


        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails;

        if (username != null && authentication == null) {
            Optional<UserDetails> user = userRepository.findByEmail(username);

            if (user.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }
            userDetails = user.get();

            if (jwtService.isTokenValid(jwt, userDetails)) {
                SecurityContext contextHolder = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                contextHolder.setAuthentication(token);
                SecurityContextHolder.setContext(contextHolder);
            }


        }

        filterChain.doFilter(request,response);
    }
}
