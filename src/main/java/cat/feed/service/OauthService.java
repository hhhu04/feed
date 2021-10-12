package cat.feed.service;

import cat.feed.oauth.GoogleOauth;
import cat.feed.oauth.KakaoOauth;
import cat.feed.oauth.NaverOauth;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    public String naverLogout(String token) {
        String url="https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=T1Zt7FTzi7EtAJw5pAZY&client_secret=EKG2lRBlEQ&access_token="+token+"&service_provider=NAVER";
        RestTemplate template = new RestTemplate();
        ResponseEntity<?> re = template.getForEntity(url, Map.class);
        Map<String,String> result = (Map<String, String>) re.getBody();
        if(result.get("result").equals("success")){
           return result.get("result");
        }
        else return "no";

    }
}