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

package bz.davide.dmxmljson.marshalling.json;

import bz.davide.dmxmljson.marshalling.Array;
import bz.davide.dmxmljson.marshalling.Structure;
import bz.davide.dmxmljson.marshalling.Value;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class JSONValue implements Value
{

   String          name;
   JSONModelObject jsonModelObject;
   int             indent;

   public JSONValue(String name, JSONModelObject jsonModelObject, int indent)
   {
      super();
      this.name = name;
      this.jsonModelObject = jsonModelObject;
      this.indent = indent;
   }

   @Override
   public void nullValue()
   {
      this.jsonModelObject.putAttribute(this.name, new JSONModelNull());
   }

   @Override
   public void string(String value)
   {
      this.jsonModelObject.putAttribute(this.name, new JSONModelString(value));

   }

   @Override
   public void integer(long value)
   {
      this.jsonModelObject.putAttribute(this.name, new JSONModelInteger(value));
   }

   @Override
   public void decimal(double value)
   {
      this.jsonModelObject.putAttribute(this.name, new JSONModelDecimal(value));
   }

   @Override
   public void booleanValue(boolean value)
   {
      this.jsonModelObject.putAttribute(this.name, new JSONModelBoolean(value));
   }

   @Override
   public Array array(int len)
   {
      JSONStructureArray jsonStructureArray = new JSONStructureArray(indent);
      this.jsonModelObject.putAttribute(this.name, jsonStructureArray.array);
      return jsonStructureArray;
   }

   @Override
   public Structure structure()
   {
      JSONStructure jsonStructure = new JSONStructure(indent);
      this.jsonModelObject.putAttribute(this.name, jsonStructure.jsonObject);
      return jsonStructure;
   }

}
