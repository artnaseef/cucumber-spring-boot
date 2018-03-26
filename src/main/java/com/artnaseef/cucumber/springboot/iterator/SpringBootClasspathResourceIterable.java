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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;

import cucumber.runtime.CucumberException;
import cucumber.runtime.io.DelegatingResourceIteratorFactory;
import cucumber.runtime.io.FlatteningIterator;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceIteratorFactory;

/**
 * NOTE: most of this code is copied from Cucumber sources.  The key difference between this implementation and the
 * core Cucumber version is the use of the SpringBootZipThenFileResourceIterationFactory.
 *
 * @see cucumber.runtime.io.ClasspathResourceIterable
 *
 * Created by art on 3/23/18.
 */
public class SpringBootClasspathResourceIterable implements Iterable<Resource> {
    private final ResourceIteratorFactory resourceIteratorFactory =
            new DelegatingResourceIteratorFactory(new SpringBootZipThenFileResourceIteratorFactory());

    private final ClassLoader classLoader;
    private final String path;
    private final String suffix;

    public SpringBootClasspathResourceIterable(ClassLoader classLoader, String path, String suffix) {
        this.classLoader = classLoader;
        this.path = path;
        this.suffix = suffix;
    }

    @Override
    public Iterator<Resource> iterator() {
        try {
            FlatteningIterator<Resource> iterator = new FlatteningIterator<Resource>();
            Enumeration<URL> resources = classLoader.getResources(path);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Iterator<Resource> resourceIterator = resourceIteratorFactory.createIterator(url, path, suffix);
                iterator.push(resourceIterator);
            }
            return iterator;
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

}
