import java.io.Serializable;

/**
 * @author ${USER}
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class Message implements Serializable {
    static final long serialVersionUID = 426567411L;
    String mess;

    public Message(String mess) {
        this.mess = mess;
    }

    public String getMess() {
        return mess;
    }
}