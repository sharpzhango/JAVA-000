import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class HelloClassLoader extends ClassLoader{

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        Class<?> c = new HelloClassLoader().findClass();
        Object o = c.getConstructor().newInstance();
        Method method = c.getMethod("hello");
        method.invoke(o);

    }

    private Class<?> findClass() throws IOException {
        byte[] b = getBytesFromFile("Hello.xlass");
        return defineClass("Hello",b,0,b.length);
    }

    private byte[] getBytesFromFile(String s) throws IOException {
        InputStream inputStream = getResourceAsStream(s);
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);

        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (255 - data[i]);
        }
        return data;
    }

}
