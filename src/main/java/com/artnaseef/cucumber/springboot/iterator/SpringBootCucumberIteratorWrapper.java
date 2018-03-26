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

import java.util.Iterator;

import cucumber.runtime.io.Resource;

/**
 * Created by art on 3/23/18.
 */
public class SpringBootCucumberIteratorWrapper implements Iterator<Resource> {

  private final Iterator<Resource> nested;

  public SpringBootCucumberIteratorWrapper(Iterator<Resource> nested) {
    this.nested = nested;
  }

  @Override
  public boolean hasNext() {
    return this.nested.hasNext();
  }

  @Override
  public Resource next() {
    Resource nestedResource = this.nested.next();

    return new SpringBootCucumberResourceWrapper(nestedResource);
  }
}
