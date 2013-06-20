/*
 * Copyright 2009 Harvard University Library This file is part of FITS (File
 * Information Tool Set). FITS is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. FITS is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. You should have received a
 * copy of the GNU Lesser General Public License along with FITS. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package edu.harvard.hul.ois.fits.junit;

/*
 * BROKEN TEST
 */
// @RunWith(value=Parameterized.class)
public class FitsOutputXmlTest /* extends XMLTestCase */
{
    /*
     * private FitsOutput expected; private FitsOutput actual; private
     * XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
     * public FitsOutputXmlTest(FitsOutput expected, FitsOutput value) {
     * this.expected = expected; this.actual = value; }
     * @SuppressWarnings({ "rawtypes", "unchecked" })
     * @Parameters public static Collection data() throws Exception { Fits fits
     * = new Fits(""); SAXBuilder builder = new SAXBuilder(); List inputs = new
     * ArrayList<String[][]>(); File inputDir =new File("tests/input"); File
     * outputDir =new File("tests/output"); for(File input :
     * inputDir.listFiles()) { //skip directories if(input.isDirectory()) {
     * continue; } FitsOutput fitsOut = fits.examine(input); File outputFile =
     * new File(outputDir + File.separator + input.getName()+".xml");
     * if(!outputFile.exists()) {
     * System.err.println("Not Found: "+outputFile.getPath()); continue; }
     * Document expectedXml = builder.build(new FileInputStream(outputFile));
     * FitsOutput expectedFits = new FitsOutput(expectedXml); FitsOutput[][] tmp
     * = new FitsOutput[][]{{expectedFits,fitsOut}}; inputs.add(tmp[0]); }
     * return inputs; }
     */
    /*
     * @Test public void testEquality() throws Exception { //convert FitsOutput
     * xml doms to strings for the diff StringWriter sw = new StringWriter();
     * Document expectedDom = expected.getFitsXml(); Document actualDom =
     * actual.getFitsXml(); serializer.output(actualDom, sw); String actualStr =
     * sw.toString(); sw = new StringWriter(); serializer.output(expectedDom,
     * sw); String expectedStr = sw.toString(); //get the file name in case of a
     * failure FitsMetadataElement item = actual.getMetadataElement("filename");
     * DifferenceListener myDifferenceListener = new
     * IgnoreAttributeValuesDifferenceListener(); Diff diff = new
     * Diff(expectedStr,actualStr);
     * diff.overrideDifferenceListener(myDifferenceListener);
     * assertXMLEqual("Error comparing: "+item.getValue(),diff,true); }
     */
}
