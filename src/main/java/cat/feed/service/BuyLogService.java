package cat.feed.service;

import cat.feed.entity.BuyLogs;
import cat.feed.entity.User;
import cat.feed.repository.BuyLogsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BuyLogService {

    private final BuyLogsRepository buyLogsRepository;


    public void save(Map<String, String> map, List<String> itemNames, User user, BuyLogs buyLogs) {
        buyLogs = buyLogs.save(map,itemNames,user,buyLogs);
        buyLogsRepository.save(buyLogs);
    }
}
