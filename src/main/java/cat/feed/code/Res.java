package cat.feed.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Res<T> {

    private int statusCode;

    private String responseMessage;

    private T data;

    public Res(int statusCode,String responseMessage){
        this.statusCode = statusCode;
        this.responseMessage = responseMessage;
        this.data = null;
    }

    public static<T> Res<T> res(final int statusCode, final String responseMessage) {
        return res(statusCode, responseMessage, null);
    }

    public static<T> Res<T> res(final int statusCode, final String responseMessage, final T t) {
        return Res.<T>builder()
                .data(t)
                .statusCode(statusCode)
                .responseMessage(responseMessage)
                .build();
    }


}
