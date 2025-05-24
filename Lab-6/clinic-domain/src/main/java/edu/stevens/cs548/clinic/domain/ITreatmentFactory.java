package edu.stevens.cs548.clinic.domain;


public interface ITreatmentFactory {
	
	public DrugTreatment createDrugTreatment ();
	
	/*
	 * TODO add methods for Radiology, Surgery, Physiotherapy
	 */

	public PhysiotherapyTreatment createPhysiotherapyTreatment ();

	public RadiologyTreatment createRadiologyTreatment ();

	public SurgeryTreatment createSurgeryTreatment ();
	
}
