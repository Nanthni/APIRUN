package com.orion.trial;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.http.HTTPExecutor;

public class ConnectionDeletion {

    public static void main(String[] args) throws OrionRegressionException {

        //FileTransferBean set parameters
        FileTransferBean fileTransferBean = new FileTransferBean ();

        fileTransferBean.setHostName ( "test01.orionic.com" );

        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ " );

        fileTransferBean.setConnectionName( "aa1" );      //java-1925 oracle-1926 pen-1902

        //HttpExecutor
        HTTPExecutor.initializeHttpClient ();

        HTTPExecutor.deleteConnection ( fileTransferBean ); //deletes Connection successfully

        HTTPExecutor.destroyHttpClient ();
    }
}


