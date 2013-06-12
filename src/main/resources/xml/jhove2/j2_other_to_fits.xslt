<?xml version="1.0" ?>
<xsl:stylesheet version="2.0"
	xmlns="http://hul.harvard.edu/ois/xml/ns/fits/fits_output"
	xmlns:fits_XsltFunctions="edu.harvard.hul.ois.fits.tools.utils.XsltFunctions"
	xmlns:j2="http://jhove2.org/xsd/1.0.0" xmlns:mix="http://www.loc.gov/mix/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="./j2_common_to_fits.xslt" />

	<xsl:template match="/">

		<fits xmlns="http://hul.harvard.edu/ois/xml/ns/fits/fits_output">

			<xsl:apply-imports />

		</fits>
	</xsl:template>
</xsl:stylesheet>
