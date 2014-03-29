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

      ArrayList<FieldOrMethod> fom = new ArrayList<FieldOrMethod>();
      FieldOrMethod.allFieldOrMethods(clazz, this.type, fom);

      out.println("      this.emptyObjectCheck.put(\""
                  + clazz.getName()
                  + "\", new bz.davide.dmxmljson.unmarshalling.EmptyFieldChecker<"
                  + clazz.getCanonicalName()
                  + ">() {");
      out.println("         @Override public void check(" + clazz.getCanonicalName() + "  ret){");
      if (this.type == Access.FIELD)
      {
         for (int i = 0; i < fom.size(); i++)
         {
            FieldOrMethod f = fom.get(i);
            out.println("            // " + f.name);
            if (f.type == Long.TYPE || f.type == Integer.TYPE || f.type == Double.TYPE)
            {
               out.println("            if (ret." + f.name + " != 0)");
            }
            else if (f.type == Boolean.TYPE)
            {
               out.println("            if (ret." + f.name + " != false)");
            }
            else
            {
               out.println("            if (ret." + f.name + " != null)");
            }
            out.println("               throw new RuntimeException(\"The constructor initialized the field "
                        + clazz.getName()
                        + "."
                        + f.name
                        + "\");");
         }
         Class superClass = clazz.getSuperclass();
         if (superClass != null && !(superClass == Object.class))
         {
            out.println("            emptyObjectCheck.get(\"" + superClass.getName() + "\").check(ret);");
         }

      }
      out.println("         }");
      out.println("      });");

      out.println("      this.putInstanceFactory(\""
                  + clazz.getName()
                  + "\", new bz.davide.dmxmljson.unmarshalling.InstanceFactory() {");
      out.println("         @Override public Object newInstance() throws Exception {");
      if (Modifier.isAbstract(clazz.getModifiers()))
      {
         out.println("            return null;");
      }
      else
      {
         if (this.type == Access.FIELD)
         {
            out.println("            "
                        + clazz.getCanonicalName()
                        + " ret = new "
                        + clazz.getCanonicalName()
                        + this.defaultConstructorArguments
                        + ";");
            out.println("            emptyObjectCheck.get(\"" + clazz.getName() + "\").check(ret);");
            out.println("            return ret;");
         }
         else
         {
            out.println("            return new " + clazz.getName() + this.defaultConstructorArguments + ";");
         }

      }
      out.println("         }");
      out.println("      });");
      out.println();
      out.println("      this.putClassUnmarshaller(\""
                  + clazz.getName()
                  + "\", new bz.davide.dmxmljson.unmarshalling.ClassUnmarshaller() {");
      out.println("         @Override public void unmarshall(bz.davide.dmxmljson.unmarshalling.Structure structure, Object obj, java.util.HashMap<String, Object> identities) throws Exception {");
      if (clazz.getSuperclass() != null && !clazz.getSuperclass().getName().equals("java.lang.Object"))
      {
         out.println("            internalUnmarschall(structure, \""
                     + clazz.getSuperclass().getName()
                     + "\", obj, identities);");
         this.addClassToList(clazz.getSuperclass());
      }
      out.println("            structure.open();");
      out.println("            String id = structure.getId();");
      out.println("            if (id != null)");
      out.println("               identities.put(id, obj);");
      out.println("            bz.davide.dmxmljson.unmarshalling.Value value;");
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
            out.println("                  (("
                        + clazz.getCanonicalName()
                        + ")obj)."
                        + f.writeSetCode("null")
                        + ";");
         }
         out.println("               else");
         out.println("               {");
         if (f.type == String.class)
         {
            out.println("                  (("
                        + clazz.getCanonicalName()
                        + ")obj)."
                        + f.writeSetCode("value.string()")
                        + ";");
         }
         else if (f.type == Integer.class || f.type == Integer.TYPE)
         {
            out.println("                  (("
                        + clazz.getCanonicalName()
                        + ")obj)."
                        + f.writeSetCode("(int)value.integer()")
                        + ";");
         }
         else if (f.type == Long.class || f.type == Long.TYPE)
         {
            out.println("                  (("
                        + clazz.getSimpleName()
                        + ")obj)."
                        + f.writeSetCode("value.integer()")
                        + ";");
         }
         else if (f.type == Double.TYPE)
         {
            out.println("                  (("
                        + clazz.getSimpleName()
                        + ")obj)."
                        + f.writeSetCode("value.decimal()")
                        + ";");
         }
         else if (f.type == Boolean.TYPE)
         {
            out.println("                  (("
                        + clazz.getSimpleName()
                        + ")obj)."
                        + f.writeSetCode("value.booleanValue()")
                        + ";");
         }
         else if (f.type.isEnum())
         {

         }
         else if (f.type.isArray())
         {
            Class componentType = f.type.getComponentType();

            out.println("                  bz.davide.dmxmljson.unmarshalling.Array arr = value.array();        ");
            out.println("                  arr.open();        ");
            out.println("                  "
                        + componentType.getName()
                        + "[] arrayList = new "
                        + componentType.getName()
                        + "[arr.length()];       ");
            out.println("                  for (int i = 0; i < arrayList.length; i++) {                       ");
            out.println("                     value = arr.nextItem();                                       ");
            if (!componentType.isPrimitive())
            {
               out.println("                     if (value.isNull())                                           ");
               out.println("                        arrayList[i] = null;                                       ");
               out.println("                     else                                                          ");
            }
            if (componentType == String.class)
            {
               out.println("                        arrayList[i] = (value.string());");
            }
            else if (componentType == Double.class)
            {
               out.println("                        arrayList[i] = (value.decimal());");
            }
            else if (componentType == Integer.class || componentType == Integer.TYPE)
            {
               out.println("                        arrayList[i] = ((int)value.integer());");
            }
            else
            {
               this.addClassToList(componentType);

               out.println("                     {                                                                   ");
               out.println("                        bz.davide.dmxmljson.unmarshalling.Structure tmpStructure = value.structure();");
               out.println("                        String refid = tmpStructure.getRefId();    ");
               out.println("                        if (refid != null)                              ");
               out.println("                           arrayList[i] = ("
                           + componentType.getName()
                           + ")(identities.get(refid));                                                ");
               out.println("                        else {");

               out.println("                           Object o = newInstance(tmpStructure.getRuntimeClassName(\""
                           + componentType.getSimpleName()
                           + "\"));              ");
               out.println("                           internalUnmarschall(tmpStructure, o.getClass().getName(), o, identities); ");
               out.println("                           arrayList[i] = ("
                           + componentType.getName()
                           + ")(o);                                                ");
               out.println("                        }");
               out.println("                     }                                                                   ");
            }
            out.println("                  }                                                                   ");
            out.println("                  arr.close();        ");

            out.println("                  (("
                        + clazz.getCanonicalName()
                        + ")obj)."
                        + f.writeSetCode("arrayList")
                        + ";");

         }
         else if (f.type == ArrayList.class)
         {
            out.println("                  bz.davide.dmxmljson.unmarshalling.Array arr = value.array();        ");
            out.println("                  arr.open();        ");
            out.println("                  java.util.ArrayList arrayList = new java.util.ArrayList(arr.length());       ");
            out.println("                  while ((value = arr.nextItem()) != null) {                       ");
            out.println("                     if (value.isNull())                                           ");
            out.println("                        arrayList.add(null);                                       ");
            out.println("                     else                                                          ");

            Class componentType = (Class) ((ParameterizedType) f.genericType).getActualTypeArguments()[0];

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

               out.println("                           Object o = newInstance(tmpStructure.getRuntimeClassName(\""
                           + componentType.getSimpleName()
                           + "\"));              ");
               out.println("                           internalUnmarschall(tmpStructure, o.getClass().getName(), o, identities); ");
               out.println("                           arrayList.add(o);                                                ");
               out.println("                        }");
               out.println("                     }                                                                   ");
            }
            out.println("                  }                                                                   ");
            out.println("                  arr.close();        ");
            out.println("                  (("
                        + clazz.getCanonicalName()
                        + ")obj)."
                        + f.writeSetCode("arrayList")
                        + ";");

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
               out.println("                     Object o = newInstance(value.structure().getRuntimeClassName(\""
                           + componentType.getSimpleName()
                           + "\"));              ");
               out.println("                     internalUnmarschall(value.structure(), o.getClass().getName(), o, identities); ");
               out.println("                     hashMap.put(key,o);");
            }
            out.println("                  }                       ");

            out.println("                  (("
                        + clazz.getSimpleName()
                        + ")obj)."
                        + f.writeSetCode("hashMap")
                        + ";");
         }
         else
         {
            this.addClassToList(f.type);
            out.println("                  String refid = value.structure().getRefId();    ");
            out.println("                  if (refid != null)                              ");
            out.println("                     (("
                        + clazz.getCanonicalName()
                        + ")obj)."
                        + f.writeSetCode("(" + f.type.getName() + ")identities.get(refid)")
                        + ";");
            out.println("                  else {");
            out.println("                     Object o = newInstance(value.structure().getRuntimeClassName(\""
                        + f.type.getSimpleName()
                        + "\"));              ");
            out.println("                     internalUnmarschall(value.structure(), o.getClass().getName(), o, identities); ");
            out.println("                     (("
                        + clazz.getCanonicalName()
                        + ")obj)."
                        + f.writeSetCode("(" + f.type.getName() + ")o")
                        + ";");
            out.println("                  }");
         }
         out.println("               }");

      }
      out.println("            structure.close();");

      out.println("         }");
      out.println("      });");

      out.close();

   }
}
