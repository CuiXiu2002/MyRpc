package test;
import proxy.UserProxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cuixiu2002
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class MethodUser {

    public static void main(String[] args) throws Exception {
        UserProxy userProxy = new UserProxy();
        Hello hello1= userProxy.getInstance(Hello.class);
//        Hello hello2= userProxy.getInstance(Hello.class);
//        Hello hello3= userProxy.getInstance(Hello.class);
//        ExecutorService service = Executors.newFixedThreadPool(3);
//        service.execute(()->{
//            System.out.println(hello2.hi());
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        service.execute(()->{
//            System.out.println(hello1.add(1,2));
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        service.execute(()->{
//            System.out.println(hello3.add(1,3));
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        service.shutdown();
        System.out.println(hello1.add(1, 2));

    }
}