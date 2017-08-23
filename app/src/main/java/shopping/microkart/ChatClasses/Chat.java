package shopping.microkart.ChatClasses;



import java.util.Map;


public class Chat {

    private String message;
    private String author;
    private Integer status;
    private Long timestamp;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Chat() {
    }

    public Chat(String message, String author) {
        this.message = message;
        this.author = author;
    }
    public Chat(String message, String author, Long timestamp) {
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public Chat(String message, String author, Map timestamp, Integer status) {
        this.message = message;
        this.author = author;
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Integer getStatus(){
        return status;
    }
}
