package cat.feed.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FeedDto {

    private String title;

    private String body;

    private String img;

}
