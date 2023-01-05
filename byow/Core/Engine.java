package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.*;
import java.io.*;
public class Engine {
    private TERenderer title = new TERenderer();
    public static final int WIDTH = 60;
    public static final int HEIGHT = 30;
    private boolean isRunning;
    private boolean colonQ = false;
    private TETile avatar = Tileset.AVATAR;
    private static final int TIME = 20;
    private HashMap<String, Integer> avatarPos;
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        KeyboardInputSourceDog inputSource = new KeyboardInputSourceDog();
        char key = inputSource.getNextKey();
        if (key == 'p') {
            System.exit(0);
        } else if (key == 'c') {
            avatar = inputSource.getAvatar();
            inputSource = inputSource.getNewInput();
            inputSource.getNextKey();
        }
        char inputType = inputSource.getInputType();
        String seed = inputSource.getSeed();
        System.out.println(seed);
        String moves = inputSource.getMoves();
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        String[] mainArgument = new String[] {seed};
        /** 0th input is seed. */
        MapGenerator map = new MapGenerator(Long.parseLong(mainArgument[0]),
                WIDTH, HEIGHT, finalWorldFrame);
        map.main(mainArgument);
        if (inputType == 'n' || inputType == 'l' || inputType == 'r') {
            title.initialize(WIDTH, HEIGHT + 5);
            HashMap<String, Integer> aPos = new HashMap<>();
            if (inputType == 'n') {
                isRunning = true;
                aPos = initialAvatarPos(map);
                finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                title.renderFrame(finalWorldFrame);
                nWhileLoop(finalWorldFrame, aPos, seed, moves);
            } else if (inputType == 'l') {
                isRunning = true;
                aPos.put("x", inputSource.getaPosX());
                aPos.put("y", inputSource.getaPosY());
                finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                title.renderFrame(finalWorldFrame);
                steph(finalWorldFrame, aPos, seed, moves);
            } else if (inputType == 'r') {
                isRunning = true;
                boolean isRunning2 = true;
                aPos = initialAvatarPos(map);
                char[] charMoves = new char[moves.length()];
                for (int i = 0; i < moves.length(); i++) {
                    charMoves[i] = moves.charAt(i);
                }
                while (isRunning) {
                    replayMoves(charMoves, finalWorldFrame, aPos);
                    isRunning = false;
                    isRunning2 = true;
                }
                stdDrawThing(isRunning2, finalWorldFrame, seed, moves, aPos);
            }
        }
    }


    private void filerTheCreator(String seed, String moves, int x, int y) {
        try {
            File file = new File(System.getProperty("user.dir") + "/savedKey.txt");
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
            File savedWorld = new File(System.getProperty("user.dir") + "/savedKey.txt");
            System.out.println("new saved file");
            FileWriter writeToFile = new FileWriter(savedWorld);
            BufferedWriter bw = new BufferedWriter(writeToFile);
            bw.write(seed);
            bw.newLine();
            bw.write(moves);
            bw.newLine();
            bw.write(x + "");
            bw.newLine();
            bw.write(y + "");
            bw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void nWhileLoop(TETile[][] finalWorldFrame,
                            HashMap<String, Integer> aPos, String seed, String moves) {
        while (isRunning) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                boolean move;
                if (c == ':') {
                    colonQ = true;
                    continue;
                } else if (c == 'q' && colonQ) {
                    filerTheCreator(seed, moves, aPos.get("x"), aPos.get("y"));
                    isRunning = false;
                    System.exit(0);
                } else if (c == 'a') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('a');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x") + 1][aPos.get("y")] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                } else if (c == 'd') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('d');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x") - 1][aPos.get("y")] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                } else if (c == 's') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('s');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x")][aPos.get("y") + 1] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                } else if (c == 'w') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('w');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x")][aPos.get("y") - 1] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                }
                colonQ = false;
            }
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if (x < WIDTH && x >= 0) {
                if (y < HEIGHT && y >= 0) {
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    TETile tile = finalWorldFrame[x][y];
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(7, HEIGHT + 2, tile.description());
                    StdDraw.show();
                    StdDraw.clear(Color.BLACK);

                }
            }
            title.renderFrame(finalWorldFrame);
        }
    }

    private void steph(TETile[][] finalWorldFrame, HashMap<String,
            Integer> aPos, String seed, String moves) {
        while (isRunning) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                boolean move;
                if (c == ':') {
                    colonQ = true;
                    continue;
                } else if (c == 'q' && colonQ) {
                    filerTheCreator(seed, moves, aPos.get("x"), aPos.get("y"));
                    isRunning = false;
                    System.exit(0);
                } else if (c == 'a') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('a');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x") + 1][aPos.get("y")] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                } else if (c == 'd') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('d');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x") - 1][aPos.get("y")] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                } else if (c == 's') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('s');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x")][aPos.get("y") + 1] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                } else if (c == 'w') {
                    move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
                    if (move) {
                        moves += Character.toString('w');
                        finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                        finalWorldFrame[aPos.get("x")][aPos.get("y") - 1] = Tileset.FLOOR;
                    }
                    title.renderFrame(finalWorldFrame);
                }
                colonQ = false;
            }
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if (x < WIDTH && x >= 0) {
                if (y < HEIGHT && y >= 0) {
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    TETile tile = finalWorldFrame[x][y];
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(7, HEIGHT + 2, tile.description());
                    StdDraw.show();
                    StdDraw.clear(Color.BLACK);

                }
            }
            title.renderFrame(finalWorldFrame);
        }
    }

    private void replayMoves(char[] charMoves, TETile[][] finalWorldFrame,
                             HashMap<String, Integer> aPos) {
        for (char c : charMoves) {
            boolean move = moveAvatarKeyboard(c, aPos, finalWorldFrame);
            if (move && c == 'w') {
                finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                finalWorldFrame[aPos.get("x")][aPos.get("y") - 1] = Tileset.FLOOR;
                title.renderFrame(finalWorldFrame, TIME);
            } else if (move && c == 'a') {
                finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                finalWorldFrame[aPos.get("x") + 1][aPos.get("y")] = Tileset.FLOOR;
                title.renderFrame(finalWorldFrame, TIME);
            } else if (move && c == 's') {
                finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                finalWorldFrame[aPos.get("x")][aPos.get("y") + 1] = Tileset.FLOOR;
                title.renderFrame(finalWorldFrame, TIME);
            } else if (move && c == 'd') {
                finalWorldFrame[aPos.get("x")][aPos.get("y")] = avatar;
                finalWorldFrame[aPos.get("x") - 1][aPos.get("y")] = Tileset.FLOOR;
                title.renderFrame(finalWorldFrame, TIME);
            }
        }
    }

    private void stdDrawThing(boolean isRunning2, TETile[][] finalWorldFrame,
                              String seed, String moves, HashMap<String, Integer> aPos) {
        while (isRunning2) {
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if (x < WIDTH && x >= 0) {
                if (y < HEIGHT && y >= 0) {
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    TETile tile = finalWorldFrame[x][y];
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(7, HEIGHT + 2, tile.description());
                    StdDraw.show();
                    StdDraw.clear(Color.BLACK);

                }
            }
            title.renderFrame(finalWorldFrame);
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (c == ':') {
                    colonQ = true;
                    continue;
                } else if (c == 'q' && colonQ) {
                    filerTheCreator(seed, moves, aPos.get("x"), aPos.get("y"));
                    isRunning2 = false;
                    System.exit(0);
                }
                colonQ = false;
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        InputSource inputSource = new StringInputDevice(input);
        char inputType = inputSource.getNextKey();
        String stringSeed = "";
        String stringMoves = "";
        if (inputType == 'n') {
            while (inputSource.possibleNextInput()) {
                char c = inputSource.getNextKey();
                if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4'
                        || c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
                    stringSeed = stringSeed + Character.toString(c);
                } else if (c == 's') {
                    break;
                }
            }
            while (inputSource.possibleNextInput()) {
                char c = inputSource.getNextKey();
                if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
                    stringMoves += Character.toString(c);
                } else if (c == ':') {
                    tryStatement(stringSeed, stringMoves);
                    break;
                }
            }
        } else if (inputType == 'l') {
            String path = System.getProperty("user.dir") + "/saved.txt";
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
            while (inputSource.possibleNextInput()) {
                char c = inputSource.getNextKey();
                if (c == 'w' || c == 'a' || c == 's' || c == 'd') {
                    stringMoves += Character.toString(c);
                } else if (c == ':') {
                    try {
                        File file = new File(System.getProperty("user.dir") + "/saved.txt");
                        if (file.delete()) {
                            System.out.println("File deleted successfully");
                        } else {
                            System.out.println("Failed to delete the file");
                        }
                        File savedWorld = new File(System.getProperty("user.dir") + "/saved.txt");
                        FileWriter writeToFile = new FileWriter(savedWorld);
                        BufferedWriter bw = new BufferedWriter(writeToFile);
                        bw.write(stringSeed);
                        bw.newLine();
                        bw.write(stringMoves);
                        bw.close();
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        renderAndPrint(finalWorldFrame, stringSeed, stringMoves, inputType);
        System.out.println(TETile.toString(finalWorldFrame));
        return finalWorldFrame;
    }

    private void renderAndPrint(TETile[][] finalWorldFrame, String stringSeed,
                                String stringMoves, char inputType) {
        String[] mainArgument = new String[] {stringSeed};
        /** 0th input is seed. */
        MapGenerator map = new MapGenerator(Long.parseLong(mainArgument[0]),
                WIDTH, HEIGHT, finalWorldFrame);
        map.main(mainArgument);
        if (inputType == 'n' || inputType == 'l') {
            avatarPos = initialAvatarPos(map);
            char[] charMoves = new char[stringMoves.length()];
            for (int i = 0; i < stringMoves.length(); i++) {
                charMoves[i] = stringMoves.charAt(i);
            }
            for (char c : charMoves) {
                moveAvatar(c, avatarPos, finalWorldFrame);
            }
            finalWorldFrame[avatarPos.get("x")][avatarPos.get("y")] = avatar;
        }
    }

    private void tryStatement(String stringSeed, String stringMoves) {
        try {
            File file = new File(System.getProperty("user.dir") + "/saved.txt");
            if (file.delete()) {
                System.out.println("File deleted successfully");
            } else {
                System.out.println("Failed to delete the file");
            }
            File savedWorld = new File(System.getProperty("user.dir") + "/saved.txt");
            System.out.println("new saved file");
            FileWriter writeToFile = new FileWriter(savedWorld);
            BufferedWriter bw = new BufferedWriter(writeToFile);
            bw.write(stringSeed);
            bw.newLine();
            bw.write(stringMoves);
            bw.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private HashMap<String, Integer> initialAvatarPos(MapGenerator map) {
        TETile[][] worldTile = map.getWorld();
        HashMap<String, Integer> avatarXY = new HashMap<>();
        int startX;
        int startY;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (worldTile[j][i] == Tileset.FLOOR) {
                    startX = j;
                    startY = i;
                    avatarXY.put("x", startX);
                    avatarXY.put("y", startY);
                    return avatarXY;
                }
            }
        }
        return null;
    }

    private void moveAvatar(char c, HashMap<String, Integer> aPos, TETile[][] worldFrame) {
        int x = aPos.get("x");
        int y = aPos.get("y");
        if (c == 'w') {
            if (worldFrame[x][y + 1] == Tileset.FLOOR) {
                aPos.replace("y", y + 1);
            }
            return;
        } else if (c == 'a') {
            if (worldFrame[x - 1][y] == Tileset.FLOOR) {
                aPos.replace("x", x - 1);
            }
            return;
        } else if (c == 's') {
            if (worldFrame[x][y - 1] == Tileset.FLOOR) {
                aPos.replace("y", y - 1);
            }
            return;
        } else if (c == 'd') {
            if (worldFrame[x + 1][y] == Tileset.FLOOR) {
                aPos.replace("x", x + 1);
            }
            return;
        }
    }

    private boolean moveAvatarKeyboard(char c, HashMap<String,
            Integer> aPos, TETile[][] worldFrame) {
        int x = aPos.get("x");
        int y = aPos.get("y");
        if (c == 'w') {
            if (worldFrame[x][y + 1] == Tileset.FLOOR) {
                aPos.replace("y", y + 1);
                return true;
            }
        } else if (c == 'a') {
            if (worldFrame[x - 1][y] == Tileset.FLOOR) {
                aPos.replace("x", x - 1);
                return true;
            }
        } else if (c == 's') {
            if (worldFrame[x][y - 1] == Tileset.FLOOR) {
                aPos.replace("y", y - 1);
                return true;
            }
        } else if (c == 'd') {
            if (worldFrame[x + 1][y] == Tileset.FLOOR) {
                aPos.replace("x", x + 1);
                return true;
            }
        }
        return false;
    }
}
