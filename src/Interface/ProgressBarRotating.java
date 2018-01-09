package Interface;

public class ProgressBarRotating extends Thread{
    public boolean showProgress = true;
    private int seconds;


    public ProgressBarRotating(int seconds) {
        this.seconds = seconds;
    }

    public void run() {
        String anim[] = new String[4];
        anim[0] = "";
        anim[1] = ".";
        anim[2] = "..";
        anim[3] = "...";
        int x = 0;

        long end = System.currentTimeMillis() + (seconds*1000);
        while (showProgress) {
            //System.out.print("\r\r Processing " + anim.charAt(x++ % anim.length()) + " ");
            long curr = System.currentTimeMillis();
            long diff = end - curr;
            if(diff < 0){
                System.out.print("\r\r\r\r\r\r\r\r\r\r\r Processing ...["+ 0 + "s left]");
            }else {
                System.out.print("\r\r\r\r Processing " + anim[x++ % anim.length] + "\t[" + diff/1000 + "s left]");
            }

            try {
                Thread.sleep(500);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}