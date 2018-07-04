package com.orion.trial;

import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.http.HTTPExecutor;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FileUpload { //set hostname,tokenid,downloadurl
   // private static final Logger log = Logger.getLogger( com.orion.cli.CliClient.class.getName());

    public static void main(String[] args) throws OrionRegressionException {

        //FileTransferBean set parameters
        FileTransferBean fileTransferBean = new FileTransferBean ();
        fileTransferBean.setHostName ( "test01.orionic.com" );
        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ " );
        fileTransferBean.setJobId ( "2016" );      //java-1925 oracle-1926 pen-1902
        fileTransferBean.setStepId ( "2322" );
        fileTransferBean.setSourceUploadPath
                //("C:\\Users\\IND029\\Desktop\\TestFolders\\OrHarvesterUtil-0.0.1-jar-with-dependencies.jar");
                ( "C:/Users/IND029/eclipse-workspace/Project1/src/task4.java" );
        //HttpExecutor
        HTTPExecutor.initializeHttpClient ();
        HTTPExecutor.uploadFormSourceFile ( fileTransferBean );
        HTTPExecutor.destroyHttpClient ();


    }
}




