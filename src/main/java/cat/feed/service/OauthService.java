package cat.feed.service;

import cat.feed.oauth.GoogleOauth;
import cat.feed.oauth.KakaoOauth;
import cat.feed.oauth.NaverOauth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final HttpServletResponse response;
    private final KakaoOauth kakaoOauth;
    private final GoogleOauth googleOauth;
    private final NaverOauth naverOauth;

    public void request(String socialLoginType,String url) {
        String redirectURL;
        if (socialLoginType.equals("google")) redirectURL = googleOauth.getOauthRedirectURL();
        else if(socialLoginType.equals("naver")) redirectURL = naverOauth.getOauthRedirectURL(url);
        else redirectURL = kakaoOauth.getOauthRedirectURL(url);
        try {
            response.sendRedirect(redirectURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String requestAccessToken(String socialLoginType, String code,String url) {

        if (socialLoginType.equals("google")) return googleOauth.requestAccessToken(code);
        else if(socialLoginType.equals("naver")) return naverOauth.requestAccessToken(code,url);
        else {
            return kakaoOauth.requestAccessToken(code,url);
        }
    }


    public void kakaoLogout() {

        try {
            response.sendRedirect("https://kauth.kakao.com/oauth/logout?client_id=b4dad0c3fd74414e64580f182c1e5df9&logout_redirect_uri=http://localhost:8080/logout/kakao");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        kakaoOauth.logout();
    }
}