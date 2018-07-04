package com.orion.trial;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.http.HTTPExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Complete_Creation_nodeletion {

    private static final Logger log = Logger.getLogger ( Complete_Creation_nodeletion.class.getName () );

    public static void main(String[] args) throws Exception {
        try {
            HTTPExecutor.initializeHttpClient ();
            FileTransferBean bean1 = initializeNextBean ();

            log.log ( Level.INFO, "************ Performing Harvester Setup...************  " );
            String nextRunId = performHarvesterSetup ( bean1 );
            System.out.println ( nextRunId );

        } catch (OrionRegressionException ore) {
            log.log ( Level.ERROR, "************OrionRegressionException ->" + ore );
            ore.printStackTrace ();
        } finally {
            HTTPExecutor.destroyHttpClient ();
        }
    }

    /**
     * Extract regression parameters for "Next" Harvester and initializes the bean
     *
     * @return
     * @throws Exception
     */
    private static FileTransferBean initializeNextBean() throws Exception {
        FileTransferBean bean1 = new FileTransferBean ();
        BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader ( System.in ) );

        System.out.println ( "Enter the Host name : " );
        String hostname = bufferedReader.readLine ();
        System.out.println ( "Enter the Token id :" );
        String tokenid = bufferedReader.readLine ();
        System.out.println ( "******Connection Creation*******" );
        System.out.println ( "Enter the connection parameters(json) :" );
        String conn = bufferedReader.readLine ();
        System.out.println ( "******Job Creation*******" );
        System.out.println ( "Enter the job parameters(json) :" );
        String jobb = bufferedReader.readLine ();
        System.out.println ( "******Upload SourceFiles*******" );
        System.out.println ( "Enter the path of files to upload :" );
        String filepath = bufferedReader.readLine ();

        bean1.setTokenId ( tokenid );
        bean1.setHostName ( hostname );
        bean1.setConnParamsJsonStr ( conn );
        bean1.setRunParamsJsonStr ( jobb );

//        bean1.getConnectionName ();
//        bean1.getJobName ();
//        bean1.getJobId ();
        bean1.setSourceUploadPath ( filepath );

        return bean1;
    }

    public static String performHarvesterSetup(FileTransferBean bean) throws OrionRegressionException {
        log.log ( Level.INFO, "Invoking Connection creation at " + bean.getHostName () );
        HTTPExecutor.createConnection ( bean );
        log.log ( Level.INFO, "Invoking Job creation at " + bean.getHostName () );
        HTTPExecutor.createJob ( bean );
        log.log ( Level.INFO, "Invoking source upload at " + bean.getHostName () );
        HTTPExecutor.uploadFormSourceFile ( bean );
        log.log ( Level.INFO, "Invoking job run at " + bean.getHostName () );
        String runId = HTTPExecutor.runSourceFile ( bean );
        HTTPExecutor.isStillRunning ( bean, runId );
        return runId;
    }

}


