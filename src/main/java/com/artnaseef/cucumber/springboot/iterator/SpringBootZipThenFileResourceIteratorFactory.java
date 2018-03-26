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

import java.net.URL;
import java.util.Iterator;

import cucumber.runtime.io.FileResourceIteratorFactory;
import cucumber.runtime.io.Resource;
import cucumber.runtime.io.ResourceIteratorFactory;

/**
 * Created by art on 3/23/18.
 */
public class SpringBootZipThenFileResourceIteratorFactory implements ResourceIteratorFactory {
    private final ResourceIteratorFactory
        springBootZipResourceIteratorFactory = new SpringBootZipResourceIteratorFactory();
    private final ResourceIteratorFactory fileResourceIteratorFactory = new FileResourceIteratorFactory();

    @Override
    public boolean isFactoryFor(URL url) {
        return springBootZipResourceIteratorFactory.isFactoryFor(url) || fileResourceIteratorFactory.isFactoryFor(url);
    }

    @Override
    public Iterator<Resource> createIterator(URL url, String path, String suffix) {
        if (springBootZipResourceIteratorFactory.isFactoryFor(url)) {
            return springBootZipResourceIteratorFactory.createIterator(url, path, suffix);
        } else {
            return fileResourceIteratorFactory.createIterator(url, path, suffix);
        }
    }
}
