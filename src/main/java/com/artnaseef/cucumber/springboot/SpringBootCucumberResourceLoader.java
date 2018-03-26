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

import com.artnaseef.cucumber.springboot.iterator.SpringBootClasspathResourceIterable;
import com.artnaseef.cucumber.springboot.iterator.SpringBootCucumberIterableWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceLoader;

/**
 * Created by art on 3/23/18.
 */
public class SpringBootCucumberResourceLoader implements ResourceLoader {

  private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(SpringBootCucumberResourceLoader.class);

  private Logger log = DEFAULT_LOGGER;

  private final ClassLoader classLoader;

//========================================
// Constructor
//----------------------------------------

  public SpringBootCucumberResourceLoader(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }


//========================================
// Getters and Setters
//----------------------------------------

  public Logger getLog() {
    return log;
  }

  public void setLog(Logger log) {
    this.log = log;
  }


//========================================
// Processing
//----------------------------------------

  @Override
  public Iterable<Resource> resources(String path, String suffix) {
    if (path.startsWith("classpath:")) {
      path = path.substring(10);
    }

    Iterable<Resource> plainResourceIterable = new SpringBootClasspathResourceIterable(this.classLoader, path, suffix);

    if (this.log.isTraceEnabled()) {
      for (Resource oneReource : plainResourceIterable) {
        this.log.trace("found resource: path={}; suffix={}; resource={}", path, suffix, oneReource);
      }
    }

    return new SpringBootCucumberIterableWrapper(plainResourceIterable);
  }
}
