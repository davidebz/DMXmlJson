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

package bz.davide.dmxmljson.unmarshalling.xml;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.Value;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class W3CXMLStructure implements Structure
{
   ElementAndSubtype                             element;
   HashMap<String, ArrayList<ElementAndSubtype>> elementsByName = new HashMap<String, ArrayList<ElementAndSubtype>>();

   W3CXMLStructure(ElementAndSubtype element)
   {
      super();
      this.element = element;
      this.extractChildElementByName();
   }

   public W3CXMLStructure(InputStream inputStream) throws ParserConfigurationException, SAXException,
            IOException
   {
      this(domParser(inputStream));
   }

   static ElementAndSubtype domParser(InputStream inputStream) throws ParserConfigurationException,
                                                              SAXException,
                                                              IOException
   {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      Document doc = docBuilder.parse(inputStream);

      ElementAndSubtype elementAndSubtype = new ElementAndSubtype();
      elementAndSubtype.element = doc.getDocumentElement();
      String[] parts = extractNameAndSubtype(elementAndSubtype.element.getTagName());
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
            String[] parts = W3CXMLStructure.extractNameAndSubtype(tagName);

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
         }
      }
   }

   @Override
   public void open()
   {
   }

   @Override
   public String getId() throws SQLException
   {
      String attr = this.element.element.getAttribute("id");
      if (attr != null && attr.length() > 0)
      {
         return attr;
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
   public String getRefId() throws SQLException
   {
      String attr = this.element.element.getAttribute("refid");
      if (attr != null && attr.length() > 0)
      {
         return attr;
      }
      return null;
   }

   @Override
   public Value property(String name)
   {
      ArrayList<ElementAndSubtype> es = this.elementsByName.get(name);
      if (es == null || es.size() == 0)
      {
         // attribute with this name?
         if (this.element.element.hasAttribute(name))
         {
            return new W3CXMLValue(this.element.element.getAttribute(name));
         }
         return null;
      }
      return new W3CXMLValue(es);
   }

   @Override
   public void close()
   {

   }

}
