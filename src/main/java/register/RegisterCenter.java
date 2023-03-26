package register;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author Lenovo
 * @create 2023-03-26-9:08
 */
public class RegisterCenter {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    public static final int PORT = 50000;
    private static final String rootPath="/MyRpc";
    static RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
    static CuratorFramework zkClient = CuratorFrameworkFactory.builder()
            // the server to connect to (can be a server list)
            .connectString("43.143.124.26:2181")
            .retryPolicy(retryPolicy)
            .build();
    static{
        zkClient.start();
        try {
            if(zkClient.checkExists().forPath(rootPath)==null)
                zkClient.create().forPath(rootPath);
            else
                System.out.println("根节点已存在");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void registerService(String interfaceName,String ip) throws Exception {
        String address=ip+":"+PORT;
        if(zkClient.checkExists().forPath(rootPath+"/"+interfaceName)==null)
            zkClient.create().forPath(rootPath+"/"+interfaceName,address.getBytes());
        else
            zkClient.setData().forPath(rootPath+"/"+interfaceName,address.getBytes());
    }
    public static String getService(String interfaceName) throws Exception {
        return new String(zkClient.getData().forPath(rootPath+"/"+interfaceName));
    }

    public static void main(String[] args) throws Exception {


    }

}
