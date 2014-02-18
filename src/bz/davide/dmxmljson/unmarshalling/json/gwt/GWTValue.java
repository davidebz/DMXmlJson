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

package bz.davide.dmxmljson.unmarshalling.json.gwt;

import bz.davide.dmxmljson.unmarshalling.Array;
import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class GWTValue implements Value
{

   JSONValue jsonValue;

   public GWTValue(JSONValue jsonValue)
   {
      super();
      this.jsonValue = jsonValue;
   }

   @Override
   public boolean isNull()
   {
      return this.jsonValue instanceof JSONNull;
   }

   @Override
   public String string()
   {
      return ((JSONString) this.jsonValue).stringValue();
   }

   @Override
   public long integer()
   {
      return (long) ((JSONNumber) this.jsonValue).doubleValue();
   }

   @Override
   public double decimal()
   {
      return ((JSONNumber) this.jsonValue).doubleValue();
   }

   @Override
   public boolean booleanValue()
   {
      return ((JSONBoolean) this.jsonValue).booleanValue();
   }

   @Override
   public Array array()
   {
      return new GWTArray((JSONArray) this.jsonValue);
   }

   @Override
   public Structure structure()
   {
      return new GWTStructure((JSONObject) this.jsonValue);
   }

}
