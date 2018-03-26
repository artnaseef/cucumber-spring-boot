/*
 * Copyright (c) 2018 Arthur Naseef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.artnaseef.cucumber.springboot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cucumber.runtime.io.Resource;

/**
 * Created by art on 3/23/18.
 */
public class SpringBootCucumberResource implements Resource {

  private final URL url;
  private final String className;

  public SpringBootCucumberResource(URL url, String className) {
    this.url = url;
    this.className = className;
  }

//========================================
// Resource Interface
//----------------------------------------

  @Override
  public String getPath() {
    return this.url.getPath();
  }

  @Override
  public String getAbsolutePath() {
    return this.url.toString();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return this.url.openStream();
  }

  @Override
  public String getClassName(String extension) {
    return this.className;
  }

//========================================
// Miscellaneous
//----------------------------------------

  @Override
  public String toString() {
    return "SpringBootCucumberResource{" +
           "url=" + url +
           ", className='" + className + '\'' +
           '}';
  }
}
