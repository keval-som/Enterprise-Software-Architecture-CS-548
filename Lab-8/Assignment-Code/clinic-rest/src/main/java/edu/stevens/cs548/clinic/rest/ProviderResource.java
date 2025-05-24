package edu.stevens.cs548.clinic.rest;

import edu.stevens.cs548.clinic.service.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.IProviderService;
import edu.stevens.cs548.clinic.service.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.dto.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.nio.file.ProviderNotFoundException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO
@Path("provider")
@RequestScoped
public class ProviderResource extends ResourceBase {
	
	private final Logger logger = Logger.getLogger(ProviderResource.class.getCanonicalName());

	// TODO
	@Context
	private UriInfo uriInfo;
	
	// TODO
	@Inject
	private IProviderService providerService;
	
	// TODO

	/*
	 * Return a provider DTO including the list of treatments they are administering.
	 */
	@GET
	@Path("{id}")
	@Produces("application/json")
	public Response getProvider(@PathParam("id") String id) {
		// TODO Complete this (see getPatient)
		try{
			UUID providerId = UUID.fromString(id);
			ProviderDto provider = providerService.getProvider(providerId, true);
			ResponseBuilder responseBuilder = Response.ok(provider);

			for (TreatmentDto treatment : provider.getTreatments()) {
				responseBuilder.link(getTreatmentUri(uriInfo, treatment.getPatientId(), treatment.getId()), TREATMENT);
			}
			return responseBuilder.build();
		} catch (ProviderNotFoundException e) {
			logger.info("Provider with id " + id + " not found");
			return Response.status(Status.NOT_FOUND).build();
		} catch (ProviderServiceExn e) {
			logger.log(Level.SEVERE, "Provider with id " + id + " not found", e);
			return Response.status(Status.NOT_FOUND).build();
		} catch(IllegalArgumentException e) {
			logger.info("Bad provider id " + id);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	// TODO

	@POST
	@Consumes("application/json")
	public Response addProvider(ProviderDto providerDto) {
		// TODO Complete this (see addPatient)
		try {
			UUID id = providerService.addProvider(providerDto);
			URI uri = getProviderUri(uriInfo, id);
			return Response.created(uri).build();
		} catch (ProviderServiceExn e) {
			logger.log(Level.SEVERE, "Provider service request failed! ", e);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
	
	// TODO

	/*
	 * Return a provider DTO including the list of treatments they are administering.
	 */
	@GET
	@Path("{id}/treatment/{tid}")
	@Produces("application/json")
	public Response getTreatment(@PathParam("id") String id, @PathParam("tid") String tid) {
		try {
			UUID providerId = UUID.fromString(id);
			UUID treatmentId = UUID.fromString(tid);
			TreatmentDto treatment = providerService.getTreatment(providerId, treatmentId);
			ResponseBuilder responseBuilder = Response.ok(treatment);
			/* 
			 * TODO Add links for patient and provider in response headers.
			 */

			return responseBuilder.build();
		} catch (ProviderServiceExn e) {
			logger.info("Failed to find provider with id "+id);
			return Response.status(Status.NOT_FOUND).build();
		} catch (IllegalArgumentException e) {
			logger.info("Badly formed provider id: "+id);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	// TODO
	@POST
	@Path("{id}/treatment")
	@Consumes("application/json")
	public Response addTreatment(@PathParam("id") String providerId, TreatmentDto treatmentDto) {
		String treatmentProvider = treatmentDto.getProviderId().toString();
		if (!providerId.equals(treatmentProvider)) {
			logger.severe(String.format("Provider in path %s does not match provider in body %s!", providerId, treatmentProvider));
			return Response.status(Status.BAD_REQUEST).build();
		}
		try {
			UUID id = providerService.addTreatment(treatmentDto);
			return Response.created(getTreatmentUri(uriInfo, UUID.fromString(providerId), id)).build();
		} catch (ProviderServiceExn|PatientServiceExn e) {
			logger.log(Level.SEVERE, "Provider service request (addTreatment) failed! ", e);
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

}
