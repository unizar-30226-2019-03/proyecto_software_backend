package com.unicast.unicast_backend.filters;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unicast.unicast_backend.configuration.SecurityConstants;
import com.unicast.unicast_backend.principal.UserDetailsImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityConstants securityConstants;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) {
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;

        setFilterProcessesUrl(securityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authentication) throws IOException {
        UserDetailsImpl user = ((UserDetailsImpl) authentication.getPrincipal());

        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = securityConstants.JWT_SECRET.getBytes();

        String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", securityConstants.TOKEN_TYPE).setIssuer(securityConstants.TOKEN_ISSUER)
                .setAudience(securityConstants.TOKEN_AUDIENCE).setSubject(user.getId().toString())
                .setExpiration(new Date(System.currentTimeMillis() + securityConstants.TOKEN_TTL_MS))
                .claim("rol", roles).compact();

        response.addHeader(securityConstants.TOKEN_HEADER, securityConstants.TOKEN_PREFIX + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"id\":\"" + user.getUser().getId() + "\",\"token\":\"" + token + "\"}");
        response.getWriter().flush();
    }
}