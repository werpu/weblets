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

import java.text.ParseException;

import junit.framework.TestCase;

/**
 * HttpDateFormatTest tests the HttpDateFormat implementation of
 * HTTP Protocol RFC2616 Section 3.3.1 Full Date.
 */
public class HttpDateFormatTest extends TestCase
{
  /**
   * Creates a new HttpDateFormatTest.
   * 
   * @param testName  the test name
   */
  public HttpDateFormatTest(
    String testName)
  {
    super(testName);
  }
  
  /**
   * Tests parsing of HTTP Date format strings.
   */
  public void testParse()
  {
    try
    {
      new HttpDateFormat().parse("Tue, 24 Jan 2006 09:54:10 GMT");
    }
    catch (ParseException e)
    {
      fail();
    }
  }
}