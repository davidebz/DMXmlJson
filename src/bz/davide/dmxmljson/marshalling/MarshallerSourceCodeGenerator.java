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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;

import bz.davide.dmxmljson.CommonSourceCodeGenerator;
import bz.davide.dmxmljson.FieldOrMethod;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class MarshallerSourceCodeGenerator extends CommonSourceCodeGenerator
{

   public MarshallerSourceCodeGenerator(String[] args) throws ClassNotFoundException, IOException
   {
      super(args, Marshaller.class.getName());
   }

   public static void main(String[] args) throws ClassNotFoundException, IOException
   {
      new MarshallerSourceCodeGenerator(args);
   }

   @Override
   protected void writeClass(Class clazz, PrintWriter out) throws FileNotFoundException, IOException
   {

      out.println("      this.putClassMarshaller(\"" +
                  clazz.getName() +
                  "\", new bz.davide.dmxmljson.marshalling.ClassMarshaller() {");
      out.println("         @Override public void marshall(Object obj, String compileTimeClassName, bz.davide.dmxmljson.marshalling.Structure structure, java.util.IdentityHashMap<Object, bz.davide.dmxmljson.marshalling.Structure> identities, long[] seq, boolean superClass) throws Exception {");
      out.println("            if (!superClass) {");
      out.println("               if (isReference(structure, obj, identities, seq))");
      out.println("                  return;");

      out.println("               identities.put(obj, structure);");
      out.println("               structure.open(shortName(compileTimeClassName), shortName(obj.getClass().getName()), null);");
      out.println("            }");
      if (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals("java.lang.Object"))
      {
         out.println("            internalMarschall(obj, \"" +
                     clazz.getSuperclass().getName() +
                     "\", \"N/A\",structure, identities, seq, true);");
         this.addClassToList(clazz.getSuperclass());
      }
      out.println("            Object value;");
      ArrayList<FieldOrMethod> fom = new ArrayList<FieldOrMethod>();
      FieldOrMethod.allFieldOrMethods(clazz, this.type, fom);
      for (int i = 0; i < fom.size(); i++)
      {
         FieldOrMethod f = fom.get(i);
         out.println("            // " + f.name);
         out.println("            value = ((" + clazz.getName() + ")obj)." + f.name + ";");
         out.println("            if (value == null)");
         out.println("               structure.property(\"" + f.name + "\").nullValue();");
         out.println("            else");
         out.println("            {");
         if (f.type == String.class)
         {
            out.println("                    structure.property(\"" +
                        f.name +
                        "\").string((String)value);                          ");
         }
         else if (f.type == Integer.class || f.type == Integer.TYPE)
         {
            out.println("                    structure.property(\"" +
                        f.name +
                        "\").integer((Integer)value);                          ");
         }
         else if (f.type == Long.class || f.type == Long.TYPE)
         {
            out.println("                    structure.property(\"" +
                        f.name +
                        "\").integer((Long)value);                          ");
         }
         else if (f.type == Double.TYPE)
         {
            out.println("                    structure.property(\"" +
                        f.name +
                        "\").decimal((Double)value);                          ");
         }
         else if (f.type == Boolean.TYPE)
         {
            out.println("                    structure.property(\"" +
                        f.name +
                        "\").booleanValue((Boolean)value);                          ");
         }
         else if (f.type.isArray())
         {
            Class componentType = f.type.getComponentType();
            out.println("               " +
                        componentType.getName() +
                        "[] rawarray = (" +
                        componentType.getName() +
                        "[])value;                        ");
            out.println("               bz.davide.dmxmljson.marshalling.Array array = structure.property(\"" +
                        f.name +
                        "\").array(rawarray.length);        ");
            out.println("               for (Object o: rawarray) {                                    ");
            out.println("                  if (o == null)                                              ");
            out.println("                     array.item().nullValue();                                ");
            if (componentType == String.class)
            {
               out.println("                  else   array.item().string((String)o);                          ");
            }
            else if (componentType == Double.class)
            {
               out.println("                  else   array.item().decimal((Double)o); ");
            }
            else if (componentType == Integer.class || componentType == Integer.TYPE)
            {
               out.println("                  else   array.item().integer((Integer)o); ");
            }
            else
            {
               this.addClassToList(componentType);
               out.println("                     internalMarschall(o, o.getClass().getName(), \"" +
                           componentType.getName() +
                           "\", array.item().structure(), identities, seq, false);");
            }
            out.println("               }                                                              ");
         }
         else if (f.type == ArrayList.class)
         {
            out.println("               java.util.ArrayList arrayList = (java.util.ArrayList)value;                        ");
            out.println("               bz.davide.dmxmljson.marshalling.Array array = structure.property(\"" +
                        f.name +
                        "\").array(arrayList.size());        ");
            out.println("               for (Object o: arrayList) {                                    ");
            out.println("                  if (o == null)                                              ");
            out.println("                     array.item().nullValue();                                ");
            out.println("                  else                                                        ");

            /**
             * @author Davide Montesin <d@vide.bz>
             */
            Class componentType = (Class) ((ParameterizedType) f.genericType).getActualTypeArguments()[0];
            if (componentType == String.class)
            {
               out.println("                     array.item().string((String)o);                          ");
            }
            else if (componentType == Integer.class)
            {
               out.println("                     array.item().integer((Integer)o);                          ");
            }
            else
            {
               this.addClassToList(componentType);
               out.println("                     internalMarschall(o, o.getClass().getName(), \"" +
                           componentType.getName() +
                           "\", array.item().structure(), identities, seq, false);");
            }
            out.println("               }                                                              ");

         }
         else if (f.type == HashMap.class)
         {
            out.println("               // Hashmap");
            out.println("               java.util.HashMap hashMap = (java.util.HashMap)value;                        ");
            out.println("               bz.davide.dmxmljson.marshalling.Array array = structure.property(\"" +
                        f.name +
                        "\").array(hashMap.size());        ");
            out.println("               java.util.ArrayList keySelList = new java.util.ArrayList(hashMap.keySet());");
            out.println("               java.util.Collections.sort(keySelList);");
            out.println("               for (Object key: keySelList) {");
            out.println("                  bz.davide.dmxmljson.marshalling.Array item = array.item().array(2); ");


            Class keyType = (Class) ((ParameterizedType) f.genericType).getActualTypeArguments()[0];
            if (keyType == Integer.class)
            {
               out.println("                  item.item().integer((Integer)key);");
            }
            else if (keyType == String.class)
            {
               out.println("                  item.item().string((String)key);");
            }
            Class componentType = (Class) ((ParameterizedType) f.genericType).getActualTypeArguments()[1];
            if (componentType == String.class)
            {
               out.println("                  item.item().string((String)hashMap.get(key));");
            }
            else
            {
               this.addClassToList(componentType);
               out.println("                  Object o = hashMap.get(key);");
               out.println("                  internalMarschall(o, o.getClass().getName(), \"" +
                           componentType.getName() +
                           "\", item.item().structure(), identities, seq, false);");
            }
            out.println("               }");

         }
         else
         {
            this.addClassToList(f.type);
            out.println("                     internalMarschall(value, value.getClass().getName(),\"" +
                        f.type.getName() +
                        "\", structure.property(\"" +
                        f.name +
                        "\").structure(), identities, seq, false);");
         }
         out.println("            }");

      }
      out.println("            if (!superClass)");
      out.println("               structure.close();");

      out.println("         }");
      out.println("      });");

   }
}
