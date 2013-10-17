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

package bz.davide.dmxmljson.unmarshalling.dom.gwt;

import java.util.ArrayList;
import java.util.HashMap;

import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class GWTDOMStructure implements Structure
{
   ElementAndSubtype                             element;
   HashMap<String, ArrayList<ElementAndSubtype>> elementsByName = new HashMap<String, ArrayList<ElementAndSubtype>>();
   ArrayList<ElementAndSubtype>                  childNodes     = new ArrayList<ElementAndSubtype>();

   GWTDOMStructure(ElementAndSubtype element)
   {
      super();
      this.element = element;
      this.extractChildElementByName();
   }

   public GWTDOMStructure(Element xhtml)
   {
      this(domParser(xhtml));
   }

   static ElementAndSubtype domParser(Element xhtml)
   {

      ElementAndSubtype elementAndSubtype = new ElementAndSubtype();
      elementAndSubtype.element = xhtml;
      String[] parts = extractNameAndSubtype(xhtml.getTagName().toLowerCase());
      elementAndSubtype.subtype = parts[1];

      return elementAndSubtype;
   }

   private static String[] extractNameAndSubtype(String fullName)
   {
      String[] result = new String[2];
      String[] parts = fullName.split("[.-]");
      result[0] = parts[0];
      if (parts.length == 2)
      {
         result[1] = parts[1];
      }
      else
      {
         result[1] = null;
      }
      return result;
   }

   private void extractChildElementByName()
   {
      NodeList nl = this.element.element.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++)
      {
         Node node = nl.getItem(i);
         if (node.getNodeType() == Node.ELEMENT_NODE)
         {
            Element element = (Element) node;

            String tagName = element.getTagName().toLowerCase();
            String[] parts = extractNameAndSubtype(tagName);

            String attrName = parts[0];
            ElementAndSubtype elementAndSubtype = new ElementAndSubtype();
            elementAndSubtype.element = element;
            elementAndSubtype.subtype = parts[1];

            ArrayList<ElementAndSubtype> elements = this.elementsByName.get(attrName);
            if (elements == null)
            {
               elements = new ArrayList<ElementAndSubtype>();
               this.elementsByName.put(attrName, elements);
            }
            elements.add(elementAndSubtype);

            ElementAndSubtype childElementAndSubtype = new ElementAndSubtype();
            childElementAndSubtype.element = element;
            childElementAndSubtype.subtype = tagName;
            // Convert first letter to upper case, because java classes start normally with upper case
            childElementAndSubtype.subtype = childElementAndSubtype.subtype.substring(0, 1).toUpperCase() +
                                             childElementAndSubtype.subtype.substring(1);
            this.childNodes.add(childElementAndSubtype);
         }
         if (node.getNodeType() == Node.TEXT_NODE)
         {
            String txt = node.getNodeValue();
            if (txt.trim().length() > 0)
            {
               ElementAndSubtype childElementAndSubtype = new ElementAndSubtype();
               childElementAndSubtype.element = node;
               childElementAndSubtype.subtype = "TextNode";
               this.childNodes.add(childElementAndSubtype);
            }
         }
      }
   }

   @Override
   public void open()
   {
   }

   @Override
   public String getId() throws Exception
   {
      if (this.element.element.getNodeType() == Node.ELEMENT_NODE)
      {
         String attr = ((Element) this.element.element).getAttribute("id");
         if (attr != null && attr.length() > 0)
         {
            return attr;
         }
      }
      return null;
   }

   @Override
   public String getRuntimeClassName(String compileTimeClassName)
   {
      // @@@ tag instead attribute
      String attr = this.element.subtype;//Attribute("subclass");
      if (attr != null && attr.length() > 0)
      {
         return attr;
      }
      return compileTimeClassName;
   }

   @Override
   public String getRefId() throws Exception
   {
      if (this.element.element.getNodeType() == Node.ELEMENT_NODE)
      {
         String attr = ((Element) this.element.element).getAttribute("refid");
         if (attr != null && attr.length() > 0)
         {
            return attr;
         }
      }
      return null;
   }

   @Override
   public Value property(String name)
   {
      if (this.element.element.getNodeType() == Node.TEXT_NODE && name.equals("value"))
      {
         return new GWTDOMValue(this.element.element.getNodeValue());
      }
      ArrayList<ElementAndSubtype> es = this.elementsByName.get(name);
      if (es == null || es.size() == 0)
      {
         // attribute with this name?
         if (((Element) this.element.element).hasAttribute(name))
         {
            return new GWTDOMValue(((Element) this.element.element).getAttribute(name));
         }
         if (name.equals("childNodes")) // special case, when xml is used as list of childNodes and not as a map like in xhtml
         {
            return new GWTDOMValue(this.childNodes);
         }
         return null;
      }
      return new GWTDOMValue(es);
   }

   @Override
   public void close()
   {

   }

}
