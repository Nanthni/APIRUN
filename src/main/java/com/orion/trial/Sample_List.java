//package com.orion.trial;
//import com.orion.regression.bean.FileTransferBean;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Consumer;
//public class Sample_List {
//
//
//
//    public static void main(String[] args) {
//
//        List<FileTransferBean> bean_all = new ArrayList<>();
//        bean_all.add(fileTransferBean);
//        bean_all.add();
//        bean_all.add();
//        bean_all.add();
//
//        Consumer<Object> consumer = new Sample_List().new MyConsumer();
//
//        bean_all.forEach(consumer);
//
//        //lambda style
//        bean_all.forEach(x -> {System.out.println("Processed "+x);});
//
//    }
//
//    class MyConsumer implements Consumer<Object>{
//
//        @Override
//        public void accept(Object t) {
//            System.out.println("Processing "+t);
//        }
//
//    }
//}
