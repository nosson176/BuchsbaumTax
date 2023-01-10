package com.buchsbaumtax.app.job;

import com.buchsbaumtax.app.domain.UpdateSmartviews;
import com.buchsbaumtax.core.dao.SmartviewDAO;
import com.buchsbaumtax.core.model.Smartview;
import com.sifradigital.framework.db.Database;
import org.knowm.sundial.Job;
import org.knowm.sundial.annotations.CronTrigger;
import org.knowm.sundial.exceptions.JobInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@CronTrigger(cron = "0 */5 * * * ?")
public class UpdateSmartviewsJob extends Job {

    private static final Logger Log = LoggerFactory.getLogger(UpdateSmartviewsJob.class);

    @Override
    public void doRun() throws JobInterruptException {
        Log.info("Starting Update Smartviews Job...");
        UpdateSmartviews updateSmartviews = new UpdateSmartviews();

        List<Smartview> smartviews = Database.dao(SmartviewDAO.class).getAll();
        for (Smartview smartview : smartviews) {
           updateSmartviews.updateSmartview(smartview);
        }

        Log.info("UpdateSmartviews Job completed.");
    }
}
