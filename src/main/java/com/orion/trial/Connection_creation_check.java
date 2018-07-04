package com.orion.trial;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.http.HTTPExecutor;

public class Connection_creation_check {
    public static void main(String[] args) throws OrionRegressionException {

        FileTransferBean fileTransferBean = new FileTransferBean ();
        fileTransferBean.setHostName ( "test01.orionic.com" );
        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ" );
        fileTransferBean.setConnParamsJsonStr
                ( "{\"connector_id\":\"graphml\",\"name\":\"aa1" + "\",\"parameters\":{\"fileExtensions\":\"graphml\"}}" );

        HTTPExecutor.initializeHttpClient ();
        HTTPExecutor.checkConnection ( fileTransferBean );
        System.out.println ( "here            " + fileTransferBean.getConnectionName () );

    }
}