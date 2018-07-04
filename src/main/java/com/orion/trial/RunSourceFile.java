package com.orion.trial;

import com.orion.regression.http.HTTPExecutor;
import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;

public class RunSourceFile {

    public static void main(String[] args) throws OrionRegressionException {
        //FileTransferBean set parameters
        FileTransferBean fileTransferBean = new FileTransferBean ();

        fileTransferBean.setHostName ( "test01.orionic.com" );

        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ " );

        fileTransferBean.setJobId ( "2016" );      //java-1925 oracle-1926 pen-1902
        //HttpExecutor
        HTTPExecutor.initializeHttpClient ();

        HTTPExecutor.runSourceFile ( fileTransferBean ); // runs sourcefile successfully (jobid-1925,1926,1902)

        HTTPExecutor.destroyHttpClient ();
    }
}



