package cat.feed.dto;

import cat.feed.entity.Feed;
import cat.feed.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class AllDto {
    private List<User> userList;
    private List<User> adminList;
    private List<Feed> feedList;

    private long userCount;
    private long adminCount;
    private long feedCount;

}
