package com.buchsbaumtax.app.domain.taxyear;

import com.buchsbaumtax.app.dto.BaseResponse;
import com.buchsbaumtax.core.dao.TaxYearDAO;
import com.buchsbaumtax.core.model.TaxYear;
import com.sifradigital.framework.db.Database;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class DeleteTaxYear {

    public BaseResponse deleteTaxYear(int taxYearId) {
        try {
            // Check if the TaxYear exists before attempting to delete
            TaxYear taxYear = Database.dao(TaxYearDAO.class).get(taxYearId);
            if (taxYear == null) {
                // Return error if no such TaxYear exists
                return new BaseResponse("Error", "TaxYear not found", Response.Status.NOT_FOUND.getStatusCode());
            }

            // Proceed with deletion if found
            Database.dao(TaxYearDAO.class).delete(taxYearId);

            // Return success response
            return new BaseResponse("Success", "TaxYear deleted successfully", Response.Status.OK.getStatusCode());

        } catch (Exception e) {
            // Log the error (optional)
            e.printStackTrace();

            // Return failure response if exception occurs
            return new BaseResponse("Error", "Failed to delete TaxYear: " + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }
}
