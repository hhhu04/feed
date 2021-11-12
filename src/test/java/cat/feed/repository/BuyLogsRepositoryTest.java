package cat.feed.repository;

import cat.feed.entity.BuyLogs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BuyLogsRepositoryTest {

    @Autowired
    BuyLogsRepository buyLogsRepository;

    @Test
    @Transactional
    void save(){
        BuyLogs buy = new BuyLogs();
        buy.setUserId(32);
        buy.setTradeList("4/5");
        buy.setTradeNumber(12345);
        buy.setCreatedAt(LocalDateTime.now());
        buy.setStatus("구매성공");
        buyLogsRepository.save(buy);

    }

}