
package com.paynet.controller;

import com.paynet.model.UserPayApp;
import com.paynet.service.UserService;
import com.paynet.web.mapper.UserPayAppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private IConnectedUserInfo connectedUserInfo;

    @Autowired
    private UserController userController;

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private UserPayAppMapper userPayAppMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getUserInfo(Principal user, UserPayApp userPayApp, Model model) {
        if (  user instanceof OAuth2AuthenticationToken
                && userService.getUserByEmail(connectedUserInfo.getConnectedUserEmail(user).toString())==null){
            return userController.showInscriptionForm(user, userPayAppMapper.toUserPayAppDTO(userPayApp), model);
        }
        else {
            return transactionController.showAddTransactionForm(model, user);}
    }

    @GetMapping("/showLoginPage")
    public String showLoginPage() {
        return "login";
    }

}




/*

    @GetMapping("/user")
    public String getUser() {
        return "Welcome, UserPayApp";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Welcome, Admin";
    }
    @GetMapping("/*")
    public String getUserInfo(Principal user, @AuthenticationPrincipal OidcUser oidcUser) {
        StringBuffer userInfo = new StringBuffer();
        if (user instanceof UsernamePasswordAuthenticationToken) {
            userInfo.append(getUsernamePasswordLoginInfo(user));
        } else if (user instanceof OAuth2AuthenticationToken) {
            userInfo.append(getOauth2LoginInfo(user, oidcUser));
        }
        return userInfo.toString();
    }




 */


/*
    public static String connectedUserEmail;

    @GetMapping("/*")
    public String getUserInfo(Principal user, @AuthenticationPrincipal OidcUser oidcUser) {
        connectedUserEmail = getConnectedUserEmail(user, oidcUser);
        return connectedUserEmail;
    }

    public String getConnectedUserEmail(Principal user, @AuthenticationPrincipal OidcUser oidcUser) {
        StringBuffer userInfo = new StringBuffer();
        if (user instanceof UsernamePasswordAuthenticationToken) {
            userInfo.append(getUsernamePasswordLoginInfo(user));
        } else if (user instanceof OAuth2AuthenticationToken) {
            userInfo.append(getOauth2LoginInfo(user, oidcUser));
        }
        return userInfo.toString();
    }
 */

/*


 */


//quand connexion avec formulaire Spring Security :

    /*private StringBuffer getUsernamePasswordLoginInfo(Principal user)
    {
        StringBuffer usernameInfo = new StringBuffer();

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        if(token.isAuthenticated()){
            User u = (User) token.getPrincipal();
            usernameInfo.append(u.getUsername());
            //usernameInfo.append("Welcome, " + u.getUsername());
        }
        else{
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }

    private StringBuffer getOauth2LoginInfo(Principal user, OidcUser oidcUser) {
        StringBuffer protectedInfo = new StringBuffer();

        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        OAuth2AuthorizedClient authClient = this.authorizedClientService
                .loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
        if (authToken.isAuthenticated()) {

            Map<String, Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

            String userToken = authClient.getAccessToken().getTokenValue();
            protectedInfo.append(userAttributes.get("email"));
            /*
            protectedInfo.append("Welcome, " + userAttributes.get("name") + "<br><br>");
            protectedInfo.append("e-mail: " + userAttributes.get("email") + "<br><br>");
            //protectedInfo.append("Access Token: " + userToken + "<br><br>");

             */
/*
            if (oidcUser != null) {
                OidcIdToken idToken = oidcUser.getIdToken();
                if (idToken != null) {
                    protectedInfo.append("idToken value: " + idToken.getTokenValue() + "<br><br>");
                    protectedInfo.append("Token mapped values <br><br>");
                    Map<String, Object> claims = idToken.getClaims();
                    for (String key : claims.keySet()) {
                        protectedInfo.append("  " + key + ": " + claims.get(key) + "<br>");
                    }
                }
            }
            */
/*
        } else {
            protectedInfo.append("NA");
        }
        return protectedInfo;
    }
    */



    /*
    @GetMapping("/")
    public String getGitHub() {
        return "Welcome, GitHub user";
    }
     */




/*
    @GetMapping("/")
    public String getUserInfo(Principal user,  @AuthenticationPrincipal OidcUser oidcUser) {
        StringBuffer userInfo= new StringBuffer();
        if(user instanceof UsernamePasswordAuthenticationToken){
            userInfo.append(getUsernamePasswordLoginInfo(user));
        } else if(user instanceof OAuth2AuthenticationToken){
            userInfo.append(getOauth2LoginInfo(user, oidcUser));
        }
        return userInfo.toString();
    }

    private StringBuffer getUsernamePasswordLoginInfo(Principal user)
    {
        StringBuffer usernameInfo = new StringBuffer();

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) user);
        if(token.isAuthenticated()){
            User u = (User) token.getPrincipal();
            usernameInfo.append("Welcome, " + u.getUsername());
        }
        else{
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }

    private StringBuffer getOauth2LoginInfo(Principal user, OidcUser oidcUser){

        StringBuffer protectedInfo = new StringBuffer();

        OAuth2AuthenticationToken authToken = ((OAuth2AuthenticationToken) user);
        OAuth2AuthorizedClient authClient = this.authorizedClientService.loadAuthorizedClient(authToken.getAuthorizedClientRegistrationId(), authToken.getName());
        if(authToken.isAuthenticated()){

            Map<String,Object> userAttributes = ((DefaultOAuth2User) authToken.getPrincipal()).getAttributes();

            String userToken = authClient.getAccessToken().getTokenValue();
            protectedInfo.append("Welcome, " + userAttributes.get("name")+"<br><br>");
            protectedInfo.append("e-mail: " + userAttributes.get("email")+"<br><br>");
            //protectedInfo.append("Access Token: " + userToken+"<br><br>");

            if (oidcUser != null) {
                OidcIdToken idToken = oidcUser.getIdToken();
                if (idToken != null) {
                    protectedInfo.append("idToken value: " + idToken.getTokenValue() + "<br><br>");
                    protectedInfo.append("Token mapped values <br><br>");
                    Map<String, Object> claims = idToken.getClaims();
                    for (String key : claims.keySet()) {
                        protectedInfo.append("  " + key + ": " + claims.get(key) + "<br>");
                    }
                }
            }
        }
        else{
            protectedInfo.append("NA");
        }
        return protectedInfo;
    }
}
 */




