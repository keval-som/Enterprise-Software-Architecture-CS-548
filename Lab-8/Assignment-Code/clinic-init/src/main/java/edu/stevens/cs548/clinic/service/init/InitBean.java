package edu.stevens.cs548.clinic.service.init;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import edu.stevens.cs548.clinic.service.IPatientService;
import edu.stevens.cs548.clinic.service.IProviderService;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.PhysiotherapyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDtoFactory;

@Singleton
@LocalBean
@Startup
// @ApplicationScoped
// @Transactional
public class InitBean {

	private static final Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());

	private static final ZoneId ZONE_ID = ZoneOffset.UTC;

	private PatientDtoFactory patientFactory = new PatientDtoFactory();

	private ProviderDtoFactory providerFactory = new ProviderDtoFactory();

	private TreatmentDtoFactory treatmentFactory = new TreatmentDtoFactory();

	// TODO
	@Inject
	private IPatientService patientService;

	// TODO
	@Inject
	private IProviderService providerService;

	/*
	 * Initialize using EJB logic
	 */
	@PostConstruct
	/*
	 * This should work to initialize with CDI bean, but there is a bug in
	 * Payara.....
	 */
	// public void init(@Observes @Initialized(ApplicationScoped.class)
	// ServletContext init) {
	public void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the
		 * server logs.
		 */
		logger.info("Your name here: Keval Sompura");
		System.err.println("Your name here!");

		try {

			/*
			 * Clear the database and populate with fresh data.
			 *
			 * Note that the service generates the external ids, when adding the entities.
			 */

			providerService.removeAll();
			patientService.removeAll();

			PatientDto john = patientFactory.createPatientDto();
			john.setName("John Doe");
			john.setDob(LocalDate.parse("1995-08-15"));

			john.setId(patientService.addPatient(john));

			ProviderDto jane = providerFactory.createProviderDto();
			jane.setName("Jane Doe");
			jane.setNpi("1234");

			jane.setId(providerService.addProvider(jane));

			DrugTreatmentDto drug01 = treatmentFactory.createDrugTreatmentDto();
			drug01.setPatientId(john.getId());
			drug01.setPatientName(john.getName());
			drug01.setProviderId(jane.getId());
			drug01.setProviderName(jane.getName());
			drug01.setDiagnosis("Headache");
			drug01.setDrug("Aspirin");
			drug01.setDosage(20);
			drug01.setFrequency(7);
			drug01.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drug01.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));

			providerService.addTreatment(drug01);

			// TODO add more testing, including treatments and providers

			PatientDto keval = patientFactory.createPatientDto();
			keval.setName("Keval Sompura");
			keval.setDob(LocalDate.parse("1999-01-02"));

			keval.setId(patientService.addPatient(keval));

			ProviderDto aetna = providerFactory.createProviderDto();
			aetna.setName("Aetna Doe");
			aetna.setNpi("5522");
			aetna.setId(providerService.addProvider(aetna));

			PhysiotherapyTreatmentDto physiotherapyTreatmentDto = treatmentFactory.createPhysiotherapyTreatmentDto();
			physiotherapyTreatmentDto.setTreatmentDates(Arrays.asList(LocalDate.parse("2024-01-01")));
			physiotherapyTreatmentDto.setDiagnosis("Knee pain");
			physiotherapyTreatmentDto.setPatientId(keval.getId());
			physiotherapyTreatmentDto.setPatientName(keval.getName());
			physiotherapyTreatmentDto.setProviderId(aetna.getId());
			physiotherapyTreatmentDto.setProviderName(aetna.getName());
			providerService.addTreatment(physiotherapyTreatmentDto);

			SurgeryTreatmentDto surgery = treatmentFactory.createSurgeryTreatmentDto();
			surgery.setPatientId(keval.getId());
			surgery.setPatientName(keval.getName());
			surgery.setProviderId(aetna.getId());
			surgery.setProviderName(aetna.getName());
			surgery.setSurgeryDate(LocalDate.parse("2024-01-02"));
			surgery.setDiagnosis("knee replacement");
			surgery.setDischargeInstructions("Get well soon");
			providerService.addTreatment(surgery);

			RadiologyTreatmentDto radiologyTreatmentDto = treatmentFactory.createRadiologyTreatmentDto();
			radiologyTreatmentDto.setPatientId(keval.getId());
			radiologyTreatmentDto.setPatientName(keval.getName());
			radiologyTreatmentDto.setProviderId(aetna.getId());
			radiologyTreatmentDto.setProviderName(aetna.getName());
			radiologyTreatmentDto.setDiagnosis("cancer");
			radiologyTreatmentDto.setTreatmentDates(Arrays.asList(LocalDate.parse("2025-02-04")));
			providerService.addTreatment(radiologyTreatmentDto);

			DrugTreatmentDto drugTreatmentDto = treatmentFactory.createDrugTreatmentDto();
			drugTreatmentDto.setPatientId(keval.getId());
			drugTreatmentDto.setPatientName(keval.getName());
			drugTreatmentDto.setProviderId(aetna.getId());
			drugTreatmentDto.setProviderName(aetna.getName());
			drugTreatmentDto.setDiagnosis("Cold and cough");
			drugTreatmentDto.setDrug("paracetamol");
			drugTreatmentDto.setDosage(20);
			drugTreatmentDto.setFrequency(7);
			drugTreatmentDto.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drugTreatmentDto.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));

			Collection<TreatmentDto> treatmentDtoCollection = new ArrayList<>();
			PhysiotherapyTreatmentDto physio = treatmentFactory.createPhysiotherapyTreatmentDto();
			physio.setPatientId(keval.getId());
			physio.setPatientName(keval.getName());
			physio.setProviderId(aetna.getId());
			physio.setProviderName(aetna.getName());
			physio.setTreatmentDates(Arrays.asList(LocalDate.parse("2025-02-04")));
			physio.setDiagnosis("back pain");
			treatmentDtoCollection.add(physio);

			drugTreatmentDto.setFollowupTreatments(treatmentDtoCollection);
			providerService.addTreatment(drugTreatmentDto);

			PatientDto alice = patientFactory.createPatientDto();
			alice.setName("Alice Brown");
			alice.setDob(LocalDate.parse("1988-06-25"));
			alice.setId(patientService.addPatient(alice));

			ProviderDto drSmith = providerFactory.createProviderDto();
			drSmith.setName("Dr. Smith");
			drSmith.setNpi("7788");
			drSmith.setId(providerService.addProvider(drSmith));

			DrugTreatmentDto drugTreatmentAlice = treatmentFactory.createDrugTreatmentDto();
			drugTreatmentAlice.setPatientId(alice.getId());
			drugTreatmentAlice.setPatientName(alice.getName());
			drugTreatmentAlice.setProviderId(drSmith.getId());
			drugTreatmentAlice.setProviderName(drSmith.getName());
			drugTreatmentAlice.setDiagnosis("Allergy");
			drugTreatmentAlice.setDrug("Cetirizine");
			drugTreatmentAlice.setDosage(10);
			drugTreatmentAlice.setFrequency(1);
			drugTreatmentAlice.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			drugTreatmentAlice.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			providerService.addTreatment(drugTreatmentAlice);

			PhysiotherapyTreatmentDto physioAlice = treatmentFactory.createPhysiotherapyTreatmentDto();
			physioAlice.setPatientId(alice.getId());
			physioAlice.setPatientName(alice.getName());
			physioAlice.setProviderId(drSmith.getId());
			physioAlice.setProviderName(drSmith.getName());
			physioAlice.setTreatmentDates(Arrays.asList(LocalDate.parse("2024-02-20")));
			physioAlice.setDiagnosis("Shoulder pain");

			Collection<TreatmentDto> followUpTreatments = new ArrayList<>();
			DrugTreatmentDto followUpDrugTreatment = treatmentFactory.createDrugTreatmentDto();
			followUpDrugTreatment.setPatientId(alice.getId());
			followUpDrugTreatment.setPatientName(alice.getName());
			followUpDrugTreatment.setProviderId(drSmith.getId());
			followUpDrugTreatment.setProviderName(drSmith.getName());
			followUpDrugTreatment.setDiagnosis("Follow-up for Allergy");
			followUpDrugTreatment.setDrug("Cetirizine");
			followUpDrugTreatment.setDosage(10);
			followUpDrugTreatment.setFrequency(1);
			followUpDrugTreatment.setStartDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			followUpDrugTreatment.setEndDate(LocalDate.ofInstant(Instant.now(), ZONE_ID));
			followUpTreatments.add(followUpDrugTreatment);

			physioAlice.setFollowupTreatments(followUpTreatments);
			providerService.addTreatment(physioAlice);

			PatientDto bob = patientFactory.createPatientDto();
			bob.setName("Bob Green");
			bob.setDob(LocalDate.parse("1992-11-30"));
			bob.setId(patientService.addPatient(bob));

			ProviderDto drLee = providerFactory.createProviderDto();
			drLee.setName("Dr. Lee");
			drLee.setNpi("3344");
			drLee.setId(providerService.addProvider(drLee));

			SurgeryTreatmentDto surgeryBob = treatmentFactory.createSurgeryTreatmentDto();
			surgeryBob.setPatientId(bob.getId());
			surgeryBob.setPatientName(bob.getName());
			surgeryBob.setProviderId(drLee.getId());
			surgeryBob.setProviderName(drLee.getName());
			surgeryBob.setSurgeryDate(LocalDate.parse("2024-03-15"));
			surgeryBob.setDiagnosis("Appendectomy");
			surgeryBob.setDischargeInstructions("take care");
			providerService.addTreatment(surgeryBob);

			RadiologyTreatmentDto radiologyBob = treatmentFactory.createRadiologyTreatmentDto();
			radiologyBob.setPatientId(bob.getId());
			radiologyBob.setPatientName(bob.getName());
			radiologyBob.setProviderId(drLee.getId());
			radiologyBob.setProviderName(drLee.getName());
			radiologyBob.setDiagnosis("Chest X-ray");
			radiologyBob.setTreatmentDates(Arrays.asList(LocalDate.parse("2024-04-10")));
			providerService.addTreatment(radiologyBob);

			PhysiotherapyTreatmentDto physioBob = treatmentFactory.createPhysiotherapyTreatmentDto();
			physioBob.setPatientId(bob.getId());
			physioBob.setPatientName(bob.getName());
			physioBob.setProviderId(drLee.getId());
			physioBob.setProviderName(drLee.getName());
			physioBob.setTreatmentDates(Arrays.asList(LocalDate.parse("2024-05-01")));
			physioBob.setDiagnosis("Lower back pain");

			Collection<TreatmentDto> treatmentDtoBob = new ArrayList();
			SurgeryTreatmentDto surgeryBob2 = treatmentFactory.createSurgeryTreatmentDto();
			surgeryBob2.setPatientId(bob.getId());
			surgeryBob2.setPatientName(bob.getName());
			surgeryBob2.setProviderId(drLee.getId());
			surgeryBob2.setProviderName(drLee.getName());
			surgeryBob2.setSurgeryDate(LocalDate.parse("2024-06-15"));
			surgeryBob2.setDiagnosis("Lower back pain");
			surgeryBob2.setDischargeInstructions("take care");
			treatmentDtoBob.add(surgeryBob2);

			physioBob.setFollowupTreatments(treatmentDtoBob);
			providerService.addTreatment(physioBob);

			// Now show in the logs what has been added

			Collection<PatientDto> patients = patientService.getPatients();
			for (PatientDto p : patients) {
				logger.info(String.format("Patient %s, ID %s, DOB %s", p.getName(), p.getId().toString(),
						p.getDob().toString()));
				logTreatments(p.getTreatments());
			}

			Collection<ProviderDto> providers = providerService.getProviders();
			for (ProviderDto p : providers) {
				logger.info(String.format("Provider %s, ID %s, NPI %s", p.getName(), p.getId().toString(), p.getNpi()));
				logTreatments(p.getTreatments());
			}

		} catch (Exception e) {

			throw new IllegalStateException("Failed to add record.", e);

		}

	}

	public void shutdown(@Observes @Destroyed(ApplicationScoped.class) ServletContext init) {
		logger.info("App shutting down....");
	}

	private void logTreatments(Collection<TreatmentDto> treatments) {
		for (TreatmentDto treatment : treatments) {
			if (treatment instanceof DrugTreatmentDto) {
				logTreatment((DrugTreatmentDto) treatment);
			} else if (treatment instanceof SurgeryTreatmentDto) {
				logTreatment((SurgeryTreatmentDto) treatment);
			} else if (treatment instanceof RadiologyTreatmentDto) {
				logTreatment((RadiologyTreatmentDto) treatment);
			} else if (treatment instanceof PhysiotherapyTreatmentDto) {
				logTreatment((PhysiotherapyTreatmentDto) treatment);
			}
			if (!treatment.getFollowupTreatments().isEmpty()) {
				logger.info("============= Follow-up Treatments");
				logTreatments(treatment.getFollowupTreatments());
				logger.info("============= End Follow-up Treatments");
			}
		}
	}

	private void logTreatment(DrugTreatmentDto t) {
		logger.info(String.format("...Drug treatment for %s, drug %s", t.getPatientName(), t.getDrug()));
	}

	private void logTreatment(RadiologyTreatmentDto t) {
		logger.info(String.format("...Radiology treatment for %s", t.getPatientName()));
	}

	private void logTreatment(SurgeryTreatmentDto t) {
		logger.info(String.format("...Surgery treatment for %s", t.getPatientName()));
	}

	private void logTreatment(PhysiotherapyTreatmentDto t) {
		logger.info(String.format("...Physiotherapy treatment for %s", t.getPatientName()));
	}

}
