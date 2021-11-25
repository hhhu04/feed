package cat.feed.repository;

import cat.feed.entity.BuyLogs;
import cat.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyLogsRepository extends JpaRepository<BuyLogs, Long> {


    Page<BuyLogs> findAllByUserIdOrderByIdDesc(long userId,Pageable pageable);

}