/*
DMXmlJson - Java binding framework for xml and json - http://www.davide.bz/dmxj

Copyright (C) 2013-2014 Davide Montesin <d@vide.bz> - Bolzano/Bozen - Italy

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
import org.json.JSONObject;

import bz.davide.dmxmljson.unmarshalling.Array;
import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

public class JSONOrgValue implements Value
{
   Object value;

   public JSONOrgValue(Object value)
   {
      super();
      if (value == null)
      {
         throw new IllegalArgumentException("Null Value!");
      }
      this.value = value;
   }

   @Override
   public Array array()
   {
      JSONArray jsonArray = (JSONArray)value;
      return new JSONOrgArray(jsonArray);
   }

   @Override
   public boolean booleanValue()
   {
      return ((Boolean)value).booleanValue();
   }

   @Override
   public double decimal()
   {
      if (value instanceof Integer)
      {
         return (Integer)value;
      }
      return ((Double)value).doubleValue();
      //return Double.parseDouble((String)value);
   }

   @Override
   public long integer()
   {
      if (value instanceof Integer)
      {
         return (Integer)value;
      }
      return Integer.parseInt((String)value);
   }

   @Override
   public boolean isNull()
   {
      return value == JSONObject.NULL;
   }

   @Override
   public String string()
   {
      return (String)value;
   }

   @Override
   public Structure structure()
   {
      return new JSONOrgStructure((JSONObject)value);
   }
}
