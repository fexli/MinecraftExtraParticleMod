package com.fexli.particle.future;

import com.fexli.particle.utils.TextStyleManager;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class PyThread extends Thread {
    private final String filepath;
    private final ServerCommandSource source;
    private final String args;

    public PyThread(String name,String filepath, ServerCommandSource source, String args){
        super(name);
        this.filepath = filepath;
        this.source = source;
        this.args = args;
    }
    @Override
    public void run(){
        try {
            // check file exist
            File pyfile = new File(JyProcessing.pyRootDir+filepath);
            if(!pyfile.exists())
            {
                source.sendFeedback(new TranslatableText("commands.python.execute.filenotexist",filepath).setStyle(TextStyleManager.Red),false);
                return;
            }

            // run file
            Runtime rt = Runtime.getRuntime();
            Process p = rt.exec("python "+ JyProcessing.pyRootDir+filepath+" "+args); // print('Hello!')



            /*为"错误输出流"单独开一个线程读取之,否则会造成标准输出流的阻塞*/
            Thread t=new Thread(new InputStreamRunnable(p.getErrorStream(),"ERR",source, filepath,0));
            t.start();

            /*"标准输出流"就在当前方法中读取*/
            Thread r=new Thread(new InputStreamRunnable(p.getInputStream(),"OPT",source, filepath,System.currentTimeMillis()));
            r.start();


        }
        catch(Exception e) {
            source.sendFeedback(new TranslatableText("commands.python.execute.failure",e.toString()).setStyle(TextStyleManager.Red),false);
            System.out.println(e.getMessage());
        }
    }
    static class InputStreamRunnable implements Runnable {
        BufferedReader bReader=null;
        String type=null;
        ServerCommandSource source;
        String name = null;
        Double nano = null;
        public InputStreamRunnable(InputStream is, String _type, ServerCommandSource source,String name,double nano)
        {
            try
            {
                this.bReader=new BufferedReader(new InputStreamReader(new BufferedInputStream(is), StandardCharsets.UTF_8));
                this.type=_type;
                this.source = source;
                this.name = name;
                this.nano = nano;
            }
            catch(Exception ignored)
            {
            }
        }
        public void run()
        {
            String line;
            int lineNum=0;

            try
            {
                while((line=this.bReader.readLine())!=null)
                {
//                    lineNum++;
                    if(this.source != null){
                        if (this.type.equals("ERR")){
                            this.source.sendFeedback(new TranslatableText("commands.python.execute.lineerr",line),false);
                        } else {
                            this.source.sendFeedback(new TranslatableText("commands.python.execute.line",line),false);
                        }
                    }
                    //Thread.sleep(200);
                }
                this.bReader.close();
                if (!this.type.equals("ERR")) this.source.sendFeedback(new TranslatableText("commands.python.execute.finish",this.name,(float)(System.currentTimeMillis()-this.nano)/1000).setStyle(TextStyleManager.Aqua),false);
            }
            catch(Exception ignored)
            {
            }
        }
    }
}
