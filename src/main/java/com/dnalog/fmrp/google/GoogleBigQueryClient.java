package com.dnalog.fmrp.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andrejus
 * Date: 01/07/2013
 * Time: 00:10
 * To change this template use File | Settings | File Templates.
 */
public class GoogleBigQueryClient {

    private static final HttpTransport TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public static Bigquery createClient(Credential credential) throws Exception {

        return new Bigquery.Builder(TRANSPORT, JSON_FACTORY, new GoogleHttpRequestDisableTimeout())
                .setHttpRequestInitializer(credential)
                .setApplicationName("Your User Agent Here")
                .build();
    }

    public static JobReference startQuery(Bigquery bigquery, String projectId, String querySql) throws IOException {

        System.out.format("\nInserting Query Job: %s\n", querySql);

        Job job = new Job();
        JobConfiguration config = new JobConfiguration();
        JobConfigurationQuery queryConfig = new JobConfigurationQuery();
        config.setQuery(queryConfig);

        job.setConfiguration(config);
        queryConfig.setQuery(querySql);

        Bigquery.Jobs.Insert insert = bigquery.jobs().insert(projectId, job);
        insert.setProjectId(projectId);
        JobReference jobId = insert.execute().getJobReference();

        System.out.format("\nJob ID of Query Job is: %s\n", jobId.getJobId());

        return jobId;
    }

    public static Job checkQueryResults(Bigquery bigquery, String projectId, JobReference jobId)
            throws IOException, InterruptedException {
        // Variables to keep track of total query time
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (true) {
            Job pollJob = bigquery.jobs().get(projectId, jobId.getJobId()).execute();
            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.format("Job status (%dms) %s: %s\n", elapsedTime,
                    jobId.getJobId(), pollJob.getStatus().getState());
            if (pollJob.getStatus().getState().equals("DONE")) {
                return pollJob;
            }
            // Pause execution for one second before polling job status again, to
            // reduce unnecessary calls to the BigQUery API and lower overall
            // application bandwidth.
            Thread.sleep(1000);
        }
    }

    public static void displayQueryResults(Bigquery bigquery,
                                            String projectId, Job completedJob) throws IOException {
        GetQueryResultsResponse queryResult = bigquery.jobs()
                .getQueryResults(
                        projectId, completedJob
                        .getJobReference()
                        .getJobId()
                ).execute();
        List<TableRow> rows = queryResult.getRows();
        System.out.print("\nQuery Results:\n------------\n");
        for (TableRow row : rows) {
            for (TableCell field : row.getF()) {
                System.out.printf("%-20s", field.getV());
            }
            System.out.println();
        }
    }
}
