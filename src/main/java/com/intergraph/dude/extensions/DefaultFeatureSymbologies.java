package com.intergraph.dude.extensions;
import java.util.UUID;
public enum DefaultFeatureSymbologies
{
	///////////////////////////////////////
	//// This list is for DUDEPNI SITE ////
	///////////////////////////////////////
	
	//1171	Redline_Point	Default	58997b86-c016-4b51-b428-79c77e0d80fe
	FEATURE_1171 ("58997b86-c016-4b51-b428-79c77e0d80fe"),
	//1172	Redline_Point (Site)	Default	58997b86-c016-4b51-b428-79c77e0d80fe
	FEATURE_1172 ("58997b86-c016-4b51-b428-79c77e0d80fe"),
	//1173	Redline_Line	Default	98114c1f-9de6-46f3-891c-210f46dc04a0
	FEATURE_1173 ("98114c1f-9de6-46f3-891c-210f46dc04a0"),
	//1174	Redline_Line (Site)	Default	98114c1f-9de6-46f3-891c-210f46dc04a0
	FEATURE_1174 ("98114c1f-9de6-46f3-891c-210f46dc04a0"),
	//1175	Redline_Text	Default	608e8f8d-48a8-49bd-a2b2-04ce49b101ec
	FEATURE_1175 ("608e8f8d-48a8-49bd-a2b2-04ce49b101ec"),
	//1176	Redline_Text (Site)	Default	608e8f8d-48a8-49bd-a2b2-04ce49b101ec
	FEATURE_1176 ("608e8f8d-48a8-49bd-a2b2-04ce49b101ec"),
	//1177	Redline Area	e24cd099-5ff3-4fc9-b0b3-185f773731f9
	FEATURE_1177 ("e24cd099-5ff3-4fc9-b0b3-185f773731f9"),
	//1178	Redline_Area (Site)	Default	e24cd099-5ff3-4fc9-b0b3-185f773731f9
	FEATURE_1178 ("e24cd099-5ff3-4fc9-b0b3-185f773731f9"),
	//1179	Redline_Point_Internal	Default	41805da9-8e40-4fcc-8742-e8740c2ff64d
	FEATURE_1179 ("41805da9-8e40-4fcc-8742-e8740c2ff64d"),
	//1180	Redline_Point_Internal (Site)	Default	41805da9-8e40-4fcc-8742-e8740c2ff64d
	FEATURE_1180 ("41805da9-8e40-4fcc-8742-e8740c2ff64d"),
	
	//174	ODP (Site)	Edit	d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1	ID=-1
	FEATURE_174 ("d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1"),
	//176	ODP - Footprint (Site)	Default	471719dd-7264-45e0-bd6b-f8eb18c46842	NUM_OF_SHELF <= 0
	FEATURE_176 ("471719dd-7264-45e0-bd6b-f8eb18c46842"),
	//178	Building Structure (Site)	Edit	d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1	ID=-1
	FEATURE_178 ("d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1"),
	//182	Card (Site)	Edit	7924d530-b513-4d33-8fe1-12a161987342	ID=-1
	FEATURE_182 ("7924d530-b513-4d33-8fe1-12a161987342"),
	//183	Connector (Site)	Edit	689aad24-3b97-4097-8d48-abaf01c6d675	ID=-1
	FEATURE_183 ("689aad24-3b97-4097-8d48-abaf01c6d675"),
	//185	Room (Site)	Edit	d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1	ID=-1
	FEATURE_185 ("d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1"),
	//	195	OPT (Site)	Edit	d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1	ID=-1
	FEATURE_195 ("d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1"),
	//	196	Slot (Site)	Edit	d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1	ID=-1
	FEATURE_196 ("d59ae1a7-a41e-4861-8ff7-2ff965c4fbb1"),
	//	361	Building (Site)	Building - Edit	bbc89f48-58bc-447b-a77d-d57482334e7c	ID=-1
	FEATURE_361 ("bbc89f48-58bc-447b-a77d-d57482334e7c"),
	//	362	Fibre Excess Coil (Site)	Edit	b4eb7c39-6e1d-4f53-8ca2-05f67d84c2b2	ID=-1
	FEATURE_362 ("b4eb7c39-6e1d-4f53-8ca2-05f67d84c2b2"),
	//	363	Splice Closure (Site)	Edit	5f9f652e-9ce4-43ad-bb4f-58467a45143b	ID=-1
	FEATURE_363 ("5f9f652e-9ce4-43ad-bb4f-58467a45143b"),
	//	364	Link (Site)	Edit	4f6964ca-f140-4f96-b630-532404f817b3	ID=-1
	FEATURE_364 ("4f6964ca-f140-4f96-b630-532404f817b3"),
	//	365	Tower (Site)	Edit	5e1b9ad3-a773-4bc6-8fcc-040c66843d5d	ID=-1
	FEATURE_365 ("5e1b9ad3-a773-4bc6-8fcc-040c66843d5d"),
	//	366	Underground Route	Edit	23a81f72-c415-4dce-b47b-5c09b29af11f	ID=-1
	FEATURE_366 ("23a81f72-c415-4dce-b47b-5c09b29af11f"),
	//	367	Manhole (Site)	Edit	d0376d76-2a60-4cf8-821e-8f95ca259d74	ID=-1
	FEATURE_367 ("d0376d76-2a60-4cf8-821e-8f95ca259d74"),
	//	369	Pole (Site)	Edit	a3e94f32-83f9-4745-b70b-99032ad6a0cf	ID=-1
	FEATURE_369 ("a3e94f32-83f9-4745-b70b-99032ad6a0cf"),
	//374	My SW Jobs (Site)	SW Job - To Site	5a98a092-0dc7-45c1-91cc-3572e7611b8a
	FEATURE_374 ("5a98a092-0dc7-45c1-91cc-3572e7611b8a"),
	
	////////////////////////////////////
	//// This list is for DUDE-SITE ////
	////////////////////////////////////
	
	//	302|Busbar (Site)|Busbar - Default|b9bc4e85-2da3-430d-af38-4f7449d65df4|(OPERATING_VOLTAGE IS NULL OR OPERATING_VOLTAGE NOT IN ('132 kV','275 kV','500 kV'))  AND STATUS <> 'Proposed Remove'|
	FEATURE_302 ("e928ce7e-6525-49f5-8a31-f6d59623dd5c"),
	//	303|Circuit Breaker (Site)|Circuit Breaker - Closed|0e60f76c-b4b1-4603-b232-be44d9d3d8fc| STATUS <> 'Proposed Remove' AND (NORMAL_STATUS <> 'OPEN' or NORMAL_STATUS IS NULL)|
	FEATURE_303 ("d826f0bc-da8f-4d93-b9fb-a7156ae52dad"),
	//	305|Switch Bank (Site)|Switch Bank - Unknown - Closed|2e8d5352-421b-485d-a1c5-11f02f710d40|(CGIS_CLASS = 'Unknown' OR CGIS_CLASS IS NULL ) AND NORMAL_STATUS = 'CLOSED' AND STATUS <> 'Proposed Remove'|
	FEATURE_305 ("40648774-365b-48ec-81d2-11b9ccef944c"),
	//	307|Earthing Electrode (Site)|GndBnk-NER33kV, unknown or default|4e836776-df0d-4c3e-a03e-a66f3d5c80ce|STATUS <> 'Proposed Remove' AND (CGIS_ED_GB_CLASS IN ('NER 33 kV','Unknown') OR CGIS_ED_GB_CLASS IS NULL)|
	FEATURE_307 ("07eefcd7-3615-4434-a8ee-f68abd2d2f9a"),
	//	321|Substation (Site)|Substation - StepDown, Unknown, Default|a36dc449-c000-41f1-96ef-e501e5b5504e| STATUS <> 'Proposed Remove' AND (CGIS_CATEGORY IS NULL OR CGIS_CATEGORY NOT IN ('PPU','SSU','PMU'))|
	FEATURE_321 ("02b4c61d-f80b-415a-b1a7-4346ad770d5c"),
	//	323|Demand Point (Site)|Demand Point - Default|b3d70ad8-39eb-4943-8478-da22acf6dc21|TO_VERIFY = 0 AND STATUS <> 'Proposed Remove'|
	FEATURE_323 ("59fe7ae4-2d8a-439f-8a96-2a99dcb1e924"),
	//	324|Distribution PE (Site)|Distribution PE - Default|1c6d804f-f1dc-4028-955d-94ebed5b393a|STATUS <> 'Proposed Remove' AND (CGIS_SG_CONFIG IS NULL OR (CGIS_SG_CONFIG NOT LIKE '%CB%' AND CGIS_SG_CONFIG NOT LIKE '%RMU%'))|
	FEATURE_324 ("45aec7fc-3d75-462c-8ab5-922ae644e985"),
	//	325|Distribution Transformer (Site)|Distribution Transformer - Default|388532b9-1dc6-43d3-9d81-791bf03b5f8c| STATUS <> 'Proposed Remove'|
	FEATURE_325 ("b630cf2f-0fbb-4e5b-941c-0d85fa4f31c5"),
	//	326|LV Cable Joint (Site)|LV Joint - Default, Unknown & Null|eb151ba8-9bd0-45d7-8a01-8f246634a15b|STATUS <> 'Proposed Remove' AND (CGIS_CAB_JOINT_CLASS LIKE 'LV%JOINT' OR CGIS_CAB_JOINT_CLASS IS NULL OR CGIS_CAB_JOINT_CLASS LIKE 'Unknown')|
	FEATURE_326 ("5e6cf7ef-205b-456f-96a8-7e919d608ab8"),
	//	329|LV OH Conductor (Site)|LV OH Conductor - Others|0b3e0963-84d0-4288-9d41-f38ad77f7cf2|(USAGE NOT LIKE '%LIGHT' OR USAGE IS NULL) AND STATUS <> 'Proposed Remove'|
	FEATURE_329 ("f17be64e-6482-499c-9557-860edfbb9495"),
	//  330|LV UG Conductor (Site)|LV UG Conductor - Others|bb9dcac9-1dc8-454d-9034-7ec7251d24e4|STATUS <> 'Proposed Remove' AND (USAGE NOT LIKE '%LIGHT' OR USAGE IS NULL)|
	FEATURE_330 ("d8f1f368-a485-4738-a72b-4d59f0db966e"),
	//	331|LVDB-FP - Cabinet (Site)|Cabinet - Unknown|cad0cbca-173a-40c0-8e1b-7548cc7bbb9f|CGIS_STRUCTURE = 'Unknown' AND TO_VERIFY=0 AND STATUS <> 'Proposed Remove'|
	FEATURE_331 ("2aa1a944-6966-4077-8729-7c331d212db3"),
	//	332|Manhole (Site)|Type 1 to 18|2b3081d7-0d7b-4fd0-892a-2bebf340d32a|STATUS <> 'Proposed Remove' AND (upper(CGIS_MH_TYPE) NOT LIKE '%MANHOLE%' OR CGIS_MH_TYPE IS NULL)|
	FEATURE_332 ("52b1bfb8-a277-42a7-8df2-318ffbdd0742"),
	//	333|MV Cable Joint (Site)|MV Cable Joint - Default|08bf52b5-06a6-4043-8e47-01f328806d6e|STATUS <> 'Proposed Remove' AND (TYPE NOT LIKE 'MV%TERMINAL' AND TYPE NOT LIKE 'POT%END')|
	FEATURE_333 ("db30a8ff-167c-4d22-a0a3-3b5e5ce2d0f2"),
	//	334|MV Fuse (Site)|MV Fuse Close, Unknown, Default|5680a000-f0a0-4299-987c-74da1334f7cc||
	FEATURE_334 ("62f9ee80-9e4f-4a70-b28f-13535c3a0fe1"),
	//	335|MV OH Conductor (Site)|6.6 kV or Unknown|2c562bbe-dc45-43ff-997e-b69f3acdc4a3| STATUS <> 'Proposed Remove' AND (NOMINAL_VOLTAGE LIKE '6.6%kV' OR NOMINAL_VOLTAGE LIKE 'Unknown')|
	FEATURE_335 ("af3bcea4-ac1b-4374-b0d4-8bc3dd2d445b"),
	//	336|MV Switch (Site)|MV Switch - default|90be6f63-bde1-4955-acc7-c9768c33278e||
	FEATURE_336 ("41d7b70c-92b4-4cff-b639-083f6e2f6db7"),
	//	337|MV UG Conductor (Site)|11kV, Unknown, Null|a616d635-05bc-400a-ada4-3a32086a2450| STATUS <> 'Proposed Remove' AND (NOMINAL_VOLTAGE LIKE '11%kV' OR NOMINAL_VOLTAGE LIKE 'Unknown' OR NOMINAL_VOLTAGE IS NULL)|
	FEATURE_337 ("1570395e-e0c2-479f-8387-9d84d28592f4"),
	//	338|Pit (Site)|Default|446727b3-f4e0-4c44-a98d-bcb1738a3b0b|STATUS <> 'Proposed Remove'|
	FEATURE_338 ("32b5022c-ea26-4f13-988f-3f31d19fd2d7"),
	//	339|Pole (Site)|Pole Unknown, Unknown LV, default or null|6941d47f-cecd-4ec0-8bb2-55a78759d3b6|(CGIS_STRUCTURE_TYPE = 'Unknown' OR upper(CGIS_STRUCTURE_TYPE) LIKE 'UNKNOWN%LV%' OR CGIS_STRUCTURE_TYPE IS NULL)  AND TO_VERIFY=0 AND STATUS <> 'Proposed Remove'|
	FEATURE_339 ("eafafdf4-f481-4ad9-a1a9-3d0445ae7a81"),
	//	342|Street Light (Site)|Default|34c9d693-9ed1-4e4e-b2b2-f9e52be760fd|STATUS <> 'Proposed Remove'|
	FEATURE_342 ("e53756fb-7333-4a9e-8b17-aab5a6bdaa23"),
	//	343|Structure Duct (Site)|Default|8964ac68-2bb1-4157-92d2-d0deede655f7|STATUS <> 'Proposed Remove'|
	//this is old one (line)
	//FEATURE_343 ("043d7075-6ce3-4397-901c-22522fc9c44c"), 
	//this is new one (extent)
	FEATURE_343 ("61120539-b27e-4399-9125-7d85437233a9"), 	
	//	360|My SW Design (Site)|Default|d51c71d6-5c12-4b9a-a661-50121324f9c9|TO_SITE=0|
	FEATURE_360 ("d51c71d6-5c12-4b9a-a661-50121324f9c9"),
	//	406|Ring Main Unit (Site)|Default|a57d13fa-f383-4c74-a45c-ecef31a019da|STATUS <> 'Proposed Remove'|
	FEATURE_406 ("f7a015dd-94cb-4e5f-9840-883856d0980b"),
	//	409|Substation Fuse (Site)|Substation Fuse - Closed|28f80c67-fd03-4768-9869-965a2fd0d9c8|STATUS <> 'Proposed Remove' AND (NORMAL_STATUS='CLOSED' OR NORMAL_STATUS IS NULL)|
	FEATURE_409 ("d91a990c-e5c3-4de7-bb00-4b0973fc152a"),
	//	410|Main Transformer (Site)|Default|a39a37b0-3ee2-4f69-abe5-701535b60392|STATUS <> 'Proposed Remove'|
	FEATURE_410 ("dcc35d25-214a-42cd-8fca-28198f275f90"),
	//	446|Customer (Site)|Default|3e54392c-006b-471b-8ce2-ee37d90df8bd||
	FEATURE_446 ("3e54392c-006b-471b-8ce2-ee37d90df8bd"),
	//	447|LV Feeder Point (Site)|Default|c6edd328-827b-4fec-8512-dc0925170da4||
	FEATURE_447 ("c6edd328-827b-4fec-8512-dc0925170da4"),
	//	448|Feeder Point (Site)|Default|252156d4-029c-4e35-a648-7b84b29cd4f0||
	FEATURE_448 ("252156d4-029c-4e35-a648-7b84b29cd4f0"),	
	
	//CR4 Update
	//1452|Auto Recloser DUDEEO.ED_RECLOSER 0 0 DUDE (SITE)  -- featureid: cc192567-67ad-4abb-bf6f-4ea77f2ebe39
	FEATURE_1452 ("4d3e086e-028f-4601-8f8b-be8c2ce674cc"),	
	//1454|Pothead DUDEEO.SUB_POTHEAD 0 0 DUDE (SITE)  -- featureid: 40238d77-44c4-4327-b9b4-4027326b72a8
	FEATURE_1454 ("e9a56fde-3bc9-4761-b33e-274f9632315e"),	
	//1526|Redline PE DUDEEO.CGIS_REDLINE_PE 0 0 DUDE (SITE)  -- featureid: 3ffc38fe-c112-4469-bac0-91a6df876e78
	FEATURE_1526 ("9a064fd2-c55b-4146-88aa-c74edaaba7b6"),	
	//1647|Redline LVDB DUDEEO.CGIS_REDLINE_PE 0 0 DUDE (SITE) -- featureid: d3242684-8067-4688-b275-7089289e5a42
	FEATURE_1647 ("31c76e5e-62f5-41e5-b1e0-b26d3289ccdc"),
	
	///////////////////////////////////////////////////
	/////////  DUDE-SNC SITE new redline //////////////
	///////////////////////////////////////////////////
	
	//602	LVDB-FP/Cabinet (SNC-Site)	DefaultLVDB	31c76e5e-62f5-41e5-b1e0-b26d3289ccdc	
	FEATURE_602 ("31c76e5e-62f5-41e5-b1e0-b26d3289ccdc"),
	//603	Redline_Demand_Point (SNC-Site)	Red Demand	3f88c45b-330d-4637-a1a9-a383068deadd	
	FEATURE_603 ("3f88c45b-330d-4637-a1a9-a383068deadd"),
	//604	Redline_Line (SNC-Site)	Red OH and Default	06b7fe18-39d9-4a60-950b-b7527b44962d
	FEATURE_604 ("06b7fe18-39d9-4a60-950b-b7527b44962d"),
	//605	Redline_Point (SNC-Site)	Red Pole or Default	465d6bcc-9cf5-419e-93f9-cb00ea024125
	FEATURE_605 ("465d6bcc-9cf5-419e-93f9-cb00ea024125"),
	//623	Redline_Text (SNC-Site)	Default	da015857-f118-42a6-b09a-05eb7cb646b3	
	FEATURE_623 ("da015857-f118-42a6-b09a-05eb7cb646b3"),
		
	//1346	Redline Manhole (Site)	DUDEEO.CGIS_REDLINE_MANHOLE	Redline Manhole	0ba1b95f-3ffe-4d27-8523-599a719576d8
	FEATURE_1346 ("0ba1b95f-3ffe-4d27-8523-599a719576d8"),	
	//1347	Redline Pit (Site)	DUDEEO.CGIS_REDLINE_PIT	Redline Pit	ae7bba66-8b38-4777-bb93-de2753716212
	FEATURE_1347 ("ae7bba66-8b38-4777-bb93-de2753716212"),	
	//1345	Redline Structure Duct (Site)	DUDEEO.CGIS_REDLINE_DUCT	Redline Structure Duct	91a7b7f9-ee90-4aff-bb1e-d369e8808fe9
	FEATURE_1345 ("91a7b7f9-ee90-4aff-bb1e-d369e8808fe9");
	
	

	
	
	
    private final String StyleId;       

    private DefaultFeatureSymbologies(String s) {
        StyleId = s;
    }

    public boolean equalsStyleId(String otherStyleId) {
        return (otherStyleId == null) ? false : StyleId.equals(otherStyleId);
    }

    public String toString() {
       return this.StyleId;
    }

    public UUID toUUID() {
    	return UUID.fromString(this.StyleId); 
   
    }
}
