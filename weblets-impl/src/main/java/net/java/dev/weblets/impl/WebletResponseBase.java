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
package net.java.dev.weblets.impl;

import net.java.dev.weblets.WebletResponse;
import net.java.dev.weblets.packaged.WebletResourceloadingUtils;

abstract public class WebletResponseBase implements WebletResponse {
	public WebletResponseBase(String contentTypeDefault) {
		_contentTypeDefault = contentTypeDefault;
	}

	public final void setDefaultContentType(String contentTypeDefault) {
		_contentTypeDefault = contentTypeDefault;
	}

	public final String getDefaultContentType() {
		return _contentTypeDefault;
	}

	public final void setContentType(String contentType) {
		if (contentType == null || "content/unknown".equals(contentType))
			contentType = _contentTypeDefault;
		setContentTypeImpl(contentType);
	}

	public final void setLastModified(long lastModified) {
		// Detect unknown-last-modified
		if (lastModified != 0)
			setLastModifiedImpl(lastModified);
	}

	public final void setContentVersion(String contentVersion, long timeout) {
		if (WebletResourceloadingUtils.isVersionedWeblet(contentVersion)) {
			setContentVersionImpl(contentVersion, timeout);
		}
	}

	abstract protected void setContentTypeImpl(String contentType);

	abstract protected void setLastModifiedImpl(long lastModified);

	abstract protected void setContentVersionImpl(String contentVersion, long timeout);

	private String	_contentTypeDefault;
}
