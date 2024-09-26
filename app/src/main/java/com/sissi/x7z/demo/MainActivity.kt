package com.sissi.x7z.demo

import SevenZip.Compression.LZMA.Encoder
import SevenZip.Compression.LZMA.EncoderWrapper
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onEncClicked(v: View){
        val path = filesDir.absolutePath+"/x7z_origin.txt"
        val outputpath = filesDir.absolutePath+"/x7z.enc"
        val file = File(path)
        val outputFile = File(outputpath)
        (0..1000).forEach {
            file.appendText("test 7z: $it\n")
        }
//        val fileSize = file.length()
//        val ins = BufferedInputStream(FileInputStream(path))
//        val outs= BufferedOutputStream(FileOutputStream(outputpath))
//
//        // 配置encoder。（参考lzma sdk的lzmaAlone）
//        val encoder = Encoder().apply {
//            SetAlgorithm(2)
//            SetDictionarySize(1 shl 23)
//            SetNumFastBytes(128)
//            SetMatchFinder(1)
//            SetLcLpPb(3,0,2)
//            SetEndMarkerMode(false)
//            WriteCoderProperties(outs)
//        }
//
//        for (i in 0..7)
//            outs.write((fileSize ushr (8 * i)).toInt() and 0xFF)
//        encoder.Code(ins, outs, -1, -1, null)
//
//        outs.flush()
//        outs.close()
//        ins.close()

        val encoderWrapper = EncoderWrapper()
        encoderWrapper.code(file, outputFile, null)
    }
}