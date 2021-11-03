package cat.feed.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Box {

    @Id
    @GeneratedValue
    private long id;

    private long userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "itemId")
//    private Item item;

}
