/**********************************************
 ******* Trabajo de Proyecto Software *********
 ******* Unicast ******************************
 ******* Fecha 22-5-2019 **********************
 ******* Autores: *****************************
 ******* Adrian Samatan Alastuey 738455 *******
 ******* Jose Maria Vallejo Puyal 720004 ******
 ******* Ruben Rodriguez Esteban 737215 *******
 **********************************************/

 package com.unicast.unicast_backend.principal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.unicast.unicast_backend.persistance.model.Privilege;
import com.unicast.unicast_backend.persistance.model.Role;
import com.unicast.unicast_backend.persistance.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/*
 * Permite extraer todos los datos de un usuario
 * a traves de su correspondiente token de sesion
 */

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	// Usuario
	private User user;

	// Constructor 
	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getGrantedAuthorities(getPrivileges(user.getRolesAndPrivileges()));
	}

	/*
	 * Coleccion de metodos getters para poder extraer toda la informacion 
	 * del usuario
	 */
	 
	/*
	 * Devolver el uusario
	 */
	public User getUser() {
		return user;
	}

	/*
	 * Devolver el identificador del usuario
	 */
	public Long getId() {
		return user.getId();
	}

	/*
	 * Devolver la contrasenya del usuario
	 */
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	/*
	 * Retorno del nombre real del usuario
	 */
	@Override
	public String getUsername() {
		return user.getUsername();
	}

	/*
	 * Retorno de si la cuenta del usuario ha expirado o no
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/*
	 * Devuelve si la cuenta esta o no bloqueada
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/*
	 * Devuelve si las credenciales de la cuenta han expirado
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * Retorno de si la cuenta del usuario esta o no deshabilitada
	 */
	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	/*
	 * Permite obtener una lista con todos los privilegios del usuario segun los posibles
	 * roles que puede tomar
	 * @param roles: la coleccion de roles posibles que puede desempeñar un usuario
	 */
	private List<String> getPrivileges(Collection<Role> roles) {

		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (Role role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	/*
	 * Devuelve una lista con los permisos de funcionalidad del usuario
	 * segun los privilegios que posee
	 * @param privileges: lista de privilegios del usuario
	 */
	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

}
