package cat.feed.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NaverOauth {
    private final String NAVER_SNS_BASE_URL = "https://nid.naver.com/oauth2.0/authorize";
    private final String NAVER_SNS_CLIENT_ID = "T1Zt7FTzi7EtAJw5pAZY";

    private final String NAVER_SNS_TOKEN_BASE_URL = "https://nid.naver.com/oauth2.0/token";
    private final String NAVER_SNS_CLIENT_SECRET = "EKG2lRBlEQ";
    private String to;
    private final String state = "123".toString();


    public String getOauthRedirectURL(String host) {
        String KAKAO_SNS_CALLBACK_URL = "http://"+host+":8080/naver/callback";

        Map<String, Object> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", NAVER_SNS_CLIENT_ID);
        params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.put("state",state);
        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return NAVER_SNS_BASE_URL + "?" + parameterString;
    }

    public String requestAccessToken(String code,String host) {
        RestTemplate restTemplate = new RestTemplate();
        String KAKAO_SNS_CALLBACK_URL = "http://"+host+":8080/naver/callback";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NAVER_SNS_CLIENT_ID);
        params.add("client_secret", NAVER_SNS_CLIENT_SECRET);
        params.add("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.add("code", code);
        params.add("state",state);

        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(NAVER_SNS_TOKEN_BASE_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            ObjectMapper om = new ObjectMapper();

            Map<String, String> map=null;
            try {
                map = om.readValue(responseEntity.getBody(), Map.class);
            }
            catch (Exception e){
                e.printStackTrace();
            }



            return info(map);
        }
        return "네이버로그인 요청 처리 실패";
    }

    public String info(Map<String,String> map){
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();


        String token = "Bearer "+map.get("access_token");

        to = token;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",token);


        String url ="https://openapi.naver.com/v1/nid/me";

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                url, //{요청할 서버 주소}
                HttpMethod.GET, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class);


        ObjectMapper om = new ObjectMapper();
        Map<String, Map<String,String>> map2=null;
        Map<String, String> map3=null;
        String name = null;
        String name2 = null;
        try {
            map2 = om.readValue(response.getBody(), Map.class);
            name = map2.get("response").get("email");
            name2 = map2.get("response").get("id");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(name2);
        //      8HQlSq_WEcXALSCZXjmH1tVSgeZpt-zf4Ia_WfYfShI
//              8HQlSq_WEcXALSCZXjmH1tVSgeZpt-zf4Ia_WfYfShI
        return name2;
    }
}
