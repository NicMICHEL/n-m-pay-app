package com.paynet.controller;

import com.paynet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
public class ConnectedUserInfo implements IConnectedUserInfo {

    @Autowired
    private UserService userService;

    private final OAuth2AuthorizedClientService authorizedClientService;

    public ConnectedUserInfo(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public int getConnectedUserId(Principal user) {
        String connectedUserEmail = getConnectedUserEmail(user);
        return userService.getUserByEmail(connectedUserEmail).getIdUser();
    }

    @Override
    public String getConnectedUserEmail(Principal user) {
        if (user instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            return getUsernamePasswordLoginInfo(usernamePasswordAuthenticationToken);
        } else if (user instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            return getOauth2LoginInfo(oAuth2AuthenticationToken);
        } else {
            return "NA";
        }
    }

    @Override
    public Boolean isInstanceOfOAuth2AuthenticationToken(Principal user) {
        return (user instanceof OAuth2AuthenticationToken);
    }

    private String getUsernamePasswordLoginInfo(UsernamePasswordAuthenticationToken token) {
        if (token.isAuthenticated()) {
            User u = (User) token.getPrincipal();
            return u.getUsername();
        } else {
            return "NA";
        }
    }

    private String getOauth2LoginInfo(OAuth2AuthenticationToken authToken) {
        if (authToken.isAuthenticated()) {
            Map<String, Object> userAttributes = authToken.getPrincipal().getAttributes();
            return userAttributes.get("email").toString();
        } else {
            return "NA";
        }
    }

}
