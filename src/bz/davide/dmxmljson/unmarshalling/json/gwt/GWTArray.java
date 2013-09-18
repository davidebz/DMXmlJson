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

package bz.davide.dmxmljson.unmarshalling.json.gwt;

import bz.davide.dmxmljson.unmarshalling.Array;
import bz.davide.dmxmljson.unmarshalling.Value;

import com.google.gwt.json.client.JSONArray;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class GWTArray implements Array
{
   JSONArray jsonArray;
   int       pos = 0;

   public GWTArray(JSONArray jsonArray)
   {
      this.jsonArray = jsonArray;
   }

   @Override
   public void open()
   {

   }

   @Override
   public Value nextItem()
   {
      if (this.pos >= this.jsonArray.size())
      {
         return null;
      }
      GWTValue gwtValue = new GWTValue(this.jsonArray.get(this.pos));
      this.pos++;
      return gwtValue;
   }

   @Override
   public void close()
   {

   }

}
