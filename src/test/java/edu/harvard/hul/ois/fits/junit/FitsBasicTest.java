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

import java.io.File;
import java.nio.file.Paths;

import org.custommonkey.xmlunit.XMLTestCase;
import org.junit.Test;

import edu.harvard.hul.ois.fits.Fits;
import edu.harvard.hul.ois.fits.FitsOutput;
import edu.harvard.hul.ois.fits.tools.Tool;


public class FitsBasicTest extends XMLTestCase {

    
	@Test
	public void testFits() throws Exception {	
    	Fits fits = new Fits(Paths.get("target/classes/").toAbsolutePath());
    	File jp2file = Paths.get(this.getClass().getResource("/testfiles/test.jp2").toURI()).toFile();
    	File wavfile = Paths.get(this.getClass().getResource("/testfiles/test.wav").toURI()).toFile();
    	File txtfile = Paths.get(this.getClass().getResource("/testfiles/utf16.txt").toURI()).toFile();
    	
    	for(Tool t : fits.getToolbelt().getTools()) {
    		if(t.getToolInfo().getName().equals("Jhove")) {
    			//t.setEnabled(false);
    		}
    		if(t.getToolInfo().getName().equals("Exiftool")) {
    			//t.setEnabled(false);
    		}
    	}
    	
    	FitsOutput fitsOutput = fits.examine(jp2file);
    	//fits.examine(wavfile);
    	//fits.examine(txtfile);
    	
    	//fitsOut.
    	fitsOutput.saveToDisk("fitsBasicTestOutput.xml");
    	
	}

}
