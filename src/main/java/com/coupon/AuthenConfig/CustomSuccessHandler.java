package com.coupon.AuthenConfig;
import com.coupon.entity.UserEntity;
import com.coupon.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {
@Autowired
private JwtService jwtService;
@Autowired
private UserService userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Custom logic after successful OAuth2 login
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profilePhoto = oAuth2User.getAttribute("picture");
        UserEntity userEntity=userService.findByEmail(email);
        String token =jwtService.generateToken(userEntity);
        // Example: Send a custom JSON response
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.getWriter().write("{\"message\": \"OAuth2 Login Successful\", \"name\": \"" + name + "\", \"email\": \"" + email + "\",\"token\":\""+token+"\",\"photo\":\""+profilePhoto+"\"}");
        System.out.println("token for oauth2 :  "+token);
        String redirectUrl = "http://localhost:4200/oauthCallback?message=" + URLEncoder.encode("OAuth2 Login Successful", StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&photo=" + URLEncoder.encode(profilePhoto, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);
    }
}
