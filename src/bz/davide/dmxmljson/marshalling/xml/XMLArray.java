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

package bz.davide.dmxmljson.marshalling.xml;

import bz.davide.dmxmljson.marshalling.Array;
import bz.davide.dmxmljson.marshalling.Value;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class XMLArray implements Array
{
   String            name;
   XMLElement        parent;
   XMLStructureRules rules;

   XMLArray(XMLElement parent, String name, XMLStructureRules rules)
   {
      super();
      this.parent = parent;
      this.name = name;
      this.rules = rules;
   }

   @Override
   public void open()
   {
   }

   @Override
   public void close()
   {
   }

   @Override
   public Value item()
   {
      XMLValue xmlValue = new XMLValue(this.parent, this.name, this.rules, true);
      return xmlValue;
   }
}
