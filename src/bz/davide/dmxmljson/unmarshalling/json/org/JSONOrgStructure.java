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
      String refid = this.jsonObject.optString("__refid", null);
      return refid;
   }

   @Override
   public String getRuntimeClassName(String compileTimeClassName)
   {
         String subclass = this.jsonObject.optString("__subclass", null);
         if (subclass == null)
         {
            subclass = compileTimeClassName;
         }
         return subclass;
   }

   @Override
   public Value property(String name)
   {
      Object val = jsonObject.opt(name);
      if (val == null)
      {
         return null;
      }
      return new JSONOrgValue(val);
   }

   @Override
   public String getId() throws Exception
   {
      String id = this.jsonObject.optString("__id", null);
      return id;
   }

   @Override
   public void close()
   {

   }

}
