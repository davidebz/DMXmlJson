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

package bz.davide.dmxmljson.unmarshalling.xml.gwt;

import java.util.ArrayList;

import bz.davide.dmxmljson.unmarshalling.Array;
import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class GWTXMLValue implements Value
{

   String                       attributeValue = null;

   ArrayList<ElementAndSubtype> elements;

   public GWTXMLValue(String attributeValue)
   {
      super();
      this.attributeValue = attributeValue;
   }

   public GWTXMLValue(ArrayList<ElementAndSubtype> elems)
   {
      super();
      this.elements = elems;
   }

   @Override
   public boolean isNull()
   {
      return false;
   }

   @Override
   public String string()
   {
      if (this.attributeValue != null)
      {
         return this.attributeValue;
      }
      return this.getNodeText(this.elements.get(0).element);
   }

   @Override
   public long integer()
   {
      if (this.attributeValue != null)
      {
         return Long.parseLong(this.attributeValue);
      }
      return Long.parseLong(this.getNodeText(this.elements.get(0).element));
   }

   @Override
   public double decimal()
   {
      if (this.attributeValue != null)
      {
         return Double.parseDouble(this.attributeValue);
      }
      return Double.parseDouble(this.getNodeText(this.elements.get(0).element));
   }

   @Override
   public boolean booleanValue()
   {
      if (this.attributeValue != null)
      {
         return Boolean.parseBoolean(this.attributeValue);
      }
      return Boolean.parseBoolean(this.getNodeText(this.elements.get(0).element));
   }

   @Override
   public Array array()
   {
      return new GWTXMLArray(this.elements);
   }

   @Override
   public Structure structure()
   {
      return new GWTXMLStructure(this.elements.get(0));
   }

   String getNodeText(Node node)
   {
      StringBuffer sb = new StringBuffer();
      this.recursiveText(node, sb);
      return sb.toString();
   }

   void recursiveText(Node node, StringBuffer sb)
   {
      if (node.getNodeType() == Node.TEXT_NODE)
      {
         sb.append(node.getNodeValue());
      }
      if (node.getNodeType() == Node.ELEMENT_NODE)
      {
         NodeList nl = node.getChildNodes();
         for (int i = 0; i < nl.getLength(); i++)
         {
            this.recursiveText(nl.item(i), sb);
         }
      }
   }

}
