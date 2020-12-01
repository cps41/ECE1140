public class Clock {
    public static int hour = 6;
    public static int minute = 0;
    public static int second = 0;
    public static String day = "Monday";
    public static int timeFactor = 1;
    public static final int TIME_FACTOR_LIMIT = 250;
    private static final String[] DAYS = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private static int dayIndex = 0;
    private static long pastTime = System.currentTimeMillis();

    public static void update(){
        long currentTime = System.currentTimeMillis();
        if ( (currentTime - pastTime) >= (1000/timeFactor) ){
            if (second == 59){
                second = 0;
                if (minute == 59){
                    minute = 0;
                    if (hour == 23){
                        hour = 0;
                        if (dayIndex != 6){dayIndex++;}
                        else{dayIndex=0;}
                        day = DAYS[dayIndex];
                    }
                    else{
                        hour++;
                    }
                }
                else{
                    minute ++;
                }
            }
            else{
                second++;
            }
            pastTime = currentTime;
        }

    }
    public static String getDate(){
        String date = day;
        date=date+" "+padLeft(hour)+":"+padLeft(minute)+":"+padLeft(second);
        return date;
    }
    private static String padLeft(int time){
        String string = String.valueOf(time);
        if (string.length() == 2) {return string;}
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 2 - string.length()) {sb.append('0');}
        sb.append(string);
        return sb.toString();
    }
}
