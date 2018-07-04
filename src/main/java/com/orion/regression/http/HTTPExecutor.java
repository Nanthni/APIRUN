package com.orion.regression.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;

/**
 * HTTP utlity class to perform REST calls
 *
 * @author Vijay Anand
 */
public class HTTPExecutor {
    static final int BUFFER_SIZE = 4096;
    private static final Logger log = Logger.getLogger ( HTTPExecutor.class.getName () );
    /* Create object of CloseableHttpClient */
    static CloseableHttpClient httpClient = null;

    public static void initializeHttpClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault ();
        }
    }

    public static void destroyHttpClient() {
        if (httpClient != null) {
            try {
                httpClient.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
    }

    /**
     * Creates a new connection with defined connection parameters
     *
     * @param bean
     * @throws OrionRegressionException
     */
    public static void createConnection(FileTransferBean bean) {
        try {
            String httpCreateConnUrlTemplate = "https://{0}/harvester/api/connections";
            MessageFormat messageFormat = new MessageFormat ( httpCreateConnUrlTemplate );
            Object[] httpArgs = {bean.getHostName ()};
            String restUrl = messageFormat.format ( httpArgs );
            System.out.println ( "****************" + bean.getConnParamsJsonStr () );
            String jsonResponse = HttpPostExecutor.getInstance ( httpClient, restUrl, bean.getTokenId (), bean.getConnParamsJsonStr () )
                    .execute ();
            System.out.println ( jsonResponse );
            JSONObject jsonObject = (JSONObject) new JSONParser ().parse ( jsonResponse );
            System.out.println ( jsonObject );
            JSONObject jsonObjectConnection = (JSONObject) jsonObject.get ( "connection" );
            System.out.println ( jsonObjectConnection );
            String connectionName = (String) jsonObjectConnection.get ( "name" );
            bean.setConnectionName ( connectionName );
            log.log ( Level.DEBUG, "**************** Connection created : " + connectionName + " ****************" );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static void checkConnection(FileTransferBean bean) {
        try {
            System.out.println ( "\n\n\t\t******** Getting things for connection check  **********" + bean.getConnParamsJsonStr () );
            JSONObject jsonObject = (JSONObject) new JSONParser ().parse ( bean.getConnParamsJsonStr () );
            String connectionName = (String) jsonObject.get ( "name" );
            bean.setConnectionName ( connectionName );
            String httpDeleteConnUrlTemplate = "https://{0}/harvester/api/connections/{1}";
            MessageFormat messageFormat1 = new MessageFormat ( httpDeleteConnUrlTemplate );
            Object[] httpArgs1 = {bean.getHostName (), bean.getConnectionName ()};
            String restUrl1 = messageFormat1.format ( httpArgs1 );
            String jsonResponse1 = HttpGetExecutor.getInstance ( httpClient, restUrl1, bean.getTokenId () ).execute ();
            System.out.println ( jsonResponse1 );
            JSONObject jsonObject1 = (JSONObject) new JSONParser ().parse ( jsonResponse1 );
            String connectionName1 = (String) jsonObject1.get ( "name" );
            bean.setConnectionName ( connectionName1 );
            log.log ( Level.DEBUG, "**************** Connection Name details -> " + bean.getConnectionName () + " ****************" );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public static void createJob(FileTransferBean bean) {
        try {
            String actual_step_id = null;
            String actual_connection_id = null;
            String actual_connection_name = null;
            String upload_actual_step_id = null;
            String upload_actual_connection_id = null;
            String upload_actual_connection_name = null;
            System.out.println ( "\n\n\t\t********   Getting things for job check   **********" + bean.getRunParamsJsonStr () );
            JSONObject jsonObject = (JSONObject) new JSONParser ().parse ( bean.getRunParamsJsonStr () );
            String jobName = (String) jsonObject.get ( "name" );
            bean.setJobName ( jobName );
            String httpCreateJobUrlTemplate = "https://{0}/harvester/api/jobs/{1}";
            MessageFormat messageFormat = new MessageFormat ( httpCreateJobUrlTemplate );
            Object[] httpArgs = {bean.getHostName (), bean.getJobName ()};
            String restUrl = messageFormat.format ( httpArgs );
            String jsonResponse = HttpPostExecutor.getInstance ( httpClient, restUrl, bean.getTokenId (), bean.getRunParamsJsonStr () )
                    .execute ();
            JSONObject jsonObject1 = (JSONObject) new JSONParser ().parse ( jsonResponse );
            JSONObject jsonObjectJob = (JSONObject) jsonObject1.get ( "job" );
            String jobName1 = (String) jsonObjectJob.get ( "name" );
            Long jobId = (Long) jsonObjectJob.get ( "id" );
            bean.setJobName ( jobName1 );
            bean.setJobId ( jobId.toString () );
            JSONArray jsonStepArray = (JSONArray) jsonObjectJob.get ( "steps" );
            Long stepId, connectionId;
            String upload_connection = bean.getActualConnectionName ();

            for (int i = 0; i < jsonStepArray.size (); i++) {

                String connectionName = (String) ((JSONObject) jsonStepArray.get ( i )).get ( "connection_name" );
                bean.setConnectionName ( connectionName );
                connectionName = bean.getConnectionName ();

                stepId = (Long) ((JSONObject) jsonStepArray.get ( i )).get ( "id" );
                bean.setStepId ( stepId.toString () );
                connectionId = (Long) ((JSONObject) jsonStepArray.get ( i )).get ( "connection_id" );
                bean.setConnectionId ( connectionId.toString () );
                if (upload_connection.contentEquals ( connectionName )) {
                    System.out.println ( "^^^^^^^^^^^^^^^^^^^^" );
                    upload_actual_connection_id = bean.getConnectionId ();
                    upload_actual_connection_name = bean.getConnectionName ();
                    upload_actual_step_id = bean.getStepId ();
                }
                log.log ( Level.DEBUG, "**************** Step Id           : " + bean.getStepId () + " ****************" );
                log.log ( Level.DEBUG, "**************** Connection Id     : " + bean.getConnectionId () + " ****************" );
                log.log ( Level.DEBUG, "**************** Connection Name   : " + bean.getConnectionName () + " ****************" );
                System.out.println ( "_____________________________________________________________________________________________________________\n" );
//                bean.setConnectionName ( actual_connection_name );
//                bean.setConnectionId ( actual_connection_id );
//                bean.setStepId ( actual_step_id );
            }
            log.log ( Level.DEBUG, "**************** Job name   : " + bean.getJobName () + " ****************" );
            log.log ( Level.DEBUG, "**************** Job id     : " + bean.getJobId () + " ****************" );
            System.out.println ( "_____________________________________________________________________________________________________________\n" );
            bean.setActualConnectionId ( upload_actual_connection_id );
            bean.setActualConnectionName ( upload_actual_connection_name );
            bean.setActualStepId ( upload_actual_step_id );
            upload_actual_connection_id = bean.getActualConnectionId ();
            upload_actual_connection_name = bean.getActualConnectionName ();
            upload_actual_step_id = bean.getActualStepId ();
            bean.setConnectionName ( upload_actual_connection_name );
            bean.setConnectionId ( upload_actual_connection_id );
            bean.setStepId ( upload_actual_step_id );

        } catch (Exception e) {
            e.printStackTrace ();
        }
    }


    /**
     * Creates a new job with defined runtime parameters.
     *
     * @param bean
     * @throws OrionRegressionException
     */
    public static void checkJob(FileTransferBean bean) {
        try {
            String upload_actual_step_id = null;
            String upload_actual_connection_id = null;
            String upload_actual_connection_name = null;
            System.out.println ( "\n\n****** Getting things for job   **********" + bean.getRunParamsJsonStr () );
            JSONObject jsonObject = (JSONObject) new JSONParser ().parse ( bean.getRunParamsJsonStr () );
            String jobName = (String) jsonObject.get ( "name" );
            bean.setJobName ( jobName );
            //   JSONArray jsonStepArray1 = (JSONArray) jsonObject.get ( "steps" );
            //   String given_name = (String) ((JSONObject) jsonStepArray1.get ( 0 )).get ( "connection" );
            String httpDeleteJobUrlTemplate = "https://{0}/harvester/api/jobs/{1}";
            MessageFormat messageFormat = new MessageFormat ( httpDeleteJobUrlTemplate );
            Object[] httpArgs1 = {bean.getHostName (), bean.getJobName ()};
            String restUrl1 = messageFormat.format ( httpArgs1 );
            String jsonResponse1 = HttpGetExecutor.getInstance ( httpClient, restUrl1, bean.getTokenId () ).execute ();
            System.out.println ( jsonResponse1 );
            JSONObject jsonObject1 = (JSONObject) new JSONParser ().parse ( jsonResponse1 );
            String jobName1 = (String) jsonObject1.get ( "name" );
            bean.setJobName ( jobName1 );
            Long jobId = (Long) jsonObject1.get ( "id" );
            bean.setJobId ( jobId.toString () );
            JSONArray jsonStepArray = (JSONArray) jsonObject1.get ( "steps" );
            Long stepId, connectionId;
            String connectionName;
            String upload_connection = bean.getActualConnectionName ();
            for (int i = 0; i < jsonStepArray.size (); i++) {
                stepId = (Long) ((JSONObject) jsonStepArray.get ( i )).get ( "id" );
                bean.setStepId ( stepId.toString () );
                connectionId = (Long) ((JSONObject) jsonStepArray.get ( i )).get ( "connection_id" );
                bean.setConnectionId ( connectionId.toString () );
                connectionName = (String) ((JSONObject) jsonStepArray.get ( i )).get ( "connection_name" );
                bean.setConnectionName ( connectionName );
                if (upload_connection.contentEquals ( connectionName )) {
                    System.out.println ( "^^^^^^^^^^^^^^^^^^^^" );
                    upload_actual_connection_id = bean.getConnectionId ();
                    upload_actual_connection_name = bean.getConnectionName ();
                    upload_actual_step_id = bean.getStepId ();
                }
                log.log ( Level.DEBUG, "**************** Step Id           : " + bean.getStepId () + " ****************" );
                log.log ( Level.DEBUG, "**************** Connection Id     : " + bean.getConnectionId () + " ****************" );
                log.log ( Level.DEBUG, "**************** Connection Name   : " + bean.getConnectionName () + " ****************" );
                System.out.println ( "_____________________________________________________________________________________________________________\n" );
            }
            log.log ( Level.DEBUG, "**************** Job name   : " + bean.getJobName () + " ****************" );
            log.log ( Level.DEBUG, "**************** Job id     : " + bean.getJobId () + " ****************" );
            System.out.println ( "_____________________________________________________________________________________________________________\n" );
            bean.setActualConnectionId ( upload_actual_connection_id );
            bean.setActualConnectionName ( upload_actual_connection_name );
            bean.setActualStepId ( upload_actual_step_id );
            upload_actual_connection_id = bean.getActualConnectionId ();
            upload_actual_connection_name = bean.getActualConnectionName ();
            upload_actual_step_id = bean.getActualStepId ();
            bean.setConnectionName ( upload_actual_connection_name );
            bean.setConnectionId ( upload_actual_connection_id );
            bean.setStepId ( upload_actual_step_id );
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    /**
     * Removes all repository metadata loaded from connection. Acts the same as
     * Clean repository in the harvester UI.
     *
     * @param bean
     * @throws OrionRegressionException
     */
    public static void cleanRepository(FileTransferBean bean) throws OrionRegressionException {
        try {
            String httpCleanRepoUrlTemplate = "https://{0}/harvester/api/connections/{1}/cleanup";
            MessageFormat messageFormat = new MessageFormat ( httpCleanRepoUrlTemplate );
            Object[] httpArgs = {bean.getHostName (), bean.getConnectionName ()};
            String restUrl = messageFormat.format ( httpArgs );
            HttpPostExecutor.getInstance ( httpClient, restUrl, bean.getTokenId () ).execute ();
            log.log ( Level.DEBUG, "Repository cleaned -> " + bean.getConnectionName () );
        } catch (Exception e) {
            throw new OrionRegressionException ( e );
        }
    }

    /**
     * Deletes a job.
     *
     * @param bean
     * @throws OrionRegressionException
     */
    public static void deleteJob(FileTransferBean bean) throws OrionRegressionException {
        try {
            String httpDeleteJobUrlTemplate = "https://{0}/harvester/api/jobs/{1}";
            MessageFormat messageFormat = new MessageFormat ( httpDeleteJobUrlTemplate );
            Object[] httpArgs = {bean.getHostName (), bean.getJobName ()};
            String restUrl = messageFormat.format ( httpArgs );
            HttpDeleteExecutor.getInstance ( httpClient, restUrl, bean.getTokenId () ).execute ();
            log.log ( Level.DEBUG, "**************** Job deleted -> " + bean.getJobName () + " ****************" );
        } catch (Exception e) {
            throw new OrionRegressionException ( e );
        }
    }

    /**
     * Deletes an existing connection
     *
     * @param bean
     * @throws OrionRegressionException
     */
    public static void deleteConnection(FileTransferBean bean) throws OrionRegressionException {
        try {
            String httpDeleteConnUrlTemplate = "https://{0}/harvester/api/connections/{1}";
            MessageFormat messageFormat = new MessageFormat ( httpDeleteConnUrlTemplate );
            Object[] httpArgs = {bean.getHostName (), bean.getConnectionName ()};
            String restUrl = messageFormat.format ( httpArgs );
            HttpDeleteExecutor.getInstance ( httpClient, restUrl, bean.getTokenId () ).execute ();
            log.log ( Level.DEBUG, "**************** Connection deleted -> " + bean.getConnectionName () + " ****************" );
        } catch (Exception e) {
            throw new OrionRegressionException ( e );
        }
    }

    /**
     * Upload a source file to a job step using POST parameters : id, step_id, file
     *
     * @param bean
     * @throws OrionRegressionException
     */
    public static void uploadFormSourceFile(FileTransferBean bean) throws OrionRegressionException {
        try {
            System.out.println ( "\t\t\t\t Upload Source Files " );

            System.out.println ( "Connection Name To Upload Files : " + bean.getConnectionName () );
            System.out.println ( "Connection Id                   : " + bean.getConnectionId () );
            System.out.println ( "Job Name                        : " + bean.getJobName () );
            System.out.println ( "Job Id                          : " + bean.getJobId () );
            System.out.println ( "Step Id                         : " + bean.getStepId () );

            String httpUploadUrlTemplate = "https://{0}/harvester/api/jobs/{1}/upload";
            MessageFormat messageFormat = new MessageFormat ( httpUploadUrlTemplate );
            Object[] httpArgs = {bean.getHostName (), bean.getJobId ()};
            String urlStr = messageFormat.format ( httpArgs );
            File fileToUpload = new File ( bean.getSourceUploadPath () );
            String charset = "UTF-8";
            MultiPartUtility multipart = new MultiPartUtility ( urlStr, bean.getTokenId (), charset );
            multipart.addFormField ( "id", bean.getJobId () );
            multipart.addFormField ( "step_id", bean.getStepId () );
            multipart.addFilePart ( "file", fileToUpload );

            List <String> response = multipart.finish ();
            log.log ( Level.DEBUG, "SERVER REPLIED:" );
            for (String line : response) {
                log.log ( Level.DEBUG, line );
            }
            log.log ( Level.DEBUG, "**************** File uploaded using REST API -> " + urlStr + " ****************" );
        } catch (Exception e) {
            throw new OrionRegressionException ( e );
        }
    }

    /**
     * Starts a Job run
     *
     * @param bean
     * @return
     * @throws OrionRegressionException
     */
    public static String runSourceFile(FileTransferBean bean) throws OrionRegressionException {
        String runStatusUrlTemplate = "https://{0}/harvester/api/jobs/{1}/run";
        MessageFormat messageFormat = new MessageFormat ( runStatusUrlTemplate );
        Object[] httpArgs = {bean.getHostName (), bean.getJobId ()};
        String restUrl = messageFormat.format ( httpArgs );
        String response = HttpPostExecutor.getInstance ( httpClient, restUrl, bean.getTokenId () ).execute ();
        log.log ( Level.DEBUG, "**************** Job Run invoked using REST API -> " + restUrl + " ****************" );
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) new JSONParser ().parse ( response );
        } catch (ParseException e) {
            throw new OrionRegressionException ( e );
        }
        Long runId = (Long) jsonObject.get ( "run_id" );
        return runId.toString ();
    }

    /**
     * Gets the status of specific run and returns true if the status is success,
     * otherwise it returns false
     *
     * @param bean
     * @param runId
     * @return
     * @throws OrionRegressionException
     */
    public static boolean isStillRunning(FileTransferBean bean, String runId) throws OrionRegressionException {
        String runStatusUrlTemplate = "https://{0}/harvester/api/runs/{1}";
        MessageFormat messageFormat = new MessageFormat ( runStatusUrlTemplate );
        Object[] httpArgs = {bean.getHostName (), runId};
        String restUrl = messageFormat.format ( httpArgs );
        log.log ( Level.DEBUG, "**************** Checking whether Job still running with REST API -> " + restUrl + " ****************" );
        String response = HttpGetExecutor.getInstance ( httpClient, restUrl, bean.getTokenId () ).execute ();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) new JSONParser ().parse ( response );
        } catch (ParseException e) {
            throw new OrionRegressionException ( e );
        }
        String runStatus = (String) jsonObject.get ( "status" );
        if ("S".equals ( runStatus )) {
            log.log ( Level.DEBUG, "**************** Job at " + bean.getHostName () + " ran successfully using runId ->" + runId + " ****************" );
            return false;
        } else {
            return true;
        }
    }

    /**
     * Downloads the Object/Relational JSON for a given connection
     *
     * @param bean
     * @return
     * @throws OrionRegressionException
     */
    public static String downloadJSON(FileTransferBean bean) throws OrionRegressionException {
        StringBuffer jsonStrBuffer = null;
        try {
            log.log ( Level.DEBUG, "Downloading JSON using REST API -> " + bean.getDownloadUrl () );
            URL object = new URL ( bean.getDownloadUrl () );
            HttpURLConnection con = (HttpURLConnection) object.openConnection ();
            con.setDoInput ( true );
            con.setRequestMethod ( "GET" );
            con.setRequestProperty ( "Content-Type", "application/json; charset=UTF-8" );
            con.setRequestProperty ( "Accept", "application/json" );
            con.setRequestProperty ( "Authorization", "Token token=" + bean.getTokenId () );

            int HttpResult = con.getResponseCode ();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader ( new InputStreamReader ( con.getInputStream (), "utf-8" ) );
                jsonStrBuffer = new StringBuffer ();
                String line = null;
                while ((line = br.readLine ()) != null) {
                    jsonStrBuffer.append ( line + "\n" );
                    // DownloadBar.update(jsonStrBuffer.toString().getBytes().length);
                }
                br.close ();
                System.out.flush ();
                log.log ( Level.DEBUG, "JSON download completed !							" );
                /* if(bean.isJSONInMemory()) { */
                return jsonStrBuffer.toString ();
                /*
                 * }else { FileWriter filewriter = new FileWriter(bean.getJsonDownloadPath());
                 * BufferedWriter outputStream = new BufferedWriter(filewriter);
                 * outputStream.write(jsonStrBuffer.toString()); outputStream.flush();
                 * outputStream.close(); }
                 */
            } else {
                log.log ( Level.DEBUG, con.getResponseMessage () );
            }
        } catch (Exception e) {
            throw new OrionRegressionException ( e );
        }
        return null;
    }

    /*
     * static String getCreateConnJsonParams() { JSONObject advparams = new
     * JSONObject(); advparams.put("files_dbhost", "DDL");
     * advparams.put("defaultSchema", "TEMP"); advparams.put("files_importFileMask",
     * "sql,ddl"); advparams.put("files_dbname", "DB");
     *
     * JSONObject params = new JSONObject(); params.put("connector_id", "oracle");
     * params.put("mode", "files"); params.put("name",
     * "VJ_ORA_CONN_AUTO_"+(++counter));
     *
     * params.put("parameters", advparams); String jsonData = params.toString();
     * return jsonData; }
     */

    /*
     * static String getCreateJobJsonParams() { JSONObject params = new
     * JSONObject(); params.put("name", "VJ_ORA_JOB_AUTO_"+counter);
     * params.put("tags", "Oracle");
     *
     * JSONObject step = new JSONObject(); step.put("connection",
     * "VJ_ORA_CONN_AUTO_"+counter); step.put("integration_mode", "HISTORY");
     * step.put("job_options", "ANTLR");
     *
     * JSONArray steps = new JSONArray(); steps.add(step);
     *
     * params.put("steps", steps); String jsonData = params.toString(); return
     * jsonData; }
     */
}
