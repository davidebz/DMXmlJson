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

package bz.davide.dmxmljson.unmarshalling;

import java.util.HashMap;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class Unmarshaller
{
   private HashMap<String, ClassUnmarshaller> classUnmarshallers = new HashMap<String, ClassUnmarshaller>();
   private HashMap<String, InstanceFactory>   instanceFactories  = new HashMap<String, InstanceFactory>();

   private HashMap<String, String>            short2longName     = new HashMap<String, String>();

   protected void putInstanceFactory(String name, InstanceFactory instanceFactory)
   {
      String shortName = name;
      int lastDotPos = name.lastIndexOf('.');
      if (lastDotPos > 0)
      {
         shortName = name.substring(lastDotPos + 1);
      }
      String fullName = this.short2longName.get(shortName);
      if (fullName != null && !fullName.equals(name))
      {
         throw new RuntimeException("confict on the short name " +
                                    shortName +
                                    " for classes " +
                                    fullName +
                                    " and " +
                                    name);
      }
      this.instanceFactories.put(name, instanceFactory);
      this.short2longName.put(shortName, name);
   }

   protected void putClassUnmarshaller(String name, ClassUnmarshaller classUnmarshaller)
   {
      this.classUnmarshallers.put(name, classUnmarshaller);
   }

   public Object newInstance(String className) throws Exception
   {
      String longName = this.short2longName.get(className);
      if (longName == null)
      {
         throw new RuntimeException("Full classname not found for short name: " + className);
      }
      return this.instanceFactories.get(longName).newInstance();
   }

   public void unmarschall(Structure structure, Object obj) throws Exception
   {
      this.internalUnmarschall(structure, obj.getClass().getName(), obj, new HashMap<String, Object>());
   }

   protected void internalUnmarschall(Structure structure,
                                      String className,
                                      Object obj,
                                      HashMap<String, Object> identities) throws Exception
   {
      this.getClassUnmarshaller(className).unmarshall(structure, obj, identities);
   }

   private ClassUnmarshaller getClassUnmarshaller(String clazz) throws ClassUnmarshallerNotFoundException
   {
      ClassUnmarshaller ret = this.classUnmarshallers.get(clazz);
      if (ret == null)
      {
         throw new ClassUnmarshallerNotFoundException(clazz);
      }
      return ret;
   }
}
