package com.example.barcodegenerator

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.barcodegenerator.ui.theme.BarCodeGeneratorTheme
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarCodeGeneratorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {
    val textval = remember { mutableStateOf("") }
    val qrbitmap = remember { mutableStateOf<Bitmap?>(null) }
    val barbitmap = remember { mutableStateOf<Bitmap?>(null) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(16.dp)
        ) {
            barbitmap.value?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Barcode",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

                qrbitmap.value?.let { bmp ->
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = "Barcode",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            TextField(
                value = textval.value,
                onValueChange = { textval.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    barbitmap.value = showsBarCode(textval.value)
                    qrbitmap.value = showQrCode(textval.value)
                },
                enabled = textval.value.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Show Bar Code")
            }
        }
    }
}



fun showQrCode(text: String): Bitmap {
    val size = 300
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val codewriter = MultiFormatWriter()

    try {
        val bitMatrix: BitMatrix = codewriter.encode(text, BarcodeFormat.QR_CODE, size, size)
        for (x in 0 until size) {
            for (y in 0 until size) {
                val color = if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                bitmap.setPixel(x, y, color)
            }
        }
    } catch (e: WriterException) {
        Log.d("QRcodeError", "Error generating QR code: $e")
    }

    return bitmap
}

fun showsBarCode(text: String): Bitmap {
    val size = 300
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val qrCodeWriter = MultiFormatWriter()

    try {
        val bitMatrix: BitMatrix = qrCodeWriter.encode(text, BarcodeFormat.CODE_128, size, size)
        for (x in 0 until size) {
            for (y in 0 until size) {
                val color = if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                bitmap.setPixel(x, y, color)
            }
        }
    } catch (e: WriterException) {
        Log.d("BarcodeError", "Error generating Bar Code: $e")
    }
    return bitmap
}
