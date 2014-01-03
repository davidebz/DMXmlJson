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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;

import bz.davide.dmxmljson.CommonSourceCodeGenerator;
import bz.davide.dmxmljson.FieldOrMethod;
import bz.davide.dmxmljson.FieldOrMethod.Access;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class UnmarshallerSourceCodeGenerator extends CommonSourceCodeGenerator
{
   public UnmarshallerSourceCodeGenerator(String[] args) throws ClassNotFoundException, IOException
   {
      super(args, Unmarshaller.class.getName());
   }

   public static void main(String[] args) throws ClassNotFoundException, IOException
   {
      new UnmarshallerSourceCodeGenerator(args);
   }

   @Override
   protected void writeClass(Class clazz, PrintWriter out) throws FileNotFoundException, IOException
   {

      out.println("      this.putInstanceFactory(\"" +
                  clazz.getName() +
                  "\", new bz.davide.dmxmljson.unmarshalling.InstanceFactory() {");
      out.println("         @Override public Object newInstance() throws Exception {");
      if (Modifier.isAbstract(clazz.getModifiers()))
      {
         out.println("            return null;");
      }
      else
      {
         if (this.type == Access.FIELD) // When using field access a special constructor is used, to avoid "regular" code to run!
         {
            out.println("            return new " + clazz.getName() + "();");
         }
         else
         {
            out.println("            return new " + clazz.getName() + "();");
         }

      }
      out.println("         }");
      out.println("      });");
      out.println();
      out.println("      this.putClassUnmarshaller(\"" +
                  clazz.getName() +
                  "\", new bz.davide.dmxmljson.unmarshalling.ClassUnmarshaller() {");
      out.println("         @Override public void unmarshall(bz.davide.dmxmljson.unmarshalling.Structure structure, Object obj, java.util.HashMap<String, Object> identities) throws Exception {");
      if (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals("java.lang.Object"))
      {
         out.println("            internalUnmarschall(structure, \"" +
                     clazz.getSuperclass().getName() +
                     "\", obj, identities);");
         this.addClassToList(clazz.getSuperclass());
      }
      out.println("            String id = structure.getId();");
      out.println("            if (id != null)");
      out.println("               identities.put(id, obj);");
      out.println("            bz.davide.dmxmljson.unmarshalling.Value value;");
      ArrayList<FieldOrMethod> fom = new ArrayList<FieldOrMethod>();
      FieldOrMethod.allFieldOrMethods(clazz, this.type, fom);
      for (int i = 0; i < fom.size(); i++)
      {
         FieldOrMethod f = fom.get(i);
         out.println("            // " + f.name);
         out.println("            if ((value = structure.property(\"" + f.name + "\")) != null)");
         out.println("               if (value.isNull())");
         if (f.type.isPrimitive())
         {
            out.println("                  new RuntimeException(\"Impossibile value for primitive type\");");
         }
         else
         {
            out.println("                  ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("null") +
                        ";");
         }
         out.println("               else");
         out.println("               {");
         if (f.type == String.class)
         {
            out.println("                  ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("value.string()") +
                        ";");
         }
         else if (f.type == Integer.class || f.type == Integer.TYPE)
         {
            out.println("                  ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("(int)value.integer()") +
                        ";");
         }
         else if (f.type == Long.class || f.type == Long.TYPE)
         {
            out.println("                  ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("value.integer()") +
                        ";");
         }
         else if (f.type == Double.TYPE)
         {
            out.println("                  ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("value.decimal()") +
                        ";");
         }
         else if (f.type == Boolean.TYPE)
         {
            out.println("                  ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("value.booleanValue()") +
                        ";");
         }
         else if (f.type.isEnum())
         {

         }
         else if (f.type.isArray() || f.type == ArrayList.class)
         {
            out.println("                  bz.davide.dmxmljson.unmarshalling.Array arr = value.array();        ");
            out.println("                  java.util.ArrayList arrayList = new java.util.ArrayList();       ");
            out.println("                  while ((value = arr.nextItem()) != null) {                       ");
            out.println("                     if (value.isNull())                                           ");
            out.println("                        arrayList.add(null);                                       ");
            out.println("                     else                                                          ");

            Class componentType;
            if (f.type.isArray())
            {
               componentType = f.type.getComponentType();
            }
            else
            {
               componentType = (Class) ((ParameterizedType) f.genericType).getActualTypeArguments()[0];
            }
            if (componentType == String.class)
            {
               out.println("                        arrayList.add(value.string());");
            }
            else if (componentType == Double.class)
            {
               out.println("                        arrayList.add(value.decimal());");
            }
            else if (componentType == Integer.class || componentType == Integer.TYPE)
            {
               out.println("                        arrayList.add((int)value.integer());");
            }
            else
            {
               this.addClassToList(componentType);

               out.println("                     {                                                                   ");
               out.println("                        bz.davide.dmxmljson.unmarshalling.Structure tmpStructure = value.structure();");
               out.println("                        String refid = tmpStructure.getRefId();    ");
               out.println("                        if (refid != null)                              ");
               out.println("                           arrayList.add(identities.get(refid));                                                ");
               out.println("                        else {");

               out.println("                           Object o = newInstance(tmpStructure.getRuntimeClassName(\"" +
                           componentType.getSimpleName() +
                           "\"));              ");
               out.println("                           internalUnmarschall(tmpStructure, o.getClass().getName(), o, identities); ");
               out.println("                           arrayList.add(o);                                                ");
               out.println("                        }");
               out.println("                     }                                                                   ");
            }
            out.println("                  }                                                                   ");
            if (f.type.isArray())
            {
               out.println("                  ((" +
                           clazz.getSimpleName() +
                           ")obj)." +
                           f.writeSetCode("(" +
                                          componentType.getName() +
                                          "[])arrayList.toArray(new " +
                                          componentType.getName() +
                                          "[0])") +
                           ";");
            }
            else
            {
               out.println("                  ((" +
                           clazz.getSimpleName() +
                           ")obj)." +
                           f.writeSetCode("arrayList") +
                           ";");
            }
         }
         else if (f.type == HashMap.class)
         {
            out.println("                  //hashmap");
            out.println("                  bz.davide.dmxmljson.unmarshalling.Array arr = value.array();        ");
            out.println("                  java.util.HashMap hashMap = new java.util.HashMap();       ");
            out.println("                  while ((value = arr.nextItem()) != null) {                       ");
            out.println("                     bz.davide.dmxmljson.unmarshalling.Array item = value.array();        ");


            Class keyType = (Class) ((ParameterizedType) f.genericType).getActualTypeArguments()[0];
            if (keyType == Integer.class)
            {
               out.println("                     Object key = (int)item.nextItem().integer();");
            }
            else
            {
               out.println("                     Object key = item.nextItem().string();");
            }
            Class componentType = (Class) ((ParameterizedType) f.genericType).getActualTypeArguments()[1];
            if (componentType == String.class)
            {
               out.println("                     hashMap.put(key,item.nextItem().string());");
            }
            else
            {
               this.addClassToList(componentType);
               out.println("                     value = item.nextItem();");
               out.println("                     Object o = newInstance(value.structure().getRuntimeClassName(\"" +
                           componentType.getSimpleName() +
                           "\"));              ");
               out.println("                     internalUnmarschall(value.structure(), o.getClass().getName(), o, identities); ");
               out.println("                     hashMap.put(key,o);");
            }
            out.println("                  }                       ");
            out.println("                  ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("hashMap") +
                        ";");
         }
         else
         {
            this.addClassToList(f.type);
            out.println("                  String refid = value.structure().getRefId();    ");
            out.println("                  if (refid != null)                              ");
            out.println("                     ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("(" + f.type.getName() + ")identities.get(refid)") +
                        ";");
            out.println("                  else {");
            out.println("                     Object o = newInstance(value.structure().getRuntimeClassName(\"" +
                        f.type.getSimpleName() +
                        "\"));              ");
            out.println("                     internalUnmarschall(value.structure(), o.getClass().getName(), o, identities); ");
            out.println("                     ((" +
                        clazz.getSimpleName() +
                        ")obj)." +
                        f.writeSetCode("(" + f.type.getName() + ")o") +
                        ";");
            out.println("                  }");
         }
         out.println("               }");

      }

      out.println("         }");
      out.println("      });");

      out.close();

   }
}
