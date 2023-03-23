/**
 * @author cuixiu2002
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class User {
    public static void main(String[] args) {
        UserProxy userProxy = new UserProxy();
        Hello hello= (Hello) userProxy.getInstance(Hello.class);
        System.out.println(hello.add(1, 2));
    }
}