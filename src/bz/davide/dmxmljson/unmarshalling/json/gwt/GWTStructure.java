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

import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class GWTStructure implements Structure
{
   JSONObject jsonObject;

   public GWTStructure(JSONObject jsonObject)
   {
      super();
      this.jsonObject = jsonObject;
   }

   @Override
   public void open()
   {

   }

   @Override
   public String getRefId() throws Exception
   {
      JSONValue refid = this.jsonObject.get("__refid");
      if (refid == null)
      {
         return null;
      }
      return ((com.google.gwt.json.client.JSONString) refid).stringValue();
   }

   @Override
   public String getRuntimeClassName(String compileTimeClassName)
   {
      JSONValue subclass = this.jsonObject.get("__subclass");
      if (subclass == null)
      {
         return compileTimeClassName;
      }
      return ((com.google.gwt.json.client.JSONString) subclass).stringValue();
   }

   @Override
   public Value property(String name)
   {
      JSONValue val = this.jsonObject.get(name);
      if (val == null)
      {
         return null;
      }
      return new GWTValue(val);
   }

   @Override
   public String getId() throws Exception
   {
      JSONValue id = this.jsonObject.get("__id");
      if (id == null)
      {
         return null;
      }
      return ((com.google.gwt.json.client.JSONString) id).stringValue();
   }

   @Override
   public void close()
   {

   }

}
