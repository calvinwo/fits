<?xml version="1.0" ?>
<xsl:stylesheet version="2.0"
	xmlns="http://hul.harvard.edu/ois/xml/ns/fits/fits_output"
	xmlns:fits_XsltFunctions="edu.harvard.hul.ois.fits.tools.utils.XsltFunctions"
	xmlns:j2="http://jhove2.org/xsd/1.0.0" xmlns:mix="http://www.loc.gov/mix/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="./j2_common_to_fits.xslt" />

	<!-- TODO Not quite correct, I suppose, we do need the DRIOD for all the 
		contained files, too!! -->
	<xsl:variable name="numchildsources"
		select="/j2:jhove2/j2:feature/j2:features/j2:feature[@name='NumChildSources']/j2:value" />
	<xsl:variable name="childsources"
		select="/j2:jhove2/j2:feature/j2:features/j2:feature/j2:features/j2:feature[@name='ChildSource']" />

	<xsl:template match="/">

		<fits xmlns="http://hul.harvard.edu/ois/xml/ns/fits/fits_output">

			<xsl:apply-imports />

			<metadata>
				<document>
					<!-- TODO Does FITS allow only flat structures here?? -->
					<numChildSources>
						<xsl:value-of select="$numchildsources" />
					</numChildSources>
					<xsl:for-each select="$childsources">
						<childSource>
							<xsl:attribute name="childPath">
								<xsl:value-of select="*/j2:feature[@name='Path']/j2:value" />
							</xsl:attribute>
							<xsl:attribute name="childSize">
								<xsl:value-of select="*/j2:feature[@name='Size']/j2:value" />
							</xsl:attribute>
							<xsl:attribute name="childLastModified">
								<xsl:value-of select="*/j2:feature[@name='LastModified']/j2:value" />
							</xsl:attribute>
							<xsl:attribute name="childCrc32MessageDigest">
								<xsl:value-of
								select="*/j2:feature[@name='CRC32MessageDigest']/j2:value" />
							</xsl:attribute>
							<xsl:attribute name="childSourceName">
								<xsl:value-of select="*/j2:feature[@name='SourceName']/j2:value" />
							</xsl:attribute>
							<xsl:attribute name="childNumChildSources">
								<xsl:value-of select="*/j2:feature[@name='NumChildSources']/j2:value" />
							</xsl:attribute>
							<xsl:value-of select="*/j2:feature[@name='ReportableName']/j2:value" />
						</childSource>
					</xsl:for-each>
				</document>
			</metadata>

		</fits>
	</xsl:template>
</xsl:stylesheet>
