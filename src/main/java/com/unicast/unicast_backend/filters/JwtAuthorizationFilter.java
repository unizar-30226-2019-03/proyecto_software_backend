/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 package com.unicast.unicast_backend.filters;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unicast.unicast_backend.configuration.SecurityConstants;
import com.unicast.unicast_backend.exceptions.InvalidTokenException;
import com.unicast.unicast_backend.services.UserDetailsServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

/*
 * Control de filtro de autorizaciones para los usuarios
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final SecurityConstants securityConstants;
    private final UserDetailsServiceImpl userDetailsService;

    /*
     * Constructor de la clase
     */
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants,
            UserDetailsServiceImpl userDetailsService) {
        super(authenticationManager);

        this.securityConstants = securityConstants;
        this.userDetailsService = userDetailsService;
    }

    /*
     * Permite la creacion de filtros
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        String header = request.getHeader(securityConstants.TOKEN_HEADER);

        if (StringUtils.isEmpty(header) || !header.startsWith(securityConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    /*
     * Permite determinar si el token de sesion de un usuario es o no correcto
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(securityConstants.TOKEN_HEADER);
        if (StringUtils.isNotEmpty(token)) {
            try {
                byte[] signingKey = securityConstants.JWT_SECRET.getBytes();

                Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingKey)
                        .parseClaimsJws(token.replace("Bearer ", ""));

                String tokenBody = parsedToken.getBody().getSubject();

                List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody().get("rol")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority)).collect(Collectors.toList());

                if (StringUtils.isNotEmpty(tokenBody)) {
                    String[] idAndDoublyHashedPassword = tokenBody.split(";");
                    Long userId = Long.parseLong(idAndDoublyHashedPassword[0]);
                    UserDetails user = this.userDetailsService.loadUserById(userId);
                    if (user.isEnabled() && idAndDoublyHashedPassword[1].equals(JwtAuthenticationFilter.getSHA(user.getPassword()))) {
                        return new UsernamePasswordAuthenticationToken(user, null, authorities);
                    }

                    throw new InvalidTokenException("El token no es valido");
                }
            } catch (ExpiredJwtException exception) {
                log.warn("Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
            } catch (UnsupportedJwtException exception) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
            } catch (MalformedJwtException exception) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
            } catch (SignatureException exception) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, exception.getMessage());
            } catch (IllegalArgumentException exception) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
            } catch (UsernameNotFoundException exception) {
                log.warn("Id of user in token was not found on the database: {}", exception.getMessage());
            }
        }

        return null;
    }
}