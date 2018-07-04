package com.orion.trial;

import com.orion.regression.http.HTTPExecutor;
import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;

public class RunStatus {

    public static void main(String[] args) throws OrionRegressionException {
        //FileTransferBean set parameters
        FileTransferBean fileTransferBean = new FileTransferBean ();
        fileTransferBean.setHostName ( "test01.orionic.com" );
        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ" );

        //HttpExecutor
        HTTPExecutor.initializeHttpClient ();
        HTTPExecutor.isStillRunning ( fileTransferBean,"3974" );
        //runs successfully (java-25-3867)(pentaho-1902-3868)(oracle-1926-3869)
        HTTPExecutor.destroyHttpClient ();
    }
}




