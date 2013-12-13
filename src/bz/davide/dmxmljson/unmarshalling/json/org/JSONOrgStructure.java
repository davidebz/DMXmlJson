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

import org.json.JSONException;
import org.json.JSONObject;

import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

public class JSONOrgStructure implements Structure
{

   JSONObject jsonObject = new JSONObject();

   public JSONOrgStructure(JSONObject jsonObject)
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
      if (!jsonObject.has("__refid"))
      {
         return null;
      }
      String refid = this.jsonObject.getString("__refid");
      return refid;
   }

   @Override
   public String getRuntimeClassName(String compileTimeClassName)
   {
      try
      {
         String subclass = compileTimeClassName;
         if (jsonObject.has("__subclass"))
         {
            subclass = this.jsonObject.getString("__subclass");
         }
         return subclass;
      }
      catch (JSONException e)
      {
         return compileTimeClassName;
      }
   }

   @Override
   public Value property(String name)
   {
      Object val;
      try
      {
         if (!jsonObject.has(name))
         {
            return null;
         }
         val = this.jsonObject.get(name);
         return new JSONOrgValue(val);
      }
      catch (JSONException e)
      {
         return null;
      }


   }

   @Override
   public String getId() throws Exception
   {
      if (!this.jsonObject.has("__id"))
      {
         return null;
      }
      String id = this.jsonObject.getString("__id");
      return id;
   }

   @Override
   public void close()
   {

   }

}
