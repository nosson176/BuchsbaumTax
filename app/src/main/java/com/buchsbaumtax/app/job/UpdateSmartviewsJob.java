package com.buchsbaumtax.app.job;

import com.buchsbaumtax.app.domain.UpdateSmartviews;
import org.knowm.sundial.Job;
import org.knowm.sundial.annotations.CronTrigger;
import org.knowm.sundial.exceptions.JobInterruptException;

@CronTrigger(cron = "0 */5 * * * ?")
public class UpdateSmartviewsJob extends Job {
    @Override
    public void doRun() throws JobInterruptException {
        new UpdateSmartviews().updateClients();
    }
}
