package com.orion.trial;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.http.HTTPExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Complete_Creation_Switch {

    private static final Logger log = Logger.getLogger ( Complete_Creation_nodeletion.class.getName () );

    public static void main(String[] args) throws Exception {
        try {
            String a = null;
            HTTPExecutor.initializeHttpClient ();
            FileTransferBean bean;
            bean = initializeBeanStartup ();
            do {
                System.out.println ( "____________________________" );
                System.out.println ( " Choose your option" );
                System.out.println ( "____________________________" );
                System.out.println ( "1. Create Connection\t\t 2. Create Job\t\t 3. Check Connection\t\t 4. Check Job\n" );
                System.out.println ( "5. Upload Source Files\t\t 6. Run job\n" );
                System.out.println ( "7. Delete Job   \t\t\t 8. Delete Connection\t\t\t\t             9. Clean Repository" );
                System.out.println ( "10. Complete process for one step" );
                System.out.println ( "____________________________" );
                System.out.println ( " Enter your choice : " );
                System.out.println ( "____________________________" );
                BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader ( System.in ) );
                String choice = bufferedReader.readLine ();

                switch (choice) {
                    case "1": {
                        System.out.println ( "\t******\tConnection Creation\t*******\t" );
                        System.out.println ( "Enter the Connection parameters(json) :" );
                        String con = bufferedReader.readLine ();
                        bean.setConnParamsJsonStr ( con );
                        HTTPExecutor.createConnection ( bean );
                        HTTPExecutor.checkConnection ( bean );
                        break;
                    }
                    case "2": {
                        System.out.println ( "\t******\tJob Creation\t*******\t" );
                        System.out.println ( "Enter the job parameters(json) :" );
                        String job = bufferedReader.readLine ();
                        bean.setRunParamsJsonStr ( job );
                        System.out.println ( "Enter the connection name to upload files :" );
                        String connectionpath = bufferedReader.readLine ();
                        bean.setActualConnectionName ( connectionpath );
                        HTTPExecutor.checkJob ( bean );
                        HTTPExecutor.createJob ( bean );
                        break;
                    }
                    case "3": {
                        System.out.println ( "******Connection Checkup*******" );
                        System.out.println ( "Enter the Connection parameters(json) :" );
                        String con = bufferedReader.readLine ();
                        bean.setConnParamsJsonStr ( con );
                        HTTPExecutor.checkConnection ( bean );
                        break;
                    }

                    case "4": {
                        System.out.println ( "******Job Checkup*******" );
                        System.out.println ( "Enter the job parameters(json) :" );
                        String job = bufferedReader.readLine ();
                        bean.setRunParamsJsonStr ( job );
                        System.out.println ( "Enter the connection name to upload files :" );
                        String connectionpath = bufferedReader.readLine ();
                        bean.setActualConnectionName ( connectionpath );
                        HTTPExecutor.checkJob ( bean );
                        break;
                    }
                    case "5": {
                        System.out.println ( "Enter the job parameters(json) :" );
                        String job = bufferedReader.readLine ();
                        bean.setRunParamsJsonStr ( job );
                        System.out.println ( "Enter the source path to upload files :" );
                        String path = bufferedReader.readLine ();
                        bean.setSourceUploadPath ( path );
                        System.out.println ( "Enter the connection name to upload files :" );
                        String connectionpath = bufferedReader.readLine ();
                        bean.setActualConnectionName ( connectionpath );
                        HTTPExecutor.checkJob ( bean );
                        HTTPExecutor.uploadFormSourceFile ( bean );
                        break;
                    }
                    case "6": {
                        System.out.println ( "Enter the job parameters(json) :" );
                        String job = bufferedReader.readLine ();
                        bean.setRunParamsJsonStr ( job );
                        HTTPExecutor.checkJob ( bean );
                        String runId = HTTPExecutor.runSourceFile ( bean );
                        HTTPExecutor.isStillRunning ( bean, runId );
                        break;
                    }
                    case "7": {
                        System.out.println ( "Enter the job name to delete :" );
                        String job = bufferedReader.readLine ();
                        bean.setJobName ( job );
                        HTTPExecutor.deleteJob ( bean );
                        break;
                    }
                    case "8": {
                        System.out.println ( "Enter the connection name to delete :" );
                        String connection = bufferedReader.readLine ();
                        bean.setConnectionName ( connection );
                        HTTPExecutor.deleteConnection ( bean );
                        break;
                    }

                    case "9": {
                        System.out.println ( "Enter the connection name to delete :" );
                        String connection = bufferedReader.readLine ();
                        bean.setConnectionName ( connection );
                        HTTPExecutor.cleanRepository ( bean );
                        break;
                    }


                    default:
                        System.out.println ( "*********complete*************" );
                        FileTransferBean bean1 = initializeNextBean ();
                        log.log ( Level.INFO, "************ Performing Harvester Checkup...************  " );
                        performHarvesterCheckup ( bean1 );
                        log.log ( Level.INFO, "************ Performing Harvester Setup...************  " );
                        String nextRunId = performHarvesterSetup ( bean1 );
                        System.out.println ( nextRunId );
                }
                System.out.println ( "Continue(Y/N)" );
                a = bufferedReader.readLine ();
            } while ((a.contentEquals ( "Y" )) || (a.contentEquals ( "y" )));
        } catch (OrionRegressionException ore) {
            log.log ( Level.ERROR, "************OrionRegressionException ->" + ore );
            ore.printStackTrace ();
        } finally {
            HTTPExecutor.destroyHttpClient ();
        }

    }


    private static FileTransferBean initializeBeanStartup() throws Exception {
        FileTransferBean bean1 = new FileTransferBean ();
        BufferedReader bufferedReader = new BufferedReader ( new InputStreamReader ( System.in ) );

        System.out.println ( "Enter the Host name : " );
        String hostname = bufferedReader.readLine ();
        System.out.println ( "Enter the Token id :" );
        String tokenid = bufferedReader.readLine ();

        bean1.setTokenId ( tokenid );
        bean1.setHostName ( hostname );

        return bean1;
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

    public static void performHarvesterCheckup(FileTransferBean bean) {
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
