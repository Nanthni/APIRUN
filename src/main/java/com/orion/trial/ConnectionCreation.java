package com.orion.trial;

import com.orion.regression.http.HTTPExecutor;
import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;

public class ConnectionCreation  {

    private static String name;

    public static void main(String[] args) throws OrionRegressionException {

        //FileTransferBean set parameters
        FileTransferBean fileTransferBean = new FileTransferBean ();
        fileTransferBean.setHostName ( "test01.orionic.com" );
        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ" );
        fileTransferBean.setConnParamsJsonStr
("{\"connector_id\":\"graphml\",\"name\":\"aa1" +
        "" +
        "" +
        "" +
        "" +
        "\",\"parameters\":{\"fileExtensions\":\"graphml\"}}");
       // ("{\"connector_id\":\"java\",\"name\":\"ssss\",\"parameters\":{\"srvname\":\"Java\",\"projname\":\"Java-Project\",\"options\":\"CREATE\",\"dbhost\":\"RDB\",\"dbname\":\"DB\",\"defaultSchema\":\"TEMP\",\"excludePathMask\":\"test/java,test/resources,target/classes,__MACOSX\"}}");
        //HttpExecutor
        HTTPExecutor.initializeHttpClient ();
        HTTPExecutor.createConnection ( fileTransferBean );
    }
}



