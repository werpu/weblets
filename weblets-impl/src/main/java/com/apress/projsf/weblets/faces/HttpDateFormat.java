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

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * HttpDateFormat implements HTTP Protocol RFC2616 Section 3.3.1 Full Date
 *
 * Sun, 06 Nov 1994 08:49:37 GMT  ; RFC 822, updated by RFC 1123
 *
 * rfc1123-date = wkday "," SP date1 SP time SP "GMT"
 *
 * wkday        = "Mon" | "Tue" | "Wed"
 *              | "Thu" | "Fri" | "Sat" | "Sun"
 * date1        = 2DIGIT SP month SP 4DIGIT
 *                ; day month year (e.g., 02 Jun 1982)
 * time         = 2DIGIT ":" 2DIGIT ":" 2DIGIT
 *                ; 00:00:00 - 23:59:59
 */
public class HttpDateFormat extends SimpleDateFormat
{
  /**
   * Creates a new HttpDateFormat.
   */
  public HttpDateFormat()
  {
    super("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
    
    // match the pattern exactly, or fail to parse
    setLenient(false);
  }
}