/*
 * Copyright 2005 John R. Fallows
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.java.dev.weblets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;

/**
 * The WebletContextListener is responsible for initializing the
 * WebletContainer.
 *
 * The <code>META-INF/services/net.java.dev.weblets.WebletsContextListener</code>
 * Service Provider configuration file is used to lookup the implementation
 * class for this ServletContextListener, as defined by the JAR file specification.
 */
public final class WebletsContextListener implements ServletContextListener 
{
    /**
     * Callback when the ServletContext is initialized.
     * 
     * @param event  the servlet context event
     */
    public void contextInitialized(ServletContextEvent event)
    {
        try
        {
          _delegate = (ServletContextListener)_WEBLETS_CONTEXT_LISTENER_CLASS.newInstance();
        }
        catch (IllegalAccessException e)
        {
          throw new RuntimeException("Unable to access " +
                                     "WebletsContextListener implementation", e);
        }
        catch (InstantiationException e)
        {
          throw new RuntimeException("Unable to instantiate " +
                                     "WebletsContextListener implementation", e);
        }

        _delegate.contextInitialized(event);
    }

    /**
     * Callback when the ServletContext is destroyed.
     * 
     * @param event  the servlet context event
     */
    public void contextDestroyed(ServletContextEvent event) 
    {
        _delegate.contextDestroyed(event);
    }

  private ServletContextListener _delegate;

  // the WebletsContextListener Service Provider implementation class
  static private final Class _WEBLETS_CONTEXT_LISTENER_CLASS;

  static
  {
    try
    {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      String resource = "META-INF/services/" + WebletsContextListener.class.getName();
      InputStream in = loader.getResourceAsStream(resource);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String serviceClassName = reader.readLine();
      _WEBLETS_CONTEXT_LISTENER_CLASS = loader.loadClass(serviceClassName);
      reader.close();
    }
    catch (IOException e)
    {
      throw new WebletException("Error reading WebletsContextListener " +
                                "service information", e);
    }
    catch (ClassNotFoundException e)
    {
      throw new WebletException("Unable to load WebletsContextListener " +
                                "implementation class", e);
    }
  }
}
