package cat.feed.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class BuyDto {

    private List<Long> items;

    private int totalPrice;



    public void buy(String userId, BuyDto dto,String path) {
        String admin = "23b3eeb10257d6952a5b9531776274de";

        int tradeNumber = LocalDateTime.now().getYear()+LocalDateTime.now().getMonthValue()+
                LocalDateTime.now().getHour()+LocalDateTime.now().getMinute()+LocalDateTime.now().getSecond();

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("cid","TC0ONETIME");
        params.add("partner_order_id", Integer.toString(tradeNumber));
        params.add("partner_user_id",userId);
        params.add("item_name","basket");
        params.add("quantity","1");
        params.add("total_amount",Integer.toString(dto.getTotalPrice()));
        params.add("tax_free_amount","0");
        params.add("approval_url",path+"/store/ok");
        params.add("cancel_url",path+"/store/cancel");
        params.add("fail_url",path+"/store/fail");


        String Authorization ="KakaoAK "+admin;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",Authorization);


        String url ="https://kapi.kakao.com/v1/payment/ready";

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                url, //{요청할 서버 주소}
                HttpMethod.POST, //{요청할 방식}
                entity, // {요청할 때 보낼 데이터}
                String.class);

        ObjectMapper om = new ObjectMapper();
        Map<String, Map<String,String>> map2=null;
        Map<String, String> map3=null;


        try {
            map2 = om.readValue(response.getBody(), Map.class);
            System.out.println(map2);
        }
        catch (Exception e){
            e.printStackTrace();
        }




    }
}
