package com.example.testrepository

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.StringWriter
import java.nio.CharBuffer

object getRawToString {
    fun getDummy(context: Context): String {
        val inputStream = context.getResources().openRawResource(R.raw.alarm);
        val writer = StringWriter();
        var buffer = CharBuffer.allocate(1024)
        try {
            var reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"));
            var n: Int
            n = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer.array(), 0, n);
                buffer.clear()
                n = reader.read(buffer)
            }
        } catch (e: Exception) {
        } finally {
            try {
                inputStream.close();
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        var jsonString = writer.toString();
        return jsonString
    }

    fun Context.getRawtoString(id: Int): String {
        val inputStream = getResources().openRawResource(id);
        val writer = StringWriter();
        var buffer = CharBuffer.allocate(1024)
        try {
            var reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"));
            var n: Int
            n = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer.array(), 0, n);
                buffer.clear()
                n = reader.read(buffer)
            }
        } catch (e: Exception) {
        } finally {
            try {
                inputStream.close();
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        var jsonString = writer.toString();
        return jsonString
    }
}