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

package bz.davide.dmxmljson.marshalling.json;

import bz.davide.dmxmljson.marshalling.Structure;
import bz.davide.dmxmljson.marshalling.Value;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class JSONStructure implements Structure
{
   JSONModelObject jsonObject = new JSONModelObject();

   @Override
   public void open(String compiletimeClassName, String runtimeClassName, String refid) throws Exception
   {
      if (!compiletimeClassName.equals(runtimeClassName))
      {
         this.jsonObject.putAttribute("__subclass", new JSONModelString(runtimeClassName));
      }
      if (refid != null)
      {
         this.jsonObject.putAttribute("__refid", new JSONModelString(refid));
      }
   }

   @Override
   public Value property(String name)
   {
      return new JSONValue(name, this.jsonObject);
   }


   @Override
   public void close()
   {

   }

   @Override
   public String getId()
   {
      JSONModelString jsonString = (JSONModelString) this.jsonObject.attributes.get("__id");
      if (jsonString == null)
      {
         return null;
      }
      return jsonString.value;
   }

   @Override
   public void setId(String id)
   {
      this.jsonObject.putAttribute("__id", new JSONModelString(id));
   }

   public void toJSON(StringBuffer buffer)
   {
      this.jsonObject.toJSON(buffer, 0);
   }

   @Override
   public String toString()
   {
      StringBuffer sb = new StringBuffer();
      this.toJSON(sb);
      return sb.toString();
   }
}
