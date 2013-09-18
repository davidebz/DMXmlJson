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

package bz.davide.dmxmljson.marshalling;

import java.util.HashMap;
import java.util.IdentityHashMap;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class Marshaller
{
   private HashMap<String, ClassMarshaller> classMarshallers = new HashMap<String, ClassMarshaller>();
   private HashMap<String, String>          short2longName   = new HashMap<String, String>();

   protected Marshaller()
   {
   }

   protected String shortName(String name)
   {
      String shortName = name;
      int lastDotPos = name.lastIndexOf('.');
      if (lastDotPos > 0)
      {
         shortName = name.substring(lastDotPos + 1);
      }
      return shortName;
   }

   protected void putClassMarshaller(String name, ClassMarshaller classMarshaller)
   {
      String shortName = this.shortName(name);
      String fullName = this.short2longName.get(shortName);
      if (fullName != null && !fullName.equals(name))
      {
         throw new RuntimeException("confict on the short name " + shortName + " for classes " + fullName + " and " + name);
      }
      this.classMarshallers.put(name, classMarshaller);
      this.short2longName.put(shortName, name);
   }

   public void marschall(Object obj, Structure structure) throws Exception
   {
      this.internalMarschall(obj,
                             obj.getClass().getName(),
                             obj.getClass().getName(),
                             structure,
                             new IdentityHashMap<Object, Structure>(),
                             new long[] { 0 },
                             false);
   }

   protected void internalMarschall(Object obj,
                                    String runtimeClassName,
                                    String compileTimeClassName,
                                    Structure structure,
                                    IdentityHashMap<Object, bz.davide.dmxmljson.marshalling.Structure> identities,
                                    long[] seq,
                                    boolean superClass) throws Exception
   {
      this.getClassMarshaller(runtimeClassName).marshall(obj,
                                                         compileTimeClassName,
                                                         structure,
                                                         identities,
                                                         seq,
                                                         superClass);
   }

   private ClassMarshaller getClassMarshaller(String className) throws ClassMarshallerNotFoundException
   {
      ClassMarshaller ret = this.classMarshallers.get(className);
      if (ret == null)
      {
         throw new ClassMarshallerNotFoundException(className);
      }
      return ret;
   }

   protected boolean isReference(Structure curr,
                                 Object obj,
                                 IdentityHashMap<Object, Structure> identities,
                                 long[] seq) throws Exception
   {
      Structure structure = identities.get(obj);
      if (structure == null)
      {
         return false;
      }
      String id = structure.getId();
      if (id == null)
      {
         id = "ID" + seq[0];
         seq[0]++;
         structure.setId(id);
      }
      curr.open("", "", id);
      curr.close();
      return true;
   }
}
