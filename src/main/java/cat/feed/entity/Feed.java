package cat.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @Lob
    private String body;

    private String img;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String nickName;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private List<Comment> comments = new ArrayList<>();

    public Feed newFeed(Feed feed, long  id){
        feed.setCreatedAt(LocalDateTime.now());
        feed.setUserId(id);
        return feed;
    }

    public Feed upLoad(MultipartFile img,Feed feed, Long id,String path) throws Exception {
        feed.setUserId(id);
        if(img != null) feed.setImg(imgSet(img,path));

        return feed;
    }

    private String imgSet(MultipartFile file,String path) throws IOException {
        Date dt = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyMMddHHmmss");

        String imgName = date.format(dt)+".jpg";

        file.getOriginalFilename();
        if(!file.getOriginalFilename().isEmpty()) {
            file.transferTo(new File(path, imgName));
            img = "img/"+imgName;
            System.out.println("99");
        }else {
            img = "-1";
            System.out.println("4");
        }
        System.out.println("4");


        return img;
    }

    public BufferedImage img(String imgName) throws IOException {
        BufferedImage img;
        File imageFile = new File("/home/cat/web/test/"+imgName);
        img = ImageIO.read(imageFile);
        return img;
    }


}
