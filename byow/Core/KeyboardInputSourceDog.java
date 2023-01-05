package byow.Core;
import byow.InputDemo.InputSource;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KeyboardInputSourceDog implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;
    private static String stringSeed = "";
    private static String stringMoves = "";
    private int aPosX;
    private int aPosY;
    private char inputType = 'j';
    private boolean isReplay = false;
    private TETile avatar = Tileset.AVATAR;
    private KeyboardInputSourceDog newInput;
    public KeyboardInputSourceDog() {
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(0.5, 0.8, "Carter");
        StdDraw.text(0.5, 0.6, "New Game (N)");
        StdDraw.text(0.5, 0.5, "Load Game (L)");
        StdDraw.text(0.5, 0.4, "Replay Last Save (R)");
        StdDraw.text(0.5, 0.3, "Change Appearance (C)");
        StdDraw.text(0.5, 0.2, "Quit (Q)");
    }

    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == 'n') {
                    inputType = 'n';
                    titleScreenDraw();
                } else if (c == 'l') {
                    inputType = 'l';
                    String path = System.getProperty("user.dir") + "/savedKey.txt";
                    try {
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        stringSeed = br.readLine();
                        stringMoves = br.readLine();
                        aPosX = Integer.parseInt(br.readLine());
                        aPosY = Integer.parseInt(br.readLine());
                        br.close();
                    } catch (IOException ex) {
                        System.out.println("An error occurred.");
                        ex.printStackTrace();
                    }
                } else if (c == 'q') {
                    return 'p';
                } else if (c == 'r') {
                    inputType = 'r';
                    String path = System.getProperty("user.dir") + "/savedKey.txt";
                    try {
                        FileReader fr = new FileReader(path);
                        BufferedReader br = new BufferedReader(fr);
                        stringSeed = br.readLine();
                        stringMoves = br.readLine();
                        br.close();
                    } catch (IOException ex) {
                        System.out.println("An error occurred.");
                        ex.printStackTrace();
                    }
                    isReplay = true;
                } else if (c == 'c') {
                    characterScreenDraw();
                }
                if (PRINT_TYPED_KEYS) {
                    System.out.println(c);
                }
                return c;
            }
        }
    }

    private void titleScreenDraw() {
        StdDraw.clear(Color.black);
        StdDraw.text(0.5, 0.6, "Enter Random Integer Seed");
        StdDraw.text(0.5, 0.5, "Press S when done");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char nextKey = StdDraw.nextKeyTyped();
                if (Character.toUpperCase(nextKey) == 'S') {
                    break;
                }
                if (PRINT_TYPED_KEYS) {
                    System.out.println(nextKey);
                }
                stringSeed = stringSeed + Character.toString(nextKey);
                System.out.println(stringSeed);
            }
        }
    }

    private void characterScreenDraw() {
        StdDraw.clear(Color.black);
        StdDraw.text(0.5, 0.6, "Please select an avatar appearance using keys listed.");
        StdDraw.text(0.25, 0.5, "@");
        StdDraw.text(0.75, 0.5, "Ⓐ");
        StdDraw.text(0.25, 0.4, "6");
        StdDraw.text(0.75, 0.4, "9");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char nextKey = StdDraw.nextKeyTyped();
                if (nextKey == '6') {
                    avatar = Tileset.AVATAR;
                    break;
                } else if (nextKey == '9') {
                    avatar = Tileset.AVATAR2;
                    break;
                }
                break;
            }
        }
        newInput = new KeyboardInputSourceDog();
    }

    public boolean possibleNextInput() {
        return true;
    }

    public String getSeed() {
        return stringSeed;
    }

    public String getMoves() {
        return stringMoves;
    }

    public char getInputType() {
        return inputType;
    }

    public int getaPosX() {
        return aPosX;
    }

    public int getaPosY() {
        return aPosY;
    }

    public TETile getAvatar() {
        return avatar;
    }

    public KeyboardInputSourceDog getNewInput() {
        return newInput;
    }
}
