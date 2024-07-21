package com.paynet.controller;

import java.security.Principal;

public interface IConnectedUserInfo {
    String getConnectedUserEmail(Principal user);

    int getConnectedUserId(Principal user);

    Boolean isInstanceOfOAuth2AuthenticationToken(Principal user);
}
