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

import cucumber.runtime.io.ResourceLoaderClassFinder;

/**
 * NOTE: this is mostly a convenience wrapper for constructing the ResourceLoaderClassFinder with the correct spring-
 * boot resource loader.
 *
 * Created by art on 3/23/18.
 */
public class SpringBootCucumberClassFinder extends ResourceLoaderClassFinder {

  public SpringBootCucumberClassFinder(ClassLoader classLoader) {
    super(new SpringBootCucumberResourceLoader(classLoader), classLoader);
  }
}
