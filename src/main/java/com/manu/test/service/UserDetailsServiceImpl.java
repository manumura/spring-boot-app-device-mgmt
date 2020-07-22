package com.manu.test.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.manu.test.entity.User;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    @Override
    public UserDetails loadUserByUsername(final String userId) {
		
        logger.debug(">>>>> start : {} <<<<<", userId);
		
        if (userId == null || userId.isEmpty()) {
            logger.error(">>>>> userId is empty <<<<<");
            throw new UsernameNotFoundException(">>>>> userId is empty <<<<<");
		}

		
		try {
            // TODO is digit
//            User user = null;
//            if (StringUtils.isNumeric(userId)) {
//                user = userService.findById(Long.valueOf(userId));
//            }
            User user = userService.findByLogin(userId);

			logger.debug("User : {}", user);
			
			if (user == null){
                logger.error("User not found for userId : {}", userId);
                throw new UsernameNotFoundException(">>>>> Username not found <<<<<");
			}
			
            // boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;

            final org.springframework.security.core.userdetails.User userDetails =
                    new org.springframework.security.core.userdetails.User(user.getLogin(),
                            user.getPassword(), user.getEmailConfirmed(), accountNonExpired,
                            credentialsNonExpired,
                            accountNonLocked,
                            getGrantedAuthorities(user));
            logger.debug(">>>>> end : {} <<<<<", userDetails);
			return userDetails;
			
        } catch (Exception e) {
			logger.error(">>>>> Exception : {} <<<<<", e.getMessage(), e);
			throw new UsernameNotFoundException(">>>>> An exception occured <<<<<", e);
        }
	}
	
	/**
	 * getGrantedAuthorities
	 * 
	 * @param user the user
	 * @return list of granted authorities
	 */
    private List<GrantedAuthority> getGrantedAuthorities(final User user) {

        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        // TODO
        // for (UserRole userRole : user.getUserRoles()) {
        // logger.debug("UserRole : {}", userRole);
        // authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getType()));
        // }

        logger.debug("Authorities : {}", authorities);
        return authorities;
    }
}
