package com.orion.trial;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.http.HTTPExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Complete_Creation_with_Checkup {

    private static final Logger log = Logger.getLogger ( com.orion.trial.Complete_Creation_nodeletion.class.getName () );

    public static void main(String[] args) throws Exception {
        try {
            HTTPExecutor.initializeHttpClient ();
            FileTransferBean bean1 = initializeNextBean ();
            log.log ( Level.INFO, "************ Performing Harvester Checkup...************  " );
            performHarvesterCheckup ( bean1 );
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
        System.out.println ( "Enter the connection name to upload files :" );
        String connectionpath = bufferedReader.readLine ();

        bean1.setTokenId ( tokenid );
        bean1.setHostName ( hostname );
        bean1.setConnParamsJsonStr ( conn );
        bean1.setRunParamsJsonStr ( jobb );
        bean1.setSourceUploadPath ( filepath );
        bean1.setActualConnectionName ( connectionpath );

        return bean1;
    }

    public static void performHarvesterCheckup(FileTransferBean bean)  {
        log.log ( Level.INFO, "\n\n\t\t_______________________________________Invoking Connection creation check up at " + bean.getHostName () );
        HTTPExecutor.checkConnection ( bean );
        log.log ( Level.INFO, "\n\n\t\t_______________________________________Invoking Job creation check up at " + bean.getHostName () );
        HTTPExecutor.checkJob ( bean );
    }

    public static String performHarvesterSetup(FileTransferBean bean) throws OrionRegressionException {
        log.log ( Level.INFO, "\n\n\t\t_______________________________________Invoking Connection creation at " + bean.getHostName () );
        HTTPExecutor.createConnection ( bean );
        log.log ( Level.INFO, "\n\n\t\t_______________________________________Invoking Job creation at " + bean.getHostName () );
        HTTPExecutor.createJob ( bean );
        log.log ( Level.INFO, "\n\n\t\t_______________________________________Invoking source upload at " + bean.getHostName () );
        HTTPExecutor.uploadFormSourceFile ( bean );
        log.log ( Level.INFO, "\n\n\t\t_______________________________________Invoking job run at " + bean.getHostName () );
        String runId = HTTPExecutor.runSourceFile ( bean );
        HTTPExecutor.isStillRunning ( bean, runId );
        return runId;
    }

}
