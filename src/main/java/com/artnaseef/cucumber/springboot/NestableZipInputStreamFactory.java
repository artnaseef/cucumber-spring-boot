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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by art on 3/25/18.
 */
public class NestableZipInputStreamFactory {

  public ZipInputStream open(URL url) throws IOException {
    ZipInputStream result;

    String[] pathParts = url.getPath().split("!");
    if (pathParts.length > 1) {
      URL outtermostUrl = new URL("file", url.getHost(), url.getPort(), pathParts[0].replaceFirst("^file:", ""));

      //
      // Open the outtermost input stream.
      //
      ZipInputStream currentInputStream = null;
      try {
        currentInputStream = new ZipInputStream(outtermostUrl.openStream());

        int cur = 1;
        while (cur < pathParts.length) {
          String onePart = pathParts[cur];

          InputStream nextEmbeddedInputStream = openZipEntry(currentInputStream, onePart);

          if (nextEmbeddedInputStream == null) {
            throw new FileNotFoundException("did not find entry " + onePart + " while navigating url " + url);
          }

          currentInputStream = new NestedZipInputStream(currentInputStream, nextEmbeddedInputStream);
          cur++;
        }

        return currentInputStream;
      } catch (IOException ioExc) {
        if (currentInputStream != null) {
          currentInputStream.close();
        }

        throw ioExc;
      }
    } else {
      return new ZipInputStream(url.openStream());
    }
  }

//========================================
// Internals
//----------------------------------------

  private InputStream openZipEntry(ZipInputStream zipInputStream, String wantedEntryName) throws IOException {
    ZipEntry entry = zipInputStream.getNextEntry();
    while ((entry != null) && (!(this.entryNamesMatch(entry.getName(), wantedEntryName)))) {
      entry = zipInputStream.getNextEntry();
    }

    if (entry != null) {
      return zipInputStream;
    }

    return null;
  }

  private boolean entryNamesMatch(String actual, String wanted) {
    if (wanted.equals(actual)) {
      return true;
    }

    if (wanted.startsWith("/")) {
      return wanted.substring(1).equals(actual);
    }

    return false;
  }

  private static class NestedZipInputStream extends ZipInputStream {

    private final ZipInputStream parent;

    public NestedZipInputStream(ZipInputStream parent, InputStream in) {
      super(in);
      this.parent = parent;
    }

    @Override
    public void close() throws IOException {
      super.close();
      this.parent.close();
    }
  }
}
