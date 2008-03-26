package net.java.dev.weblets.util;

import net.java.dev.weblets.WebletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: werpu
 * Date: 30.12.2007
 * Time: 13:58:20
 * To change this template use File | Settings | File Templates.
 */
public class ServiceLoader {

	

    public static Class loadService(Class serviceRef) {
        BufferedReader reader = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String resource = "META-INF/services/" + serviceRef.getName();
            InputStream in = loader.getResourceAsStream(resource);
            if(in == null) { //we are in a different loader, some ear containers are at the ear level at this stage if it is a webapp context
            	loader = serviceRef.getClassLoader();
            	in = loader.getResourceAsStream(resource);
            	if(in == null) {
            		Log log = LogFactory.getLog(ServiceLoader.class);
            		log.error(serviceRef + " Implementation class could not be found" );
            		//lets bomb out afterwards!!!
            	}
            		
            }
            reader = new BufferedReader(new InputStreamReader(in));
            String serviceClassName = reader.readLine();
            return loader.loadClass(serviceClassName);

        }
        catch (IOException e) {
            throw new WebletException("Error reading Weblet Service " +
                    "service information " + serviceRef, e);
        }
        catch (ClassNotFoundException e) {
            throw new WebletException("Unable to load Weblet Service " +
                    "implementation class "+serviceRef, e);
        } finally {

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new WebletException("Unable to load Weblet Service "+serviceRef, e);
                }
        }
    }
	
    public static Class loadService(String serviceRef) {
        BufferedReader reader = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            String resource = "META-INF/services/" + serviceRef;
            InputStream in = loader.getResourceAsStream(resource);
            if(in == null) { //we are in a different loader, some ear containers are at the ear level at this stage if it is a webapp context
            	loader = ServiceLoader.class.getClassLoader();
            	in = loader.getResourceAsStream(resource);
            	if(in == null) {
            		Log log = LogFactory.getLog(ServiceLoader.class);
            		log.error(serviceRef + " Implementation class could not be found" );
            		//lets bomb out afterwards!!!
            	}
            		
            }
            reader = new BufferedReader(new InputStreamReader(in));
            String serviceClassName = reader.readLine();
            return loader.loadClass(serviceClassName);

        }
        catch (IOException e) {
            throw new WebletException("Error reading Weblet Service " +
                    "service information " + serviceRef, e);
        }
        catch (ClassNotFoundException e) {
            throw new WebletException("Unable to load Weblet Service " +
                    "implementation class "+serviceRef, e);
        } finally {

            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new WebletException("Unable to load Weblet Service "+serviceRef, e);
                }
        }
    }
}
