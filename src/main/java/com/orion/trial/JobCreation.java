package com.orion.trial;
;
import com.orion.regression.http.HTTPExecutor;
import com.orion.exception.OrionRegressionException;
import com.orion.regression.bean.FileTransferBean;

import java.util.ArrayList;
import java.util.List;

public class JobCreation  {

    public static void main(String[] args) throws  OrionRegressionException {

        //FileTransferBean set parameters
        FileTransferBean fileTransferBean = new FileTransferBean ();
        fileTransferBean.setHostName ( "test01.orionic.com" );
        fileTransferBean.setTokenId ( "mmx-NzrDHZIS3ouTkumlIMYMQ" );
        fileTransferBean.setRunParamsJsonStr
                ("{\"name\":\"job_aa1\",\"tags\":\"gc\",\"steps\":[{\"connection\":\"hello\"}]}");
                //( "{\"name\":\"ssss_job2\",\"tags\":\"gc\",\"steps\":[{\"connection\":\"ssss\"}]}" );
        //HttpExecutor
        HTTPExecutor.initializeHttpClient ();
        HTTPExecutor.createJob ( fileTransferBean );
        HTTPExecutor.destroyHttpClient ();
        List<FileTransferBean> bean_all = new ArrayList<> ();
        bean_all.add(fileTransferBean);

    }
}




