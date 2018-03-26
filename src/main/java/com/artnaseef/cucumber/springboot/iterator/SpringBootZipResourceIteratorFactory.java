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

package com.artnaseef.cucumber.springboot.iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import cucumber.runtime.CucumberException;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceIteratorFactory;

/**
 * NOTE: most of this code was copied out of the Cucumber sources and modified as-needed.
 *
 * Created by art on 3/23/18.
 */
public class SpringBootZipResourceIteratorFactory implements ResourceIteratorFactory {

  private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(SpringBootZipResourceIteratorFactory.class);

  private Logger log = DEFAULT_LOGGER;

  @Override
  public boolean isFactoryFor(URL url) {
    return url.getFile().contains("!/");
  }

  @Override
  public Iterator<Resource> createIterator(URL url, String path, String suffix) {
    try {
      URL jarUrl = extractJarUrl(url);

      return new SpringBootZipUrlResourceIterator(jarUrl, path, suffix);
    } catch (IOException e) {
      throw new CucumberException(e);
    }
  }

//========================================
// Internals
//----------------------------------------

  /**
   * Extract the JAR url from within the given full URL.
   *
   * @param fullUrl - full URL to the resource, which includes a jar file, and possible additional nested jar files.
   * @return
   */
  private URL extractJarUrl(URL fullUrl) {
    String urlPath = fullUrl.getPath();
    String[] parts = urlPath.split("!");

    int cur = parts.length - 1;
    while ((cur >= 0) && (! parts[cur].endsWith(".jar"))) {
      cur--;
    }

    if (cur >= 0) {
      String urlString = this.joinStringList("!", Arrays.copyOf(parts, cur + 1));

      try {
        return new URL(urlString);
      } catch (MalformedURLException muExc) {
        this.log.warn("problem extracting base jar URL: source-url={}; extracted={}", fullUrl, urlString, muExc);

        throw new RuntimeException("failed to exract base jar URL", muExc);
      }
    } else {
      this.log.warn("problem extracting base jar URL: source-url={}; url-path={}", fullUrl, urlPath);
      throw new RuntimeException("JAR url appears to be missing jar: url={}" + fullUrl);
    }
  }

  private String joinStringList(String separator, String... parts) {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    for (String onePart : parts) {
      if (first) {
        first = false;
      } else {
        result.append(separator);
      }

      result.append(onePart);
    }

    return result.toString();
  }
}
