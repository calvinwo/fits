/* 
 * Copyright 2009 Harvard University Library
 * 
 * This file is part of FITS (File Information Tool Set).
 * 
 * FITS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * FITS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with FITS.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.harvard.hul.ois.fits.junit;


public class DocMDTest /*extends XMLTestCase*/ {

    
	/*@Test
	public void testDocMD() throws Exception {	
    	Fits fits = new Fits();
    	File input = new File("src/test/resources/testfiles/test.ods");
    	
    	
    	FitsOutput fitsOut = fits.examine(input);
    	
		XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
		serializer.output(fitsOut.getFitsXml(), System.out);
		
		DocumentMD docmd = (DocumentMD)fitsOut.getStandardXmlContent();
		
		if(docmd != null) {
		docmd.setRoot(true);
			XMLOutputFactory xmlof = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = xmlof.createXMLStreamWriter(System.out); 
			
			docmd.output(writer);
		}
    	
	}*/

}
