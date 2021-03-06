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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

/*
 * Permite el control de autentificacion de los flitros de los usuarios
 */

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityConstants securityConstants;
    static private MessageDigest mdSha256;

    static {
        try {
            mdSha256 = MessageDigest.getInstance("SHA-256");
            // nunca deberia llegar a catch
        } catch (NoSuchAlgorithmException e) {}
    }

    /*
     * Constructor de la clase
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, SecurityConstants securityConstants) {
        this.authenticationManager = authenticationManager;
        this.securityConstants = securityConstants;

        setFilterProcessesUrl(securityConstants.AUTH_LOGIN_URL);
    }

    /*
     * Permite la obtencion del Hash del usuario
     * Parametros
     * @param string: contrasenya a cifrar y obtener el valor de hash
     */
    public static String getSHA(String string) {
        byte[] hashedString = mdSha256.digest(string.getBytes());

        return (new BigInteger(1, hashedString)).toString(16);
    }

    /*
     * Permite llevar a cabo la autentificacion de un usuario
     * Parametros
     * @param request: peticion del cliente al servidor
     * @param response: respuesta del servidor al cliente
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
                password);

        return authenticationManager.authenticate(authenticationToken);
    }


    /*
     * Permite determinar si la peticion de registro del cliente se ha llevado a cabo correctamente
     * Parametros
     * @param request: peticion del cliente al servidor
     * @param response: respuesta del servidor al cliente
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain, Authentication authentication) throws IOException {
        UserDetailsImpl user = ((UserDetailsImpl) authentication.getPrincipal());

        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        byte[] signingKey = securityConstants.JWT_SECRET.getBytes();

        String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", securityConstants.TOKEN_TYPE).setIssuer(securityConstants.TOKEN_ISSUER)
                .setAudience(securityConstants.TOKEN_AUDIENCE).setSubject(user.getId().toString() + ";" + getSHA(user.getPassword()))
                .setExpiration(new Date(System.currentTimeMillis() + securityConstants.TOKEN_TTL_MS))
                .claim("rol", roles).compact();

        response.addHeader(securityConstants.TOKEN_HEADER, securityConstants.TOKEN_PREFIX + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"id\":\"" + user.getUser().getId() + "\",\"token\":\"" + token + "\"}");
        response.getWriter().flush();
    }
}