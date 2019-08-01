package ru.com.m74.cubes.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.com.m74.extjs.dto.Response;

@RestController
public class SecurityController {

    @RequestMapping(value = "/remoteUser", method = RequestMethod.GET)
    public Object remoteUser() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return new Response<>(a != null ? a.getPrincipal() : null);
    }
}
