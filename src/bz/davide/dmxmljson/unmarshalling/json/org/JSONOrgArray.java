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

package bz.davide.dmxmljson.unmarshalling.json.org;

import org.json.JSONArray;
import org.json.JSONException;

import bz.davide.dmxmljson.unmarshalling.Array;
import bz.davide.dmxmljson.unmarshalling.Value;

public class JSONOrgArray implements Array
{
   JSONArray jsonArray;
   int pos = 0;
   int len;

   public JSONOrgArray(JSONArray jsonArray)
   {
      this.jsonArray = jsonArray;
      this.len = jsonArray.length();
   }

   @Override
   public void close()
   {
   }

   @Override
   public Value nextItem()
   {
      Object val;
      try
      {
         if (pos >= len)
         {
            return null;
         }
         val = jsonArray.get(pos);
         pos++;
         return new JSONOrgValue(val);
      }
      catch (JSONException e)
      {
         e.printStackTrace();
         return null;
      }

   }

   @Override
   public void open()
   {
   }

}
