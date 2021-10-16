public class PomodoroTimer {
    private int[] focusTime;
    private int[] shortBreakTime;
    private int[] longBreakTime;

    public PomodoroTimer(int[] focus, int[] shortBreak, int[] longBreakTime){
        this.focusTime = focus;
        this.shortBreakTime = shortBreak;
        this.longBreakTime = longBreakTime;
    }

    public int[] getFocusTime(){
        return this.focusTime;
    }

    public int[] shortBreakTime(){
        return this.shortBreakTime;
    }

    public int[] longBreakTime(){
        return this.longBreakTime;
    }

    public void setFocusTime(int[] focus){
        this.focusTime = focus;
    }

    public void setShortBreak(int[] shortBreak){
        this.shortBreakTime = shortBreak;
    }

    public void setLongBreak(int[] longBreak){
        this.longBreakTime = longBreak;
    }

    public void pause(){
    }

    public void start(){
    }

    public void resume(){
    }

    public void countDown(){
    }

}
