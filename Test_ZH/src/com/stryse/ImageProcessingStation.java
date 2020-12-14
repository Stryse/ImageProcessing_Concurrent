package com.stryse;

import com.stryse.Tools.IMG;
import java.util.concurrent.BlockingQueue;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;


public class ImageProcessingStation implements Runnable {

    private final BlockingQueue<IMG> input;
    private final BlockingQueue<IMG> output;
    private final Consumer<IMG> func;
    private final BooleanSupplier endCondition;
    private boolean stopped = false;

    public ImageProcessingStation(BlockingQueue<IMG> input,
                                  BlockingQueue<IMG> output,
                                  Consumer<IMG>      func,
                                  BooleanSupplier    endCondition)
    {
        this.input = input;
        this.output = output;
        this.func = func;
        this.endCondition = endCondition;
    }

    @Override
    public void run() {
        stopped = false;
        try{
            while(!endCondition.getAsBoolean() && !stopped)
            {
                //PROCESS
                IMG img = input.take();
                func.accept(img);

                //FORWARD TO OUTPUT IF POSSIBLE
                if(output != null)
                    output.put(img);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Stop(){
        this.stopped = true;
    }
}
