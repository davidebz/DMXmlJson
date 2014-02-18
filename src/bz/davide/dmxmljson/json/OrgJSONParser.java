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

package bz.davide.dmxmljson.json;

import java.io.IOException;

import org.json.JSONObject;

import bz.davide.dmxmljson.unmarshalling.IOUtil;
import bz.davide.dmxmljson.unmarshalling.Structure;
import bz.davide.dmxmljson.unmarshalling.json.JSONParser;
import bz.davide.dmxmljson.unmarshalling.json.org.JSONOrgStructure;

public class OrgJSONParser implements JSONParser
{
   @Override
   public Structure parse(String json) throws IOException
   {
      try
      {
         JSONObject jsonObject = new JSONObject(json);
         JSONOrgStructure structure = new JSONOrgStructure(jsonObject);
         return structure;
      }
      catch (Exception exxx)
      {
         throw IOUtil.wrapIntoIOException(exxx);
      }
   }

}
