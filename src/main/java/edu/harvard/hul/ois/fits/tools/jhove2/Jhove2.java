/*
 * Copyright 2011 Goettingen State and University Library This file is part of
 * FITS (File Information Tool Set). FITS is free software: you can redistribute
 * it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. FITS is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with FITS. If
 * not, see <http://www.gnu.org/licenses/>.
 */
package edu.harvard.hul.ois.fits.tools.jhove2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jhove2.config.spring.SpringConfigInfo;
import org.jhove2.core.I8R;
import org.jhove2.core.JHOVE2;
import org.jhove2.core.JHOVE2Exception;
import org.jhove2.core.format.FormatIdentification;
import org.jhove2.core.format.FormatIdentification.Confidence;
import org.jhove2.core.io.Input;
import org.jhove2.core.source.Source;
import org.jhove2.core.source.SourceFactoryUtil;
import org.jhove2.module.display.Displayer;
import org.jhove2.persist.PersistenceManager;
import org.jhove2.persist.PersistenceManagerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

import edu.harvard.hul.ois.fits.Fits;
import edu.harvard.hul.ois.fits.exceptions.FitsException;
import edu.harvard.hul.ois.fits.exceptions.FitsToolException;
import edu.harvard.hul.ois.fits.identity.ExternalIdentifier;
import edu.harvard.hul.ois.fits.identity.ToolIdentity;
import edu.harvard.hul.ois.fits.tools.ToolBase;
import edu.harvard.hul.ois.fits.tools.ToolInfo;
import edu.harvard.hul.ois.fits.tools.ToolOutput;
import edu.harvard.hul.ois.fits.tools.droid.Droid;
import edu.harvard.hul.ois.fits.tools.utils.XsltTransformMap;

/** <p>
 * The JHOVE2 integration class.
 * </p>
 * 
 * @author Stefan E. Funk, SUB Göttingen
 * @version 2011-04-15
 * @since 2011-03-09 */
public class Jhove2 extends ToolBase
{
    private JHOVE2              j2;
    private Map<String, String> formatMap;
    private boolean             enabled             = true;
    private final static String JHOVE2_NAME         = "JHOVE2";
    private final static String JHOVE2_CLASS        = "JHOVE2";
    private final static String JHOVE2_XSLT_MAP     = "j2_xslt_map.xml";
    private final static String JHOVE2_DEFAULT_XSLT = "j2_common_to_fits.xslt";
    public final static String  j2FitsConfig        = Fits.FITS_XML + "jhove2" + File.separator;
    private final static Logger log                 = LoggerFactory
                                                            .getLogger("edu.harvard.hul.ois.fits.tools.jhove2");

    /** <p>
     * Ths constructor. Initializes the JHOVE2 and gets the format map for PUID
     * mapping to JHOVE2 IDs.
     * </p>
     * 
     * @throws FitsException */
    public Jhove2 () throws FitsException
    {
        log.debug("Entering Jhove2()");
        PersistenceManager persistenceManager = null;
        // Initialize Jhove2.
        try
        {
            // Put all the JHOVE IDs into a format map.
            SpringConfigInfo configInfo = new SpringConfigInfo();
            PersistenceManagerUtil.createPersistenceManagerFactory(configInfo);
            persistenceManager = PersistenceManagerUtil.getPersistenceManagerFactory()
                    .getInstance();
            persistenceManager.initialize();
            this.formatMap = configInfo.getFormatAliasIdsToJ2Ids(I8R.Namespace.PUID);
            if (log.isTraceEnabled())
            {
                printFormatMap_();
            }
            this.j2 = SpringConfigInfo.getReportable(JHOVE2.class, JHOVE2_CLASS);
        }
        catch (JHOVE2Exception e)
        {
            throw new FitsToolException("Error initializing " + JHOVE2_NAME, e);
        }
        // Initialize tool info and transform map.
        this.info = new ToolInfo(JHOVE2_NAME, this.j2.getVersion(), this.j2.getReleaseDate());
        this.transformMap = XsltTransformMap.getMap(j2FitsConfig + JHOVE2_XSLT_MAP);
        if (log.isTraceEnabled())
        {
            printTransformMap_();
        }
        log.debug("Exiting Jhove2()");
    }

    /** {@inheritDoc} */
    public ToolOutput extractInfo (File file) throws FitsToolException
    {
        log.debug("Entering ToolOutput(file={})", file);
        try
        {
            // Activate the DROID to get format infos.
            // TODO Get that information from the FITS' DROID run, do not call
            // the DROID twice!!
            // TODO Handle DROID information of files inside e.g. a ZIP
            // container. How to get those file information?? Maybe JHOVE should
            // handle that matter anyway??
            Droid droid = new Droid();
            ToolOutput to = droid.extractInfo(file);
            // Get the DROIDS PUID format name.
            // TODO Could there be more than one PUID for one format?? If wo,
            // implement as a list!!
            String droidFormatName = extractDroidFormatInfo(to.getFileIdentity());
            String jhoveFormatName = this.formatMap.get(droidFormatName);
            // System.out.println("JHOVE2 FORMAT NAME: " + jhoveFormatName);
            // Exit with null documents, if no JHOVE2 ID is existing.
            // TODO Check if that is correct here!!
            if (jhoveFormatName == null || jhoveFormatName.isEmpty())
            {
                log.debug("Exiting extractInfo(): null");
                return new ToolOutput(this, null, null);
            }
            // Create I8R format and identification from JHOVE2 format mapping.
            I8R format = new I8R(jhoveFormatName, I8R.Namespace.JHOVE2);
            // TODO Why take PositiveGeneric here??
            FormatIdentification id = new FormatIdentification(format, Confidence.PositiveGeneric);
            // Generate source object from given file, and add format.
            Source source = SourceFactoryUtil.getSource(j2, file);
            source.addPresumptiveFormat(id);
            Input input = source.getInput(j2);
            // Run JHOVE2.
            try
            {
                this.j2.characterize(source, input);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                // FIXME Why I do only get a JHOVE2 result if using ZIP??
                // TODO Check Exeption:
                // java.lang.IllegalArgumentException: Null charset name
                // at java.nio.charset.Charset.lookup(Charset.java:429)
                // at java.nio.charset.Charset.forName(Charset.java:502)
                // at
                // org.jhove2.module.format.xml.NumericCharacterReferences.parse(NumericCharacterReferences.java:137)
                // at
                // org.jhove2.module.format.xml.XmlModule.parse(XmlModule.java:463)
                // at
                // org.jhove2.module.format.BaseFormatModule.invoke(BaseFormatModule.java:155)
                // at
                // org.jhove2.module.format.DispatcherCommand.execute(DispatcherCommand.java:164)
                // at org.jhove2.core.JHOVE2.characterize(JHOVE2.java:168)
                // at
                // edu.harvard.hul.ois.fits.tools.jhove2.Jhove2.extractInfo(Jhove2.java:176)
                // at edu.harvard.hul.ois.fits.Fits.examine(Fits.java:271)
                // at edu.harvard.hul.ois.fits.Fits.main(Fits.java:160)
            }
            // TODO REMOVE DEBUG OUTPUT!!
            // System.out.println("DISPLAY TEXT:");
            // Displayer debugDisplayer = SpringConfigInfo.getReportable(
            // Displayer.class, "Text");
            // debugDisplayer.display(source);
            // TODO REMOVE DEBUG OUTPUT!!
            // Get the XML representation of the JHOVE2 results.
            // TODO This possibly could be done very much smarter??
            Displayer displayer = SpringConfigInfo.getReportable(Displayer.class, "XML");
            ByteArrayOutputStream toolOutput = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(toolOutput);
            displayer.display(source, ps);
            ByteArrayInputStream toolInput = new ByteArrayInputStream(toolOutput.toByteArray());
            Document toolDocument = this.saxBuilder.build(toolInput);
            toolInput.close();
            ps.close();
            toolOutput.close();
            // Create the FITS output.
            String xsltTransform = (String) this.transformMap.get(jhoveFormatName);
            // System.out.println(jhoveFormatName);
            // System.out.println("XSLT TRANSFORM: " + xsltTransform);
            Document fitsDocument = null;
            if (xsltTransform != null && !xsltTransform.equals(""))
            {
                fitsDocument = transform(j2FitsConfig + xsltTransform, toolDocument);
            }
            else
            {
                fitsDocument = transform(j2FitsConfig + JHOVE2_DEFAULT_XSLT, toolDocument);
            }
            // TODO REMOVE DEBUG OUTPUT!!
            // System.out.println("JHOVE2 XML OUTPUT:");
            // XMLOutputter outputter = new
            // XMLOutputter(Format.getPrettyFormat());
            // try {
            // outputter.output(toolDocument, System.out);
            // } catch (IOException e) {
            // e.printStackTrace();
            // }
            // TODO REMOVE DEBUG OUTPUT!!
            // TODO REMOVE DEBUG OUTPUT!!
            // System.out.println("JHOVE2 FITS OUTPUT:");
            // XMLOutputter outputter2 = new
            // XMLOutputter(Format.getPrettyFormat());
            // try {
            // outputter2.output(fitsDocument, System.out);
            // } catch (IOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
            // TODO REMOVE DEBUG OUTPUT!!
            // And create the FITS' tool output.
            this.output = new ToolOutput(this, fitsDocument, toolDocument);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new FitsToolException("File not found: " + file.getAbsolutePath(), e);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new FitsToolException("Error initializing " + JHOVE2_NAME, e);
        }
        catch (JHOVE2Exception e)
        {
            e.printStackTrace();
            throw new FitsToolException("Error running " + JHOVE2_NAME, e);
        }
        catch (JDOMException e)
        {
            e.printStackTrace();
            throw new FitsToolException("Error creating " + JHOVE2_NAME + " output XML", e);
        }
        log.debug("Exiting extractInfo(): {}", this.output);
        return this.output;
    }

    // **
    // PRIVATE METHODS
    // **
    /** <p>
     * Exracts the PUID identifier given by the DROID.
     * </p>
     * 
     * @param theList
     * @return */
    private String extractDroidFormatInfo (List<ToolIdentity> theList)
    {
        log.trace("Entering extractDroidFormatInfo(theList={}", theList);
        String result = "";
        for (ToolIdentity fi : theList)
        {
            for (ExternalIdentifier ei : fi.getExternalIds())
            {
                result = ei.getValue();
                // TODO REMOVE DEBUG OUTPUT!!
                // System.out.println("  **  PUID: " + result);
                // System.out.println("  **  JHOVEID: "
                // + this.formatMap.get(result));
                // TODO REMOVE DEBUG OUTPUT!!
            }
        }
        log.trace("Exiting extractDroidFormatInfo(): {}", result);
        return result;
    }

    /** {@inheritDoc} */
    public boolean isEnabled ()
    {
        return this.enabled;
    }

    /** {@inheritDoc} */
    public void setEnabled (boolean value)
    {
        this.enabled = value;
    }

    private void printFormatMap_ ()
    {
        for (String s : this.formatMap.keySet())
        {
            // log.trace("\t" + s + " = " + this.formatMap.get(s));
        }
    }

    private void printTransformMap_ ()
    {
        for (Object o : this.transformMap.keySet())
        {
            // log.trace("\t" + (String) o + " = " + this.transformMap.get(o));
        }
    }
}
