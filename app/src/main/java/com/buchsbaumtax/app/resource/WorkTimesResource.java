package com.buchsbaumtax.app.resource;

import com.buchsbaumtax.app.domain.WorkTimesCRUD;
import com.buchsbaumtax.core.model.WorkTimes;
import com.sifradigital.framework.auth.Authenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Authenticated
@Path("/worktimes")
public class WorkTimesResource {
    private static final Logger logger = LoggerFactory.getLogger(WorkTimesResource.class);

    private final WorkTimesCRUD workTimesCRUD = new WorkTimesCRUD();

    @POST
    public WorkTimes createWorkTimes(WorkTimes workTimes) {
        return workTimesCRUD.create(workTimes);
    }

    @GET
    public List<WorkTimes> getAllWorkTimes() {
        return workTimesCRUD.getAll();
    }

    @POST
    @Path("/clockin")
    public Response clockIn(WorkTimes workTimes) {
        try {
            WorkTimes createdWorkTimes = workTimesCRUD.clockIn(workTimes.getUserId(), workTimes.getUsername());
            return Response.status(Response.Status.CREATED).entity(createdWorkTimes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/clockout")
    public Response clockOut(WorkTimes workTimes) {
        try {
            WorkTimes updatedWorkTimes = workTimesCRUD.clockOut(workTimes.getUserId(), workTimes.getDate());
            return Response.ok(updatedWorkTimes).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

//    @PUT
//    public WorkTimes updateWorkTimes(WorkTimes workTimes) {
//        return workTimesCRUD.update(workTimes.getId(), workTimes);
//    }

    @PUT
    @Path("/{workTimesId}")
    public WorkTimes updateWorkTimes(@PathParam("workTimesId") int workTimesId, WorkTimes workTimes) {
        return workTimesCRUD.update(workTimesId, workTimes);
    }

    @GET
    @Path("/date/{date}")
    public List<WorkTimes> getAllByDate(@PathParam("date") long date) {
        return workTimesCRUD.getAllByDate(date);
    }

    @GET
    @Path("/user/{userId}")
    public List<WorkTimes> getAllByUserId(@PathParam("userId") int userId) {
        return workTimesCRUD.getAllByUserId(userId);
    }

    @GET
    @Path("/username/{fullName}")
    public List<WorkTimes> getAllByFullName(@PathParam("fullName") String username) {
        return workTimesCRUD.getAllByFullName(username);
    }

    @GET
    @Path("/{id}")
    public WorkTimes getById(@PathParam("id") int id) {
        return workTimesCRUD.getById(id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteWorkTimes(@PathParam("id") int id) {
        workTimesCRUD.delete(id);
    }

    // New endpoint to get work times by userId and date range
    @GET
    @Path("/user/{userId}/date-range")
    public Response getWorkTimesByUserIdAndDateRange(
            @PathParam("userId") int userId,
            @QueryParam("startDate") long startDate,
            @QueryParam("endDate") long endDate) {
        try {
            List<WorkTimes> workTimesList = workTimesCRUD.getWorkTimesByUserIdAndDateRange(userId, startDate, endDate);
            if (workTimesList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No work times found for the given user and date range").build();
            }
            return Response.ok(workTimesList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving work times").build();
        }
    }

    @GET
    @Path("/date-range")
    public Response getWorkTimesByDateRange(
            @QueryParam("startRange") long startRange,
            @QueryParam("endRange") long endRange) {
        try {
            List<WorkTimes> workTimesList = workTimesCRUD.getWorkTimesByDateRange(startRange, endRange);
            if (workTimesList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No work times found for the given date range").build();
            }
            return Response.ok(workTimesList).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving work times").build();
        }
    }
}
