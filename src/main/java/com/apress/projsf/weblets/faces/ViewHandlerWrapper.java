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
package com.apress.projsf.weblets.faces;

import javax.faces.application.ViewHandler;
import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import java.io.IOException;
import javax.faces.FacesException;

public class ViewHandlerWrapper extends ViewHandler
{
  public ViewHandlerWrapper(
    ViewHandler delegate)
  {
    _delegate = delegate;
  }

  public Locale calculateLocale(
    FacesContext context)
  {
    return _delegate.calculateLocale(context);
  }

  public String calculateRenderKitId(
    FacesContext context)
  {
    return _delegate.calculateRenderKitId(context);
  }

  public UIViewRoot createView(
    FacesContext context,
    String       viewId)
  {
    return _delegate.createView(context, viewId);
  }

  public String getActionURL(
    FacesContext context,
    String       path)
  {
    return _delegate.getActionURL(context, path);
  }

  public String getResourceURL(
    FacesContext context,
    String       path)
  {
    return _delegate.getResourceURL(context, path);
  }

  public void renderView(
    FacesContext context,
    UIViewRoot   view) throws IOException, FacesException
  {
    _delegate.renderView(context, view);
  }

  public UIViewRoot restoreView(
    FacesContext context,
    String       viewId)
  {
    return _delegate.restoreView(context, viewId);
  }

  public void writeState(
    FacesContext context) throws IOException
  {
    _delegate.writeState(context);
  }

  private final ViewHandler _delegate;
}

