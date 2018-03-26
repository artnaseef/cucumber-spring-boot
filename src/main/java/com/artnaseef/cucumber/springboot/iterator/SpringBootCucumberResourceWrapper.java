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
import java.io.InputStream;

import cucumber.runtime.io.Resource;

/**
 * Wrap another resource and apply spring-boot logic to properly extract the class name from the path.
 *
 * Created by art on 3/23/18.
 */
public class SpringBootCucumberResourceWrapper implements Resource {

  private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(SpringBootCucumberResourceWrapper.class);

  private Logger log = DEFAULT_LOGGER;

  private final Resource nested;

  public SpringBootCucumberResourceWrapper(Resource nested) {
    this.nested = nested;
  }

  @Override
  public String getPath() {
    return nested.getPath();
  }

  @Override
  public String getAbsolutePath() {
    return nested.getAbsolutePath();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return nested.getInputStream();
  }

  @Override
  public String getClassName(String extension) {
    String nestedClassName = this.nested.getClassName(extension);

    String result = nestedClassName;
    if (result.startsWith("BOOT-INFO.classes.")) {
      result = result.substring(17);
    }

    this.log.debug("extension={}; nestedClassName={}; className={}", extension, nestedClassName, result);

    return result;
  }
}
