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

package bz.davide.dmxmljson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class FieldOrMethod
{
   public final String name;
   public final Class  type;
   public final Type   genericType;

   public final Field  field;
   public final Method method;

   public static enum Access
   {
      FIELD, METHOD
   };

   private FieldOrMethod(String name, Class type, Type genericType, Field field, Method method)
   {
      this.name = name;
      this.type = type;
      this.genericType = genericType;
      this.field = field;
      this.method = method;
   }

   public abstract String writeSetCode(String codeValue);

   public static void allFieldOrMethods(Class clazz, Access type, ArrayList<FieldOrMethod> fom)
   {
      allFieldOrMethodsRecursive(clazz, type, fom);
      Collections.sort(fom, new Comparator<FieldOrMethod>()
      {
         @Override
         public int compare(FieldOrMethod o1, FieldOrMethod o2)
         {
            return o1.name.compareTo(o2.name);
         }
      });
   }

   private static void allFieldOrMethodsRecursive(Class clazz, Access type, ArrayList<FieldOrMethod> fom)
   {
      if (clazz.getName().equals("java.lang.Object"))
      {
         return;
      }
      if (type == Access.FIELD)
      {
         Field[] fs = clazz.getDeclaredFields();
         for (final Field f : fs)
         {
            if (Modifier.isTransient(f.getModifiers()))
            {
               continue;
            }
            if (Modifier.isStatic(f.getModifiers()))
            {
               continue;
            }
            fom.add(new FieldOrMethod(f.getName(), f.getType(), f.getGenericType(), f, null)
            {

               @Override
               public String writeSetCode(String codeValue)
               {
                  return f.getName() + " = " + codeValue;
               }
            });
         }
      }
      if (type == Access.METHOD)
      {
         Method[] methods = clazz.getDeclaredMethods();
         for (final Method m : methods)
         {
            if (Modifier.isStatic(m.getModifiers()))
            {
               continue;
            }
            if (m.getName().startsWith("set") && m.getParameterTypes().length == 1)
            {
               fom.add(new FieldOrMethod(m.getName().substring(3),
                                         m.getParameterTypes()[0],
                                         m.getGenericParameterTypes()[0],
                                         null,
                                         m)
               {
                  @Override
                  public String writeSetCode(String codeValue)
                  {
                     return m.getName() + "(" + codeValue + ")";
                  }
               });
            }
         }
      }
   }
}
