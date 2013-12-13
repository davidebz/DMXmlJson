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

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class JSONModelArray extends JSONModelValue
{
   ArrayList<JSONModelValue> items = new ArrayList<JSONModelValue>();

   @Override
   public void toJSON(StringBuffer buffer, int indent, int INDENTNR)
   {
      buffer.append("[");
      for (int i = 0; i < this.items.size(); i++)
      {
         if (INDENTNR > 0)
            buffer.append("\n");
         JSONModelValue jsonValue = this.items.get(i);
         jsonValue.toJSON(buffer, indent + 1, INDENTNR);
         if (i < this.items.size() - 1)
         {
            buffer.append(",");
         }
      }
      if (this.items.size() > 0 && INDENTNR > 0)
      {
         buffer.append("\n");
         buffer.append(JSONModelObject.SPACES(indent * INDENTNR));
      }
      buffer.append("]");
   }
}
