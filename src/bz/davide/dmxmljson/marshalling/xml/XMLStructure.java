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

import bz.davide.dmxmljson.marshalling.Structure;
import bz.davide.dmxmljson.marshalling.Value;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class XMLStructure implements Structure
{

   XMLStructureRules rules;

   XMLElement        xmlElement;

   public XMLStructure(String tagName, XMLStructureRules rules)
   {
      this.xmlElement = new XMLElement(tagName);
      this.rules = rules;
   }

   public XMLStructure(String tagName)
   {
      this(tagName, new XMLStructureRules());
   }

   @Override
   public void open(String compiletimeClassName, String runtimeClassName, String refid) throws Exception
   {
      if (!compiletimeClassName.equals(runtimeClassName))
      {
         this.xmlElement.putAttribute("subclass", runtimeClassName);
      }
      if (refid != null)
      {
         this.xmlElement.putAttribute("refid", refid);
      }
   }

   @Override
   public Value property(String name)
   {
      XMLValue xmlValue = new XMLValue(this.xmlElement, name, this.rules, false);
      return xmlValue;
   }

   @Override
   public void close()
   {
   }

   @Override
   public void setId(String id)
   {
      this.xmlElement.putAttribute("id", id);
   }

   @Override
   public String getId()
   {
      return this.xmlElement.getAttribute("id");
   }

   public void toXML(StringBuffer buffer)
   {
      this.xmlElement.toXML(buffer, 0);
   }

   @Override
   public String toString()
   {
      StringBuffer sb = new StringBuffer();
      this.toXML(sb);
      return sb.toString();
   }
}
