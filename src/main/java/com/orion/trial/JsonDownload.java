//package com.orion.trial;
//
//import com.orion.exception.OrionRegressionException;
//import com.orion.regression.bean.FileTransferBean;
//import com.orion.regression.http.HTTPExecutor;
//
//public class JsonDownload { //set hostname,tokenid,downloadurl
//
//    public static void main(String[] args) throws OrionRegressionException {
//
//        //FileTransferBean set parameters
//        FileTransferBean fileTransferBean = new FileTransferBean ();
//        fileTransferBean.setHostName ( "test01.orionic.com" );
//        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ " );
//        fileTransferBean.setDownloadUrl ("https://test01.orionic.com/harvester/runs/3867"  );
//
//        //HttpExecutor
//        HTTPExecutor.initializeHttpClient ();
//        HTTPExecutor.downloadJSON ( fileTransferBean );
//        HTTPExecutor.destroyHttpClient ();
//    }
//}
//
//
//
//
