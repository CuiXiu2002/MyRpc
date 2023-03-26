package test;

/**
 * @author Lenovo
 * @create 2023-03-26-10:05
 */
public class HelloImpl implements Hello{

    @Override
    public int add(int a, int b) {
        return a+b;
    }

    @Override
    public String hi() {
        return "hello,xhx";
    }
}
