package cat.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private String tradeList;

    private String tradeNumber;

    private String status;  //정상구매, 거래실패, 정상취소

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public BuyLogs save(Map<String, String> map, List<String> itemNames, User user, BuyLogs buyLogs) {
        String list = "";

        for(String i:itemNames){
            list = list+"/"+i;
        }

        buyLogs.setUserId(user.getId());
        buyLogs.setTradeList(list);
        buyLogs.setTradeNumber(map.get("partner_order_id"));
        buyLogs.setStatus("정상구매");
        buyLogs.setCreatedAt(LocalDateTime.now());

        return buyLogs;
    }
}
