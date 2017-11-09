package org.apache.jmeter.java.ffmpeg;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
 
 class StreamCaptureThread
   implements Runnable
 {
   InputStream stream;
   StringBuilder output;
   BufferedReader br;
   String type;
 
   public StreamCaptureThread(InputStream stream, String type)
   {
     this.type = type;
     this.stream = stream;
     this.output = new StringBuilder();
   }
 
   public void run() {
     try {
       try {
         this.br = new BufferedReader(new InputStreamReader(this.stream));
         String line = this.br.readLine();
         while (line != null) {
           if (line.trim().length() > 0) {
             this.output.append(line).append("\n");
             FfmpegUtil.logger.info(this.type + ": " + line + "@@@@@@@@@");
           }
           line = this.br.readLine();
         }
       } finally {
         if (this.stream != null) {
           this.stream.close();
         }
         if (this.br != null)
           this.br.close();
       }
     }
     catch (IOException e) {
       e.printStackTrace();
     }
   }
 }