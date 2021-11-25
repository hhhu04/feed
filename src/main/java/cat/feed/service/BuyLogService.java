package cat.feed.service;

import cat.feed.entity.BuyLogs;
import cat.feed.entity.User;
import cat.feed.repository.BuyLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BuyLogService {

    private final BuyLogsRepository buyLogsRepository;

    public Page<BuyLogs> AllLog(Pageable pageable,long userId) {
        Page<BuyLogs> list = buyLogsRepository.findAllByUserIdOrderByIdDesc(userId,pageable);
        return list;
    }


    public void save(Map<String, String> map, List<String> itemNames, User user, BuyLogs buyLogs) {
        buyLogs = buyLogs.save(map,itemNames,user,buyLogs);
        buyLogsRepository.save(buyLogs);
    }
}
