package com;

import elonmeter.csv.jmeter.SequenceNumber;

public class TestClient extends Thread {  
      
    private SequenceNumber sn;  
      
    public TestClient(SequenceNumber sn) {  
        this.sn = sn;  
    }  
      
    public void run() {  
//      ④每个线程打出3个序列值    
        for(int i = 0; i<15; i++) {  
            System.out.println("thread[" + Thread.currentThread().getName() + "] sn[" + sn.getNextNum() +"]");  
        }  
    }  
    public static void main(String [] args) {  
        SequenceNumber sn = new SequenceNumber(5);  
          
//      ③ 3个线程共享sn，各自产生序列号    
        TestClient tc1 = new TestClient(sn);  
        //TestClient tc2 = new TestClient(sn);  
        //TestClient tc3 = new TestClient(sn);  
          
        tc1.start();  
       // tc2.start();  
        //tc3.start();  
          
    }  
}  