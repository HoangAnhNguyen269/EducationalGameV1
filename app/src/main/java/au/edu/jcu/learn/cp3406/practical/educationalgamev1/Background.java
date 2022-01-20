package au.edu.jcu.learn.cp3406.practical.educationalgamev1;

class Background {
    //background thread for ShareOnTwitterActivity
    static void run(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
