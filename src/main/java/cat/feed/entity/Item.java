package cat.feed.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    private String nickName;

    private String name;

    private int total;

    private String img;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private int price;



    public Item newItem(Item item, MultipartFile img, String path) throws IOException {
        item.setImg(imgSet(img,path));
        item.setCreatedAt(LocalDateTime.now());
        return item;
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


}
