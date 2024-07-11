package com.paynet.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.security.Principal;

public interface IConnectedUserInfo {
    String getConnectedUserEmail(Principal user);
    int getConnectedUserId(Principal user);

    Boolean isInstanceOfOAuth2AuthenticationToken(Principal user);

}
