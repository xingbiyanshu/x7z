package com.sissi.x7z.demo

import SevenZip.Compression.LZMA.Encoder
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

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
        val outputpath = filesDir.absolutePath+"/x7z_Enc.txt"
        val file = File(path)
        file.writeText("test 7z")
        val fileSize = file.length()
        val encoder = Encoder()
//        for (i in 0..7) outStream.write((fileSize ushr (8 * i)) as Int and 0xFF)
    }
}