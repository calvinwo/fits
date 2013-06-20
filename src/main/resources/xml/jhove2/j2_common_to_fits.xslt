<?xml version="1.0" ?>
<xsl:stylesheet version="2.0"
	xmlns="http://hul.harvard.edu/ois/xml/ns/fits/fits_output"
	xmlns:fits_XsltFunctions="edu.harvard.hul.ois.fits.tools.utils.XsltFunctions"
	xmlns:j2="http://jhove2.org/xsd/1.0.0" xmlns:mix="http://www.loc.gov/mix/"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output indent="yes" method="xml" />
	<xsl:strip-space elements="*" />

	<!-- TODO Should we get the tool's name out of the JHOVE2 output, too?? -->
	<xsl:variable name="toolname">
		JHOVE2
	</xsl:variable>
	<xsl:variable name="toolversion"
		select="/j2:jhove2/j2:features/j2:feature[@name='Version']/j2:value" />
	<xsl:variable name="j2id"
		select="/j2:jhove2/j2:feature[@name='FileSource']/j2:features/j2:feature[@name='PresumptiveFormats']/j2:features/j2:feature[@name='PresumptiveFormat']/j2:features/j2:feature[@name='JHOVE2Identifier']/j2:features/j2:feature[@name='Value']/j2:value" />
	<xsl:variable name="confidence"
		select="//j2:feature[@name='PresumptiveFormats']//j2:features//j2:feature[@name='Confidence']/j2:value" />
	<xsl:variable name="isValid"
		select="//j2:feature[@name='Modules']//j2:features//j2:feature[@name='Module']//j2:features//j2:feature[@name='isValid']" />

	<xsl:template match="/">

		<identification>
			<identity>
				<xsl:attribute name="mimetype">
					<xsl:choose>
						<xsl:when test="$j2id='http://jhove2.org/terms/format/zip'">
							<xsl:value-of select="string('application/zip')" />
						</xsl:when>
						<xsl:when test="$j2id='http://jhove2.org/terms/format/tiff'">
                            <xsl:value-of select="string('image/tiff')" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="string('mime')" />
                        </xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<xsl:attribute name="format">
					<xsl:choose>
						<xsl:when test="$j2id='http://jhove2.org/terms/format/zip'">
							<xsl:value-of select="string('ZIP Format')" />
						</xsl:when>
                        <xsl:when test="$j2id='http://jhove2.org/terms/format/tiff'">
                            <xsl:value-of select="string('TIFF Format')" />
                        </xsl:when>
						<xsl:otherwise>
						  <xsl:value-of select="$j2id" />
						</xsl:otherwise>
						
					</xsl:choose>
				</xsl:attribute>

				<externalIdentifier toolname="$toolname"
					toolversion="$toolversion" type="JHOVE2Identifier">
					<xsl:value-of select="$j2id" />
				</externalIdentifier>
			</identity>
		</identification>

		<!-- TODO File status must be checked again concerning which confidence 
			levels are mapped to which validity and/or wellformedness levels!! -->
		<!-- TODO Mappings only for formats that do not have the do not have the 
			isWellFormed ans isValid attribtues!! -->
		<filestatus>
			<xsl:choose>
				<xsl:when
					test="$confidence='PositiveSpecific' or $confidence='PositiveGeneric' or $confidence='Validated'">
					<well-formed>true</well-formed>
					<valid>true</valid>
				</xsl:when>
				<xsl:otherwise>
					<well-formed>false</well-formed>
					<xsl:choose>
						<xsl:when test="$isValid='true'">
							<valid>true</valid>
						</xsl:when>
						<xsl:otherwise>
							<valid>false</valid>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>

			<!-- <well-formed toolname="$toolname" toolversion="$toolversion" status="SINGLE_RESULT"> 
				<xsl:choose> <xsl:when test="$confidence='PositiveSpecific' or $confidence='PositiveGeneric' 
				or $confidence='Validated'"> true </xsl:when> <xsl:otherwise> undetermined 
				</xsl:otherwise> </xsl:choose> </well-formed> <valid toolname="$toolname" 
				toolversion="$toolversion" status="SINGLE_RESULT"> <xsl:choose> <xsl:when 
				test="$confidence='PositiveSpecific' or $confidence='PositiveGeneric' or 
				$confidence='Validated'"> true </xsl:when> <xsl:otherwise> undetermined </xsl:otherwise> 
				</xsl:choose> </valid> -->
		</filestatus>

	</xsl:template>
</xsl:stylesheet>
