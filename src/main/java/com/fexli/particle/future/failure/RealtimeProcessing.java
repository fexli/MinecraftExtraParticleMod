package com.fexli.particle.future.failure;

import com.fexli.particle.future.JyProcessing;

import java.io.IOException;

public class RealtimeProcessing implements RealtimeProcessInterface {
    private final String targ;
    /**
     * @param args
     */
    private AbstractRealtimeProcess mRealtimeProcess = null;
    public RealtimeProcessing(String target){
        this.targ = target;
    }
    public void create(){
        mRealtimeProcess = new AbstractRealtimeProcess(this);
        //mRealtimeProcess.setDirectory("");
        mRealtimeProcess.setCommand("python "+ JyProcessing.pyRootDir+this.targ);
        try {
            mRealtimeProcess.start();
        } catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(mRealtimeProcess.getAllResult());
    }
    public void onNewStdoutListener(String newStdout) {
        // TODO Auto-generated method stub
        System.out.println("==>STDOUT  >  " + newStdout);

    }
    public void onNewStderrListener(String newStderr) {
        // TODO Auto-generated method stub
        System.out.println("==>STDERR  >  " + newStderr);
    }
    public void onProcessFinish(int resultCode) {
        // TODO Auto-generated method stub
        System.out.println("==>RESULT_CODE  >  " + resultCode);
    }
}
