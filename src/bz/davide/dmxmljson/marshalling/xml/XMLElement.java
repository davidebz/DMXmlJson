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

import java.util.ArrayList;
import java.util.HashMap;

class XMLElement
{
   private String                  tagName;
   private HashMap<String, String> attributes = null;
   private String                  innerText  = null;
   private ArrayList<XMLElement>   childs     = null;

   public XMLElement(String tagName)
   {
      super();
      this.tagName = tagName;
   }

   void addChild(XMLElement child)
   {
      if (this.childs == null)
      {
         this.childs = new ArrayList<XMLElement>();
      }
      this.childs.add(child);
   }

   void putAttribute(String name, String value)
   {
      if (this.attributes == null)
      {
         this.attributes = new HashMap<String, String>();
      }
      this.attributes.put(name, value);
   }

   String getAttribute(String name)
   {
      if (this.attributes == null)
      {
         return null;
      }
      return this.attributes.get(name);
   }

   public void setInnerText(String innerText)
   {
      this.innerText = innerText;
   }

   void toXML(StringBuffer buffer, int indent)
   {
      buffer.append(SPACES.substring(0, indent * INDENTNR));
      buffer.append("<");
      buffer.append(this.tagName);
      if (this.attributes != null)
      {
         for (String attrName : this.attributes.keySet())
         {
            buffer.append(" ");
            buffer.append(attrName);
            buffer.append("=\"");
            String value = this.attributes.get(attrName);
            buffer.append(value);
            buffer.append("\"");
         }
      }
      buffer.append(">");
      if (this.innerText != null)
      {
         buffer.append(this.innerText);

      }
      else
      {
         if (this.childs != null)
         {
            buffer.append("\n");
            for (XMLElement c : this.childs)
            {
               c.toXML(buffer, indent + 1);
            }
            buffer.append(SPACES.substring(0, indent * INDENTNR));
         }
      }
      buffer.append("</");
      buffer.append(this.tagName);
      buffer.append(">\n");
   }

   final static int    INDENTNR = 3;
   final static String SPACES   = "                                                                                                                                ";
}
