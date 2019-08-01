package ru.com.m74.cubes.security.service;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author mixam
 * @since 05.08.16 1:34
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {
    boolean hasRoles(String... roles) throws AccessDeniedException;
}
