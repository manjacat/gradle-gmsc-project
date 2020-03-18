package com.intergraph.dude.extensions;

public enum DefaultEditCmd
{

	// This List is for SNC
	FEATURE_543 ("EDIT_SNC_RedlineDemand"),
	FEATURE_544 ("EDIT_SNC_RedlineLine"),
	FEATURE_542 ("EDIT_SNC_RedlineLVDB/FP"),
	FEATURE_522 ("EDIT_SNC_RedlinePoint"),	
	FEATURE_622 ("EDIT_SNC_RedlineText"),	
	
	// This List is for SNC-SITE
	FEATURE_603 ("EDIT_RedlineDemPoint_Site"),
	FEATURE_604 ("EDIT_RedlineLine_Site"),
	FEATURE_602 ("EDIT_RedlineLVDB_Site"),
	FEATURE_605 ("EDIT_RedlinePoint_Site"),	
	FEATURE_623 ("EDIT_RedlineText_Site"),	
	
	//Added 23 Aug 2018 - CR2 Phase 1
	//1346	Redline Manhole (Site)	DUDEEO.CGIS_REDLINE_MANHOLE	Redline XEditRedlineManholeSite
	FEATURE_1346 ("XEditRedlineManholeSite"),
	
	//1347	Redline Pit (Site)	XEditRedlinePitSite	RedlinePit
	FEATURE_1347 ("XEditRedlinePitSite"),
	
	//1345	Redline Structure Duct (Site)	XEditRedlineStructureDuctSite	RedlineStDuct
	FEATURE_1345 ("XEditRedlineStructureDuctSite"),
	// End add
	
	
	// This List is for CS and CS-SITE
	FEATURE_840 ("CS_EDIT_LAND"),
	FEATURE_1224 ("CS_EDIT_LAND_OFF"),
		
	// This List is for PNI
	FEATURE_1177 ("EDIT_PNI_Redline_Area"),
	FEATURE_1173 ("EDIT_PNI_Redline_Line"),
	FEATURE_1171 ("EDIT_PNI_Redline_Point"),
	FEATURE_1179 ("EDIT_PNI_Redline_Point_Int"),	
	FEATURE_1175 ("EDIT_PNI_Redline_Text"),	
	FEATURE_315 ("ON_EDIT_BAY_315"), //ODP
	FEATURE_312 ("ON_EDIT_SLOT_312"), //Slot
	FEATURE_313 ("ON_EDIT_CARD_313"),//Card
	FEATURE_311 ("ON_EDIT_SHELF_311"),//Shelf OPT
	FEATURE_314 ("ON_EDIT_PORT_314"), // Connector
	FEATURE_209 ("ON_EDIT_SHEATH_209"), //Sheath or cable
	FEATURE_211 ("ON_EDIT_SPLICE_211"), //Splice closure
	FEATURE_216 ("ON_EDIT_UUB_216"), //UUB or Manhole
	FEATURE_165 ("ON_EDIT_FIBER_EXCESS_165"), //Fiber Excess Coil or Figure Eight

	// This List is for PNI-SITE
	FEATURE_362 ("OFF_EDIT_FIBER_EXCESS_362"), //Fiber Excess Coil or Figure Eight
	FEATURE_1178 ("EDIT_PNI_Redline_Area_Site"),
	FEATURE_1174 ("EDIT_PNI_Redline_Line_Site"),
	FEATURE_1180 ("EDIT_PNI_Redline_Point_Site"),
	FEATURE_1172 ("EDIT_PNI_Redline_Point_Int_Site"),	
	FEATURE_1176 ("EDIT_PNI_Redline_Text_Site"),	
	FEATURE_174 ("OFF_EDIT_BAY_174"), //ODP
	FEATURE_196 ("OFF_EDIT_SLOT_196"), //Slot
	FEATURE_182 ("OFF_EDIT_CARD_182"),//card
	FEATURE_195 ("OFF_EDIT_SHELF_195"),//shelf OPT
	FEATURE_183 ("OFF_EDIT_PORT_183"), // Connector
	FEATURE_364 ("OFF_EDIT_SHEATH_364"), //Sheath or cable
	FEATURE_363 ("OFF_EDIT_SPLICE_363"), //Splice closure
	FEATURE_367 ("OFF_EDIT_UUB_367"),//UUB or Manhole

	
	// This list is for DUDE
	FEATURE_95 ("EDIT_SubBus"),
	FEATURE_96 ("EDIT_CircuitBreaker"),
	FEATURE_30 ("EDIT_DemandPoint"),
	FEATURE_8 ("EDIT_DistributionSubstation"),
	FEATURE_51 ("EDIT_OHTransformer"),
	FEATURE_105 ("EDIT_Groundbank"),
	FEATURE_83 ("EDIT_SecondarySplice"),
	FEATURE_132 ("EDIT_SecondaryFuse"),
	FEATURE_47 ("EDIT_OHSecondaryConductor"),
	FEATURE_80 ("EDIT_SecondaryConductor"),
	FEATURE_20 ("EDIT_LVDB"),
	FEATURE_77 ("EDIT_PrimarySplice"),
	FEATURE_127 ("EDIT_Fuse"),
	FEATURE_45 ("EDIT_OHPrimaryConductor"),
	FEATURE_69 ("EDIT_Switch"),
	FEATURE_74 ("EDIT_PrimaryConductor"),
	FEATURE_42 ("EDIT_Manhole"),
	FEATURE_116 ("EDIT_SubSubstation"),
	FEATURE_57 ("EDIT_Pit"),
	FEATURE_62 ("EDIT_Pole"),
	FEATURE_38 ("EDIT_Light"),
	FEATURE_88 ("EDIT_Duct"),
	FEATURE_121 ("EDIT_SwitchBank"),
	FEATURE_401 ("EDIT_MainTransformer"),
	FEATURE_402 ("EDIT_RMU"),
	FEATURE_405 ("EDIT_SubFuse"),
	FEATURE_444 ("EDIT_LVFEEDERPOINT"),
	FEATURE_445 ("VIEW_FEEDERPOINT"),
	//1483 EDIT_MV_Cable_Terminal - ADDED FOR DM 2019 - AZRUL
	FEATURE_1483 ("EDIT_MV_Cable_Terminal"),
	//1444 EDIT_Pothead
	FEATURE_1444 ("EDIT_Pothead"),
	//1442 EDIT_Recloser
	FEATURE_1442 ("EDIT_Recloser"),
	
	//EDIT_Redline_PE_Site
	
	
	// This list is for DUDE-SITE
	//302	EDIT_SubBus_Site
	FEATURE_302 ("EDIT_SubBus_Site"),
	//303	EDIT_CircuitBreaker_Site
	FEATURE_303 ("EDIT_CircuitBreaker_Site"),
	//323	EDIT_DemandPoint_Site
	FEATURE_323 ("EDIT_DemandPoint_Site"),
	//324	EDIT_DistributionSubstation_Site
	FEATURE_324 ("EDIT_DistributionSubstation_Site"),	
	//325	EDIT_OHTransformer_Site
	FEATURE_325 ("EDIT_OHTransformer_Site"),	
	//307	EDIT_Groundbank_Site
	FEATURE_307 ("EDIT_Groundbank_Site"),	
	//326	EDIT_SecondarySplice_Site
	FEATURE_326 ("EDIT_SecondarySplice_Site"),	
	//327	EDIT_SecondaryFuse_Site
	FEATURE_327 ("EDIT_SecondaryFuse_Site"),	
	//329	EDIT_OHSecondaryConductor_Site
	FEATURE_329 ("EDIT_OHSecondaryConductor_Site"),	
	//330	EDIT_SecondaryConductor_Site
	FEATURE_330 ("EDIT_SecondaryConductor_Site"),	
	//331	EDIT_LVDB_Site
	FEATURE_331 ("EDIT_LVDB_Site"),	
	//333	EDIT_PrimarySplice_Site
	FEATURE_333 ("EDIT_PrimarySplice_Site"),	
	//334	EDIT_Fuse_Site
	FEATURE_334 ("EDIT_Fuse_Site"),	
	//335	EDIT_OHPrimaryConductor_Site
	FEATURE_335 ("EDIT_OHPrimaryConductor_Site"),	
	//336	EDIT_Switch_Site
	FEATURE_336 ("EDIT_Switch_Site"),	
	//337	EDIT_PrimaryConductor_Site
	FEATURE_337 ("EDIT_PrimaryConductor_Site"),	
	//332	EDIT_Manhole_Site
	FEATURE_332 ("EDIT_Manhole_Site"),	
	//321	EDIT_SubSubstation_Site
	FEATURE_321 ("EDIT_SubSubstation_Site"),	
	//338	EDIT_Pit_Site
	FEATURE_338	("EDIT_Pit_Site"),
	//339	EDIT_Pole_Site
	FEATURE_339 ("EDIT_Pole_Site"),
	//342	EDIT_Light_Site
	FEATURE_342 ("EDIT_Light_Site"),
	//343	EDIT_Duct_Site
	FEATURE_343 ("EDIT_Duct_Site"),
	//305	EDIT_SwitchBank_Site
	FEATURE_305 ("EDIT_SwitchBank_Site"),
	//406	EDIT_RMU_SITE
	FEATURE_406 ("EDIT_RMU_SITE"),
	//409	EDIT_SUBFUSE_SITE
	FEATURE_409 ("EDIT_SUBFUSE_SITE"),
	//448 VIEW_FEEDERPOINT_SITE
	FEATURE_448 ("VIEW_FEEDERPOINT_SITE"),
	//447 EDIT_LVFEEDERPOINT_SITE
	FEATURE_447 ("EDIT_LVFEEDERPOINT_SITE"),
	//410	EDIT_MainTransformer_SITE
	FEATURE_410 ("EDIT_MainTransformer_SITE"),
	
	//1484 OFF_Edit_MV_Cable_MV_Cable_Terminal - ADDED FOR DM 2019 - AZRUL
	FEATURE_1484 ("OFF_Edit_MV_Cable_MV_Cable_Terminal"),
	//1454 OFF_NEW_Pothead
	FEATURE_1454 ("OFF_Edit_Pothead"),
	//1452 OFF_EDIT_Recloser
	FEATURE_1452 ("OFF_EDIT_Recloser"),
	
	//CR4 Changes 29Oct2019
	//1525 W_EDIT_Redline_PE <--- For redline PE/LVDB
	FEATURE_1525 ("W_EDIT_Redline_PE"),
	//1526 EDIT_Redline_PE_Site <--- For redline PE/LVDB Site
	FEATURE_1526 ("EDIT_Redline_PE_Site"),
	FEATURE_1646 ("W_EDIT_Redline_CPPLVDB"),
	FEATURE_1647 ("EDIT_Redline_LVDB_Site");
	 

    private final String CmdName;       

    private DefaultEditCmd(String s) {
    	CmdName = s;
    }

    public boolean equalsCmdName(String otherCmdName) {
        return (otherCmdName == null) ? false : CmdName.equals(otherCmdName);
    }

    public String toString() {
       return this.CmdName;
    }

}
