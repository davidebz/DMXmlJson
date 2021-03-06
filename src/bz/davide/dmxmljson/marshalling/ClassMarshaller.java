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

package bz.davide.dmxmljson.marshalling;

import java.util.IdentityHashMap;

public abstract class ClassMarshaller
{
   public abstract void marshall(Object obj,
                                 String compileTimeClassName,
                                 Structure structure,
                                 IdentityHashMap<Object, bz.davide.dmxmljson.marshalling.Structure> identities,
                                 long[] seq,
                                 boolean superClass) throws Exception;
}
