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

/**
 * @author Davide Montesin <d@vide.bz>
 */
public class LazyAssociation<T>
{
   String      id;
   transient T reference;

   public LazyAssociation(T reference)
   {
      this.reference = reference;
      this.id = String.valueOf(System.currentTimeMillis());
   }
}
