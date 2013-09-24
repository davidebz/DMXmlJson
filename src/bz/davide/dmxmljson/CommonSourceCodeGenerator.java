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

package bz.davide.dmxmljson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import bz.davide.dmxmljson.FieldOrMethod.Access;

public abstract class CommonSourceCodeGenerator
{
   protected String                        targetProjectDir;
   protected String                        targetPackage;
   protected String                        targetClassName;
   protected String                        targetSourceFolder;
   protected Access                        type;
   protected HashMap<String, StringBuffer> generatedClassesConstructorContentByPackageName;
   protected ArrayList<Class>              allClasses;

   public CommonSourceCodeGenerator(String args[], String rootClass) throws ClassNotFoundException,
            IOException
   {
      Class[] otherClasses;

      // Parameters
      this.targetProjectDir = args[0];
      this.targetPackage = args[1];
      this.targetClassName = args[2];
      this.targetSourceFolder = args[3];
      this.type = Access.valueOf(args[4]);
      Class mainClass;
      mainClass = Class.forName(args[5]);
      int remainingParams = args.length - 6;
      otherClasses = new Class[remainingParams];
      for (int i = 0; i < remainingParams; i++)
      {
         otherClasses[i] = Class.forName(args[6 + i]);

      }

      // Code
      this.allClasses = new ArrayList<Class>();
      this.allClasses.add(mainClass);
      this.allClasses.addAll(Arrays.asList(otherClasses));

      this.generatedClassesConstructorContentByPackageName = new HashMap<String, StringBuffer>();

      for (int i = 0; i < this.allClasses.size(); i++)
      {
         Class clazz = this.allClasses.get(i);
         StringWriter sw = new StringWriter();
         PrintWriter out = new PrintWriter(sw);
         this.writeClass(clazz, out);
         String packageName = clazz.getPackage().getName();
         StringBuffer code = this.generatedClassesConstructorContentByPackageName.get(packageName);
         if (code == null)
         {
            code = new StringBuffer();
            this.generatedClassesConstructorContentByPackageName.put(packageName, code);
         }
         code.append(sw.getBuffer().toString());
         out.close();
      }
      File srcFolderFile = new File(new File(this.targetProjectDir), this.targetSourceFolder);

      String lastSuperClass = rootClass;

      for (String packageName : this.generatedClassesConstructorContentByPackageName.keySet())
      {
         File srcPackageFile = new File(srcFolderFile, packageName.replace('.', File.separatorChar));
         srcPackageFile.mkdirs();
         File srcJavaFile = new File(srcPackageFile, this.targetClassName + "_Helper.java");
         OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(srcJavaFile));
         PrintWriter out = new PrintWriter(outputStreamWriter);

         out.println("/*********************************************************************************");
         out.println(" *                                                                               *");
         out.println(" * WARNING: File automatically generated by DMXmlJson. DON'T CHANGE IT manually! *");
         out.println(" *                                                                               *");
         out.println(" *********************************************************************************/");
         out.println();
         out.println("package " + packageName + ";");
         out.println();
         out.println();
         out.println("public class " + this.targetClassName + "_Helper extends " + lastSuperClass + "");
         out.println("{");
         out.println("   protected " + this.targetClassName + "_Helper()");
         out.println("   {");
         out.println(this.generatedClassesConstructorContentByPackageName.get(packageName).toString());
         out.println("   }");
         out.println("}");
         out.close();

         lastSuperClass = packageName + "." + this.targetClassName + "_Helper";

      }

      // now the main class
      File srcPackageFile = new File(srcFolderFile, this.targetPackage.replace('.', File.separatorChar));
      srcPackageFile.mkdirs();
      File srcJavaFile = new File(srcPackageFile, this.targetClassName + ".java");
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(srcJavaFile));
      PrintWriter out = new PrintWriter(outputStreamWriter);
      out.println("package " + this.targetPackage + ";");
      out.println();
      out.println();
      out.println("public class " + this.targetClassName + " extends " + lastSuperClass);
      out.println("{");
      out.println("}");

      out.close();

   }

   protected void addClassToList(Class c)
   {
      if (c == Object.class)
      {
         return;
      }
      if (c.isInterface())
      {
         return;
      }
      if (!this.allClasses.contains(c))
      {
         this.allClasses.add(c);
      }
   }

   protected abstract void writeClass(Class clazz, PrintWriter out) throws FileNotFoundException, IOException;
}
