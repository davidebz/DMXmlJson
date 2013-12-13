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

package bz.davide.dmxmljson.marshalling.json;

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class JSONModelString extends JSONModelValue
{
   String value;

   public JSONModelString(String value)
   {
      super();
      this.value = value;
   }

   @Override
   public void toJSON(StringBuffer buffer, int indent, int INDENTNR)
   {
      buffer.append("\"");
      String escapedString = escapeString(this.value);
      buffer.append(escapedString);
      buffer.append("\"");
   }

   static String escapeString(String in)
   {
      // http://stackoverflow.com/questions/1466959/string-replaceall-vs-matcher-replaceall-performance-differences
      // Matcher m = Pattern.compile("\n").matcher("");
      // repeat only:
      // m.reset(s).replaceAll(repl)

      String escapedString = in;
      escapedString = escapedString.replaceAll("\n", "\\\\n");
      escapedString = escapedString.replaceAll("\"", "\\\\\"");
      return escapedString;
   }
}
