package cat.feed.dto;

import cat.feed.entity.Item;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class BuyDto {

    private String items;

    private int totalPrice;



    public List<String> buy(String userId, BuyDto dto, String path) {
        String admin = "23b3eeb10257d6952a5b9531776274de";
        path ="http://"+  path + ":8000";

        String tradeNumber =Integer.toString(LocalDateTime.now().getYear())+Integer.toString(LocalDateTime.now().getMonthValue())+
                Integer.toString(LocalDateTime.now().getHour())+Integer.toString(LocalDateTime.now().getMinute())+Integer.toString(LocalDateTime.now().getSecond());

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String local =path + "/ok";

        params.add("cid","TC0ONETIME");
        params.add("partner_order_id", tradeNumber);
        params.add("partner_user_id",userId);
        params.add("item_name","basket");
        params.add("quantity","1");
        params.add("total_amount",Integer.toString(dto.getTotalPrice()));
        params.add("tax_free_amount","0");
        params.add("approval_url",local);
        params.add("cancel_url",path+"/ok");
        params.add("fail_url",path+"/ok");


        String Authorization ="KakaoAK "+admin;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",Authorization);


        String url ="https://kapi.kakao.com/v1/payment/ready";

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                url, //{????????? ?????? ??????}
                HttpMethod.POST, //{????????? ??????}
                entity, // {????????? ??? ?????? ?????????}
                String.class);

        ObjectMapper om = new ObjectMapper();
        Map<String, String> map3=null;
        List<String> list = new ArrayList<>();

        try {
            map3 = om.readValue(response.getBody(), Map.class);
            list.add(map3.get("next_redirect_pc_url"));
            list.add(tradeNumber);
            list.add(map3.get("tid"));
            return list;

        }
        catch (Exception e){
            e.printStackTrace();
            list.add(e.getMessage());
            return list;
        }
    }


    public Map<String, String> buySuccess(String userId, String tradeNumber, BuyDto dto, String tid, String pg_token) {
        String admin = "23b3eeb10257d6952a5b9531776274de";

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("cid","TC0ONETIME");
        params.add("tid",tid);
        params.add("partner_order_id", tradeNumber);
        params.add("partner_user_id",userId);
        params.add("pg_token",pg_token);

        String Authorization ="KakaoAK "+admin;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization",Authorization);


        String url ="https://kapi.kakao.com/v1/payment/approve";

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                url, //{????????? ?????? ??????}
                HttpMethod.POST, //{????????? ??????}
                entity, // {????????? ??? ?????? ?????????}
                String.class);

        ObjectMapper om = new ObjectMapper();
        Map<String, String> map3=null;
        List<String> list = new ArrayList<>();

        try {
            map3 = om.readValue(response.getBody(), Map.class);
            return map3;

        }
        catch (Exception e){
            e.printStackTrace();
            map3.put("error",e.getMessage());
            return map3;
        }


    }

    public List<String> idToName(List<Item> items, List<String> itemNames) {
        for(Item i:items){
            itemNames.add(i.getName());
        }

        return itemNames;
    }


    public List<Item> quantity(List<Item> list) {
        for(Item i : list){
            int num = i.getTotal();
            if(num >0) num--;
            i.setTotal(num);
        }

        return list;
    }

    public List<Long> setLong(BuyDto dto, List<Long> items) {
        String[] sp = dto.getItems().split("/");

        for(int i = 1; i < sp.length; i++){
            items.add(Long.valueOf(sp[i]));
        }

        return items;
    }
}
