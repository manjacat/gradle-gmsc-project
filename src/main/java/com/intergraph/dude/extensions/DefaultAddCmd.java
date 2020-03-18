package com.intergraph.dude.extensions;

public enum DefaultAddCmd
{
	// This List is for SNC
	FEATURE_543 ("NEW_RedlineDemand"),
	FEATURE_544 ("NEW_RedlineLine"),
	FEATURE_542 ("NEW_RedlineLVDB"),
	FEATURE_522 ("NEW_RedlinePoint"),	
	FEATURE_622 ("NEW_RedlineText"),	
	
	// This List is for SNC-SITE
	//feature 603 added to allow user to add demand point in site
	FEATURE_603 ("NEW_RedlineDemPoint_Site"),
	FEATURE_604 ("NEW_RedlineLine_Site"),
	FEATURE_602 ("NEW_RedlineLVDB_Site"),
	FEATURE_605 ("NEW_RedlinePoint_Site"),	
	FEATURE_623 ("NEW_RedlineText_Site"),	
	// Add new object - 23Aug 2018 for CR 2 phase 1
	//Simple ID	Feature Name	Entity	Symbology Name	Symbology ID	Action Name (Add)
	//1346	Redline Manhole (Site)	XNewRedlineManholeSite
	FEATURE_1346 ("XNewRedlineManholeSite"),
	//1347	Redline Pit (Site)	DUDEEO.CGIS_REDLINE_PIT	XNewRedlinePitSite
	FEATURE_1347 ("XNewRedlinePitSite"),
	//1345	Redline Structure Duct (Site)	DUDEEO.CGIS_REDLINE_DUCT	XNewRedlineStructureDuctSite
	FEATURE_1345 ("XNewRedlineStructureDuctSite"),
	//End add
	
	// This List is for CS and CS-SITE
	FEATURE_840 ("ADD_TNB_LAND"),
	FEATURE_1224 ("ADD_TNB_LAND_OFF"),
	
	// This List is for PNI
	FEATURE_1177 ("NEW_PNI_Redline_Area"),
	FEATURE_1173 ("NEW_PNI_Redline_Line"),
	FEATURE_1171 ("NEW_PNI_Redline_Point"),
	FEATURE_1179 ("NEW_PNI_Redline_Point_Int"),	
	FEATURE_1175 ("NEW_PNI_Redline_Text"),	
	
	// This List is for PNI-SITE
	FEATURE_1178 ("NEW_PNI_Redline_Area_Site"),
	FEATURE_1174 ("NEW_PNI_Redline_Line_Site"),
	FEATURE_1180 ("NEW_PNI_Redline_Point_Site"),
	FEATURE_1172 ("NEW_PNI_Redline_Point_Int_Site"),	
	FEATURE_1176 ("NEW_PNI_Redline_Text_Site"),	
	
	
	// This List is for DUDE
	FEATURE_30 ("NEW_DemandPoint"),
	FEATURE_8 ("NEW_DistributionSubstation"),
	FEATURE_51 ("NEW_OHTransformer"),
	FEATURE_83 ("NEW_SecondarySplice"),
	FEATURE_132 ("NEW_SecondaryFuse"),
	FEATURE_47 ("NEW_OHSecondaryConductor"),
	FEATURE_80 ("NEW_SecondaryConductor"),
	FEATURE_20 ("NEW_LVDB"),
	FEATURE_77 ("NEW_PrimarySplice"),
	FEATURE_127 ("NEW_Fuse"),
	FEATURE_45 ("NEW_OHPrimaryConductor"),
	FEATURE_69 ("NEW_Switch"),
	FEATURE_74 ("NEW_PrimaryConductor"),
	FEATURE_42 ("NEW_Manhole"),
	FEATURE_116 ("NEW_SubSubstation"),
	FEATURE_57 ("NEW_Pit"),
	FEATURE_62 ("NEW_Pole"),
	FEATURE_38 ("NEW_Light"),
	FEATURE_88 ("NEW_Duct"),
	//1483 New_MV_Cable_Terminal - ADDED FOR DM 2019 - AZRUL
	FEATURE_1483 ("New_MV_Cable_Terminal"),
	//1444 New_Pothead
	FEATURE_1444 ("NEW_Pothead"),
	//1442 NEW_Recloser
	FEATURE_1442 ("NEW_Recloser"),
	
	// This list is for DUDE-SITE
	//323	NEW_DemandPoint_Site
	FEATURE_323 ("NEW_DemandPoint_Site"),
	//324	NEW_DistributionSubstation_Site
	FEATURE_324 ("NEW_DistributionSubstation_Site"),	
	//325	NEW_OHTransformer_Site
	FEATURE_325 ("NEW_OHTransformer_Site"),	
	//326	NEW_SecondarySplice_Site
	FEATURE_326 ("NEW_SecondarySplice_Site"),	
	//327	NEW_SecondaryFuse_Site
	FEATURE_327 ("NEW_SecondaryFuse_Site"),	
	//329	NEW_OHSecondaryConductor_Site
	FEATURE_329 ("NEW_OHSecondaryConductor_Site"),	
	//330	NEW_SecondaryConductor_Site
	FEATURE_330 ("NEW_SecondaryConductor_Site"),	
	//331	NEW_LVDB_Site
	FEATURE_331 ("NEW_LVDB_Site"),	
	//333	NEW_PrimarySplice_Site
	FEATURE_333 ("NEW_PrimarySplice_Site"),	
	//334	NEW_Fuse_Site
	FEATURE_334 ("NEW_Fuse_Site"),	
	//335	NEW_OHPrimaryConductor_Site
	FEATURE_335 ("NEW_OHPrimaryConductor_Site"),	
	//336	NEW_Switch_Site
	FEATURE_336 ("NEW_Switch_Site"),	
	//337	NEW_PrimaryConductor_Site
	FEATURE_337 ("NEW_PrimaryConductor_Site"),	
	//332	NEW_Manhole_Site
	FEATURE_332 ("NEW_Manhole_Site"),	
	//321	NEW_SubSubstation_Site
	FEATURE_321 ("NEW_SubSubstation_Site"),	
	//338	NEW_Pit_Site
	FEATURE_338	("NEW_Pit_Site"),
	//339	NEW_Pole_Site
	FEATURE_339 ("NEW_Pole_Site"),
	//342	NEW_Light_Site
	FEATURE_342 ("NEW_Light_Site"),
	//343	NEW_Duct_Site
	FEATURE_343 ("NEW_Duct_Site"),
	
	//1484 OFF_New_MV_Cable_MV_Cable_Terminal - ADDED FOR DM 2019 - AZRUL
	FEATURE_1484 ("OFF_New_MV_Cable_MV_Cable_Terminal"),
	//1454 OFF_NEW_Pothead
	FEATURE_1454 ("OFF_NEW_Pothead"),
	//1452 OFF_NEW_Recloser
	FEATURE_1452 ("OFF_NEW_Recloser");
	

    private final String CmdName;       

    private DefaultAddCmd(String s) {
    	CmdName = s;
    }

    public boolean equalsCmdName(String otherCmdName) {
        return (otherCmdName == null) ? false : CmdName.equals(otherCmdName);
    }

    public String toString() {
       return this.CmdName;
    }

}
