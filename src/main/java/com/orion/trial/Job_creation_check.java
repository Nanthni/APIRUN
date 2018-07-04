package com.orion.trial;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.http.HTTPExecutor;
import com.orion.regression.http.HttpGetExecutor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.simple.parser.ParseException;

public class Job_creation_check {
    static CloseableHttpClient httpClient = null;

    public static void main(String[] args) throws OrionRegressionException, ParseException {

        FileTransferBean fileTransferBean = new FileTransferBean ();
        fileTransferBean.setHostName ( "test01.orionic.com" );
        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ" );
        fileTransferBean.setJobName ( "s_job" );
        fileTransferBean.setConnectionName ( "ssss" );
        fileTransferBean.setConnParamsJsonStr
                ( "{\"connector_id\":\"graphml\",\"name\":\"aa1" + "\",\"parameters\":{\"fileExtensions\":\"graphml\"}}" );
        fileTransferBean.setRunParamsJsonStr
                ( "{\"name\":\"s_job\",\"tags\":\"gc\",\"steps\":[{\"connection\":\"connection_pdi_1\"},{ \"connection\":\"ssss\"},{ \"connection\":\"connection_pdi_2\"}]}" );
        // ( "{\"name\":\"s_job\",\"tags\":\"gc\",\"steps\":[{\"connection\":\"aa1\"}]}" );
        fileTransferBean.setSourceUploadPath ( "C:/Users/IND029/Desktop/task4.java" );
        fileTransferBean.setActualConnectionName ( "connection_pdi_1" );
        HTTPExecutor.initializeHttpClient ();
        HTTPExecutor.checkJob ( fileTransferBean );
        // HTTPExecutor.createJob ( fileTransferBean );
        HTTPExecutor.uploadFormSourceFile ( fileTransferBean );
        HTTPExecutor.destroyHttpClient ();
    }
}
