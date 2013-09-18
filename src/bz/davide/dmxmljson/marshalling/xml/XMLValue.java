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

package bz.davide.dmxmljson.marshalling.xml;

import bz.davide.dmxmljson.marshalling.Array;
import bz.davide.dmxmljson.marshalling.Structure;
import bz.davide.dmxmljson.marshalling.Value;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class XMLValue implements Value
{
   XMLStructureRules rules;

   String            propertyName;
   XMLElement        parent;
   boolean           isArrayItem;

   XMLValue(XMLElement parent, String propertyName, XMLStructureRules rules, boolean isArrayItem)
   {
      super();
      this.parent = parent;
      this.rules = rules;
      this.isArrayItem = isArrayItem;
      this.propertyName = propertyName.replace(this.rules.colonReplacement, ":");

   }

   @Override
   public void nullValue()
   {
      XMLElement xmlElement = new XMLElement(this.propertyName);
      this.parent.addChild(xmlElement);
      xmlElement.putAttribute("null", "true");
   }

   @Override
   public void string(String value)
   {
      if (this.rules.primitiveTypePolicy == PrimitiveTypePolicy.ATTRIBUTE && !this.isArrayItem)
      {
         this.parent.putAttribute(this.propertyName, value);
      }
      else
      {
         XMLElement xmlElement = new XMLElement(this.propertyName);
         this.parent.addChild(xmlElement);
         xmlElement.setInnerText(value);
      }
   }

   @Override
   public void integer(long value)
   {
      if (this.rules.primitiveTypePolicy == PrimitiveTypePolicy.ATTRIBUTE)
      {
         this.parent.putAttribute(this.propertyName, String.valueOf(value));
      }
      else
      {
         XMLElement xmlElement = new XMLElement(this.propertyName);
         this.parent.addChild(xmlElement);
         xmlElement.setInnerText(String.valueOf(value));
      }
   }

   @Override
   public void booleanValue(boolean value)
   {
      if (this.rules.primitiveTypePolicy == PrimitiveTypePolicy.ATTRIBUTE)
      {
         this.parent.putAttribute(this.propertyName, String.valueOf(value));
      }
      else
      {
         XMLElement xmlElement = new XMLElement(this.propertyName);
         this.parent.addChild(xmlElement);
         xmlElement.setInnerText(String.valueOf(value));
      }
   }

   @Override
   public void decimal(double value)
   {
      if (this.rules.primitiveTypePolicy == PrimitiveTypePolicy.ATTRIBUTE)
      {
         this.parent.putAttribute(this.propertyName, String.valueOf(value));
      }
      else
      {
         XMLElement xmlElement = new XMLElement(this.propertyName);
         this.parent.addChild(xmlElement);
         xmlElement.setInnerText(String.valueOf(value));
      }
   }

   @Override
   public Array array()
   {
      return new XMLArray(this.parent, this.propertyName, this.rules);
   }

   @Override
   public Structure structure()
   {
      XMLStructure xmlStructure = new XMLStructure(this.propertyName, this.rules);
      this.parent.addChild(xmlStructure.xmlElement);
      return xmlStructure;
   }

}
