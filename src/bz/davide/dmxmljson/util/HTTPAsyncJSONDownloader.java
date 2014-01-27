/*
DMXmlJson - Java binding framework for xml and json - http://www.davide.bz/dmxj

Copyright (C) 2013 Davide Montesin <d@vide.bz> - Bolzano/Bozen - Italy

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>
*/

package bz.davide.dmxmljson.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class HTTPAsyncJSONDownloader implements AsyncJSONDownloader
{
   @Override
   public void download(final String url, final AsyncJSONDownloaderCallback callback) throws IOException
   {
      new Thread(new Runnable()
      {
         @Override
         public void run()
         {
            try
            {
               URL con = new URL(url);
               InputStream is = con.openStream();
               ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
               copyAllBytesAndCloseStreams(is, outputStream);
               String json = outputStream.toString("UTF-8");
               callback.ready(json);
            }
            catch (IOException ioxxx)
            {
               callback.exception(ioxxx);
            }
         }
      }).start();
   }

   public static void copyAllBytesAndCloseStreams(InputStream is, OutputStream outputStream) throws IOException
   {
      int len;
      byte[] buf = new byte[100000];
      while ((len = is.read(buf)) > 0)
      {
         outputStream.write(buf, 0, len);
      }
      outputStream.close();
      is.close();
   }

}
