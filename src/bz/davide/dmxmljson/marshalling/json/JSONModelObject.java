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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class JSONModelObject extends JSONModelValue
{

   private ArrayList<String>       attributeNames = new ArrayList<String>();
   HashMap<String, JSONModelValue> attributes     = new HashMap<String, JSONModelValue>();

   @Override
   public void toJSON(StringBuffer buffer, int indent)
   {

      buffer.append(SPACES.substring(0, indent * INDENTNR));

      buffer.append("{");
      for (int i = 0; i < this.attributeNames.size(); i++)
      {
         String attrName = this.attributeNames.get(i);
         buffer.append("\n");
         buffer.append(SPACES.substring(0, (indent + 1) * INDENTNR));
         buffer.append(attrName);
         buffer.append(": ");
         JSONModelValue value = this.attributes.get(attrName);
         if (value instanceof JSONModelObject)
         {
            JSONModelObject jsonObject = (JSONModelObject) value;
            if (jsonObject.attributeNames.size() > 0)
            {
               buffer.append("\n");
            }
         }
         value.toJSON(buffer, indent + 2);
         if (i < this.attributeNames.size() - 1)
         {
            buffer.append(",");
         }
      }
      if (this.attributeNames.size() > 0)
      {
         buffer.append("\n");
         buffer.append(SPACES.substring(0, indent * INDENTNR));
      }
      buffer.append("}");

   }

   final static int    INDENTNR = 0;
   final static String SPACES   = "                                                                                                                                ";

   void putAttribute(String name, JSONModelValue jsonValue)
   {
      if (!this.attributes.containsKey(name))
      {
         this.attributeNames.add(name);
      }
      this.attributes.put(name, jsonValue);
   }

}