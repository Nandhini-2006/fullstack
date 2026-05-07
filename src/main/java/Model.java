import java.time.LocalDateTime;

public class Model {

    public static class Token {
        public Long id;
        public String userName;
        public String department;
        public LocalDateTime issueTime;
        public String status;
    }
}