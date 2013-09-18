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

import bz.davide.dmxmljson.marshalling.Array;
import bz.davide.dmxmljson.marshalling.Structure;
import bz.davide.dmxmljson.marshalling.Value;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class JSONArrayItem implements Value
{
   JSONModelArray jsonModelArray;
   int            index;

   public JSONArrayItem(JSONModelArray jsonModelArray, int index)
   {
      super();
      this.jsonModelArray = jsonModelArray;
      this.index = index;
   }

   @Override
   public void nullValue()
   {
      this.jsonModelArray.items.set(this.index, new JSONModelNull());
   }

   @Override
   public void string(String value)
   {
      this.jsonModelArray.items.set(this.index, new JSONModelString(value));
   }

   @Override
   public void integer(long value)
   {
      this.jsonModelArray.items.set(this.index, new JSONModelInteger(value));
   }

   @Override
   public void booleanValue(boolean value)
   {
      this.jsonModelArray.items.set(this.index, new JSONModelBoolean(value));
   }

   @Override
   public void decimal(double value)
   {
      this.jsonModelArray.items.set(this.index, new JSONModelDecimal(value));
   }

   @Override
   public Array array()
   {
      JSONStructureArray jsonStructureArray = new JSONStructureArray();
      this.jsonModelArray.items.set(this.index, jsonStructureArray.array);
      return jsonStructureArray;
   }

   @Override
   public Structure structure()
   {
      JSONStructure jsonStructure = new JSONStructure();
      this.jsonModelArray.items.set(this.index, jsonStructure.jsonObject);
      return jsonStructure;
   }

}
