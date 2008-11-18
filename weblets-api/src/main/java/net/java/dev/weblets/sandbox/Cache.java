package net.java.dev.weblets.sandbox;

/**
 * @author werpu
 * @date: 18.11.2008
 */
public interface Cache {

    public Object get(String key);

    public void put(String key, Object data);

    public void flush(); /*emty the cache no matter what!*/

    public boolean full();
}
