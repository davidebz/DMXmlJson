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

package bz.davide.dmxmljson.unmarshalling.xml.gwt;

import java.util.ArrayList;
import java.util.HashMap;

import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class GWTXMLStructure implements Structure
{
   ElementAndSubtype                             element;
   HashMap<String, ArrayList<ElementAndSubtype>> elementsByName = new HashMap<String, ArrayList<ElementAndSubtype>>();
   ArrayList<ElementAndSubtype>                  childNodes     = new ArrayList<ElementAndSubtype>();

   GWTXMLStructure(ElementAndSubtype element)
   {
      super();
      this.element = element;
      this.extractChildElementByName();
   }

   public GWTXMLStructure(String xmlText)
   {
      this(domParser(xmlText));
   }

   static ElementAndSubtype domParser(String xmlText)
   {

      Document doc = XMLParser.parse(xmlText);

      ElementAndSubtype elementAndSubtype = new ElementAndSubtype();
      Element documentElement = doc.getDocumentElement();
      elementAndSubtype.element = documentElement;
      String[] parts = extractNameAndSubtype(documentElement.getTagName());
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
         Node node = nl.item(i);
         if (node instanceof Element)
         {
            Element element = (Element) node;

            String tagName = element.getTagName();
            String[] parts = GWTXMLStructure.extractNameAndSubtype(tagName);

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
         if (node instanceof Text)
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
      if (this.element.element instanceof Element)
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
      if (this.element.element instanceof Element)
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
      if (this.element.element instanceof Text && name.equals("value"))
      {
         return new GWTXMLValue(this.element.element.getNodeValue());
      }
      ArrayList<ElementAndSubtype> es = this.elementsByName.get(name);
      if (es == null || es.size() == 0)
      {
         // attribute with this name?
         if (((Element) this.element.element).hasAttribute(name))
         {
            return new GWTXMLValue(((Element) this.element.element).getAttribute(name));
         }
         if (name.equals("childNodes")) // special case, when xml is used as list of childNodes and not as a map like in xhtml
         {
            return new GWTXMLValue(this.childNodes);
         }
         return null;
      }
      return new GWTXMLValue(es);
   }

   @Override
   public void close()
   {

   }

}
