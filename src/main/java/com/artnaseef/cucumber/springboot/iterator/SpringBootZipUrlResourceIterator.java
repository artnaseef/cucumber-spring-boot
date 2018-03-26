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

import com.artnaseef.cucumber.springboot.NestableZipInputStreamFactory;
import com.artnaseef.cucumber.springboot.SpringBootCucumberResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cucumber.runtime.io.Resource;

/**
 * Created by art on 3/23/18.
 */
public class SpringBootZipUrlResourceIterator implements Iterator<Resource> {

  private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(SpringBootZipUrlResourceIterator.class);

  private Logger log = DEFAULT_LOGGER;

  private NestableZipInputStreamFactory nestableZipInputStreamFactory;

  private final String path;
  private final String suffix;
  private final URL jarUrl;
  private final ZipInputStream jarInputStream;
  private Resource next;

  public SpringBootZipUrlResourceIterator(URL zipUrl, String path, String suffix) throws IOException {
    this.path = path;
    this.suffix = suffix;
    this.jarUrl = zipUrl;

    this.nestableZipInputStreamFactory = new NestableZipInputStreamFactory();
    this.jarInputStream = this.nestableZipInputStreamFactory.open(zipUrl);

    moveToNext();
  }

  @Override
  public boolean hasNext() {
    return next != null;
  }

  @Override
  public Resource next() {
    try {
      if (next == null) {
        throw new NoSuchElementException();
      }
      return next;
    } finally {
      moveToNext();
    }
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  private void moveToNext() {
    this.next = null;

    try {
      ZipEntry nextEntry = this.jarInputStream.getNextEntry();
      while ((this.next == null) && (nextEntry != null)) {
        String entryName = nextEntry.getName();

        if (((entryName.startsWith(path)) || ((entryName.startsWith("BOOT-INF/classes/" + path)))) && (hasSuffix(suffix, entryName))) {
          this.log.debug("matched: entry-name={}; wanted-path={}; wanted-suffix={}", entryName, path, suffix);

          URL entryUrl = new URL(this.jarUrl, "!" + entryName);

          String className = entryName.replace('/', '.');
          if (className.startsWith("BOOT-INF.classes.")) {
            className = className.substring(17);
          }

          className = className.substring(0, className.length() - suffix.length());

          this.next = new SpringBootCucumberResource(entryUrl, className);
        } else {
            this.log.debug("unmatched: entry-name={}; wanted-path={}; wanted-suffix={}", entryName, path, suffix);
            nextEntry = this.jarInputStream.getNextEntry();
        }
      }
    } catch (IOException ioExc) {
      this.log.warn("error scanning jar resource: url={}", this.jarUrl, ioExc);
    }
  }

  private boolean hasSuffix(String suffix, String name) {
    if (suffix == null) {
      return true;
    }

    return name.endsWith(suffix);
  }
}
