package SevenZip.Compression.LZMA;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EncoderWrapper {

    class Config{
        int algorithm;
        int dictionarySize;
        int numFastBytes;
        int matchFinder;
        int lc;
        int lp;
        int pb;
        boolean endMarkerMode;

        public Config(int algorithm,
                      int dictionarySize,
                      int numFastBytes,
                      int matchFinder,
                      int lc, int lp, int pb,
                      boolean endMarkerMode) {
            this.algorithm = algorithm;
            this.dictionarySize = dictionarySize;
            this.numFastBytes = numFastBytes;
            this.matchFinder = matchFinder;
            this.lc = lc;
            this.lp = lp;
            this.pb = pb;
            this.endMarkerMode = endMarkerMode;
        }
    }

    private Encoder encoder;
    private Config defaultConfig;

    public EncoderWrapper(){
        encoder = new Encoder();
        defaultConfig = new Config(2, 1<<23, 128, 1, 3, 0, 2, false);
    }

    public EncoderWrapper(int dictionarySize){
        encoder = new Encoder();
        defaultConfig = new Config(2, dictionarySize, 128, 1, 3, 0, 2, false);
    }

    static int threadCount=0;

    public boolean code(File input, File output){
        try(InputStream ins = new BufferedInputStream(new FileInputStream(input));
            OutputStream outs = new BufferedOutputStream(new FileOutputStream(output));){
            encoder.SetAlgorithm(defaultConfig.algorithm);
            encoder.SetDictionarySize(defaultConfig.dictionarySize);
            encoder.SetNumFastBytes(defaultConfig.numFastBytes);
            encoder.SetMatchFinder(defaultConfig.matchFinder);
            encoder.SetLcLpPb(defaultConfig.lc, defaultConfig.lp, defaultConfig.pb);
            encoder.SetEndMarkerMode(defaultConfig.endMarkerMode);
            encoder.WriteCoderProperties(outs);
            long fileSize = input.length();
            for (int i=0; i<8; ++i){
                outs.write((int)(fileSize >>> (8 * i)) & 0xFF);
            }

            encoder.Code(ins, outs, -1, -1, null);

            outs.flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void codeWithNonBlockManner(File input, File output, ResultListener resultListener) {
        new Thread("lzmaEncoder"+ ++threadCount){
            @Override
            public void run() {
                boolean success = code(input, output);
                if (resultListener!=null){
                    new Handler(Looper.getMainLooper()).post(() -> resultListener.onEncodeFinished(success));
                }
            }
        }.start();

    }

    public interface ResultListener{
        void onEncodeFinished(boolean success);
    }

}
