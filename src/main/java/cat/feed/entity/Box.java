package cat.feed.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Box {

    /*
    유저 가입시 하나의 파일을 생성(장바구니) 그 주소를 저장
    파일 구성은 item 의  id,수량 \n
    유저가 각자의 box id를 소유
     */



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long userId;

    private String test;



}
