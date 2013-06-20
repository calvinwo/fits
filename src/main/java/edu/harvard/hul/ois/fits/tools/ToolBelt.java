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
package edu.harvard.hul.ois.fits.tools;

import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.harvard.hul.ois.fits.Fits;
import edu.harvard.hul.ois.fits.exceptions.FitsConfigurationException;

public class ToolBelt
{
    private List<Tool>          tools;
    private final static Logger log = LoggerFactory.getLogger("edu.harvard.hul.ois.fits.tools");

    public ToolBelt (String configFile) throws FitsConfigurationException
    {
        log.debug("Entering ToolBelt(configFile={})", configFile);
        XMLConfiguration config = null;
        try
        {
            URL url = this.getClass().getResource(configFile);
            config = new XMLConfiguration(url);
        }
        catch (ConfigurationException e)
        {
            throw new FitsConfigurationException("Error reading " + configFile, e);
        }
        tools = new ArrayList<Tool>();
        // get number of tools
        int size = config.getList("tools.tool[@class]").size();
        // for each tools get the class path and any excluded extensions
        for (int i = 0; i < size; i++)
        {
            String tClass = config.getString("tools.tool(" + i + ")[@class]");
            @SuppressWarnings("unchecked")
            List<String> excludes = config.getList("tools.tool(" + i + ")[@exclude-exts]");
            @SuppressWarnings("unchecked")
            List<String> includes = config.getList("tools.tool(" + i + ")[@include-exts]");
            Tool t = null;
            try
            {
                @SuppressWarnings("rawtypes")
                Class c = Class.forName(tClass);
                t = (Tool) c.newInstance();
            }
            catch (Exception e)
            {
                throw new FitsConfigurationException("Error initializing " + tClass, e);
            }
            if (t != null)
            {
                for (String ext : excludes)
                {
                    t.addExcludedExtension(ext);
                }
                for (String ext : includes)
                {
                    t.addIncludedExtension(ext);
                }
                tools.add(t);
            }
        }
        log.debug("Exiting ToolBelt()");
    }

    public List<Tool> getTools ()
    {
        return tools;
    }

    public void printToolInfo ()
    {
        printToolInfo(false, System.out);
    }

    public void printToolInfo (boolean sysInfo, PrintStream p)
    {
        if (sysInfo)
        {
            // system info
            p.println("OS Name = " + System.getProperty("os.name"));
            p.println("OS Arch = " + System.getProperty("os.arch"));
            p.println("OS Version = " + System.getProperty("os.version"));
            p.println("------------------------------------");
        }
        for (Tool t : tools)
        {
            p.print(t.getToolInfo().print());
        }
    }
}
