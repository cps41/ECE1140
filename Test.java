public class Test {
    public static void update(){
        CTCSystem.RED_MODEL.passengerCount  = 5;
        CTCSystem.GREEN_MODEL.passengerCount  = 5;

    }
    public static void configure(){
        //create a small track, with one destination and two switches, one section
        int intArray[][] = {
            {0,0,-1,0},//
            {-2,0,2,25},//1
            {0,0,2,25},//2
            {0,0,2,25},//3
            {0,0,2,25},//4
            {1,0,2,25},//5
            {-1,0,2,25},//6
            {0,0,2,25},//7
            {0,0,2,25},//8
            {-2,0,2,25},//9
            {2,0,2,25},//10
            {0,0,2,25},//11
            {0,0,2,25},//12
            {3,0,2,25},//13
            {-3,0,2,25},//14
            {0,0,2,25},//15
            {0,0,2,25},//16
            {-3,0,2,25},//17
            {-1,0,2,25},//18
        };
        float floatArray[][] = {
            {-1,-1},
            {100,0},//1
            {100,0},//2
            {100,0},//3
            {100,0},//4
            {100,0},//5
            {100,0},//6
            {100,0},//7
            {100,0},//8
            {100,0},//9
            {100,0},//10
            {100,0},//11
            {100,0},//12
            {100,0},//13
            {100,0},//14
            {100,0},//15
            {100,0},//16
            {100,0},//17
            {0,0},//18
        };
        String stringArray[][] = {
            {"-1","-1"},
            {"A","0"},//1
            {"A","0"},//2
            {"A","STATION_A"},//3
            {"A","0"},//4
            {"A","0"},//5
            {"A","0"},//6
            {"A","0"},//7
            {"A","0"},//8
            {"A","0"},//9
            {"A","0"},//10
            {"A","0"},//11
            {"A","STATION_B"},//12
            {"A","0"},//13
            {"A","0"},//14
            {"A","0"},//15
            {"A","0"},//16
            {"A","0"},//17
            {"A","0"},//18
        }; 
        int SIZE = 17;
        CTCSystem.GREEN_MODEL.configurationInt = intArray;
        CTCSystem.RED_MODEL.configurationInt = intArray;
        CTCSystem.GREEN_MODEL.configurationFloat = floatArray;
        CTCSystem.RED_MODEL.configurationFloat = floatArray;
        CTCSystem.GREEN_MODEL.configurationString = stringArray;
        CTCSystem.RED_MODEL.configurationString = stringArray;
        CTCSystem.GREEN_MODEL.SIZE = SIZE;
        CTCSystem.RED_MODEL.SIZE = SIZE;
        /*
        Block[] BLOCKS = new Block[13];
        for (int i=1;i<=12;i++)
            BLOCKS[i] = new Block(i);

        for (int i=2;i<=9;i++){
            BLOCKS[i].CONNECTS[0] = BLOCKS[i-1];
            BLOCKS[i].CONNECTS[1] = BLOCKS[i+1];
        }
        BLOCKS[1].CONNECTS[0] = BLOCKS[10];
        BLOCKS[1].CONNECTS[1] = BLOCKS[2];
        BLOCKS[11].CONNECTS[0] = BLOCKS[9];
        BLOCKS[12].CONNECTS[0] = BLOCKS[2];
        BLOCKS[10].CONNECTS[0] = BLOCKS[9];
        BLOCKS[10].CONNECTS[1] = BLOCKS[1];

        BLOCKS[2].CONNECTS[2] = BLOCKS[12];
        BLOCKS[2].SWITCH  = 1;
        BLOCKS[1].SWITCH  = -1;
        BLOCKS[12].SWITCH  = -1;

        BLOCKS[9].CONNECTS[2] = BLOCKS[11];
        BLOCKS[9].SWITCH  = 2;
        BLOCKS[11].SWITCH  = -2;
        BLOCKS[10].SWITCH  = -2;

        for (int i=1;i<=12;i++){
            BLOCKS[i].CROSSING = 0;
            BLOCKS[i].LIMIT = 50;
            BLOCKS[i].LENGTH = 100;
            BLOCKS[i].GRADE = 0;
            BLOCKS[i].SECTION = "A";
            BLOCKS[i].DESTINATION = "0";
            BLOCKS[i].DIRECTION = 0;
        }
        BLOCKS[6].DESTINATION = "DESTINATION";
        BLOCKS[11].DIRECTION = 0;
        BLOCKS[12].DIRECTION = 1;
        CTCSystem.RED_MODEL.BLOCKS = BLOCKS;
        CTCSystem.GREEN_MODEL.BLOCKS = BLOCKS;
        CTCSystem.RED_MODEL.SIZE = 10;
        CTCSystem.GREEN_MODEL.SIZE = 10;*/
    }
    
}
