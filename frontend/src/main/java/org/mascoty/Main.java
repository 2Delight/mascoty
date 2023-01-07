package org.mascoty;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.sound.sampled.*;

public class Main {

    private static int volume = 0;
    protected static TargetDataLine line = null;


    public static void main(String[] args) {
        try {
            FlatDarculaLaf.setup();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        MainFrame frame = new MainFrame();
    }


    /**
     * VolumeMeter::calculateRMSLevel
     * Calculate the RMS of the raw audio in buffer
     *
     * @param audioData The buffer containing snippet of raw audio data
     * @return int  The RMS value of the buffer
     */
    protected static int calculateRMSLevel(byte[] audioData) {

        long lSum = 0;
        for (int i = 0; i < audioData.length; i++)
            lSum = lSum + audioData[i];

        double dAvg = lSum / audioData.length;

        double sumMeanSquare = 0d;
        for (int j = 0; j < audioData.length; j++)
            sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;
        return (int) (Math.pow(averageMeanSquare, 0.5d) + 0.5) - 50;
    }

}