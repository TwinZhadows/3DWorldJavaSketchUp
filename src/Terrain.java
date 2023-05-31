

import terrain2.*;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.j3d.QuadArray;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;

public class Terrain extends javax.swing.JFrame {

    private static int numInstance = 0;
    private static ArrayList<QuadArray> polygonAr = new ArrayList<QuadArray>();
    private static SKPControl skp = new SKPControl();
    private static final String SKP_TITLE = "Untitled - SketchUp";
    private static final String EDITOR_TITLE = "Output - terrain2 (run)   - Editor";
    private static final String RC_TITLE = "Ruby Console";
    private static JAutoIt lib = JAutoIt.INSTANCE;
    private ArrayList<StaticShape> statics = new ArrayList<StaticShape>();
    private AnimationListener lis;
    private String directory = skp.getDirectory();
    //dont change
    private String file = directory + "file.txt";
    private static int index = 0;
    private int offsetX = 0, offsetY = 0;
    private double[][] square;

    private int iter = 0;
    private int tSize = 0;
    //the size of the displayed pixel
    private int rectSize = 1;
    //the sleeping interval between two pixel calculations in milliseconds, 0 will cause the programm to calculate the diamondsquare in one flush
    private int sleep = 1;
    private int polygonSize = 0;
    private BufferedImage image;
    private Graphics2D g;
    private boolean ready = false;
    private ArrayList<Shape> shape = new ArrayList<Shape>();
    private ArrayList<Model> models = new ArrayList<Model>();
    private ArrayList<Model> movModels = new ArrayList<Model>();
    private Model user;
    private int level = 0;
    private int texIndex = 0;
    private int count = 0;
    private String[] tex;
    private int[] height;
    private boolean flat = false;
    private int depth = 0;
    private float resolution = 0;

    public Terrain(int depth, int resolution, int plgSize) {

        //generate terrain model from given resolution, depth, and polygon size
        this.depth = depth;
        if (resolution <= 0) {
            try {
                throw new TerrainException("Terrain resolution must be greater than zero");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (plgSize <= 0) {
            try {
                throw new TerrainException("Polygon size must be greater than zero");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (numInstance < 1) {
            numInstance++;
            polygonSize = plgSize;
            //this.flat = isFlat;
            tSize = resolution;
            this.resolution = (float) ((Math.pow(2, tSize) + 1) * polygonSize);
            initComponents();
            //setVisible(true);
            //adjust the second value int Math.pow to reduce the resolution
            initSquare((int) Math.pow(2, resolution) + 1);

            createPolygon();
            try {
                saveTerrain(file, (int) (Math.pow(2, tSize) + 1));
            } catch (IOException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
            loadTerrain(file, plgSize);
        } else {
            try {
                throw new TerrainException("Cannot create multiple terrains.");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    
    public Terrain(String file, int plgSize) throws IOException {
        //geenerate terrain model from coordinate file
        this.polygonSize = plgSize;

        if (file.endsWith(".txt")) {

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CollisionEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
            String line;
            String[] info;
            int count = 0;
            int size, i, j, z;
            while ((line = br.readLine()) != null) {
                if (count > 0) {
                    info = line.split(",");
                    i = Integer.parseInt(info[0]);
                    j = Integer.parseInt(info[1]);
                    z = Integer.parseInt(info[2]);
                    square[i][j] = z;
                } else {
                    info = line.split(",");
                    System.out.println(info[1]);
                    size = Integer.parseInt(info[1]);
                    tSize = size;
                    square = new double[(int) Math.pow(2, size) + 1][(int) Math.pow(2, size) + 1];
                }
                count++;
            }
        } else {
            try {
                throw new TerrainException("Incompatible file type");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.resolution = (float) ((Math.pow(2, tSize) + 1) * polygonSize);
        createPolygon();

        loadTerrain(file, plgSize);
    }

    
    public Terrain(String file, int depth, int plgSize) throws IOException {
        //generate terrain model from heightmap
        this.polygonSize = plgSize;
        if (file.endsWith(".bmp") || file.endsWith(".png") || file.endsWith(".jpg")) {
            try {

                BufferedImage image = ImageIO.read(new File(file));
                square = new double[image.getWidth()][image.getHeight()];
                resolution = image.getHeight() * plgSize;
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        /* in a grayscale map, the RGB parts will all be the same value, 
                         so just take one out of the packed ints */
                        int height = depth - (int) ((image.getRGB(x, y) & 0xFF) / 255.0 * depth);
                        if (height != 0) {
                            System.out.printf("%01d ", height);
                        } else {
                            System.out.print("  ");
                        }
                        square[x][image.getHeight() - 1 - y] = height;

                    }
                }
                file = "C:/Users/Roonnapak/Documents/NetBeansProjects/terrain2/file.txt";
                saveTerrain(file, image.getWidth());
                System.out.println();
            } catch (IOException e) {
                System.out.println(e);
            }

        } else {
            try {
                throw new TerrainException("Incompatible file type");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        createPolygon();

        loadTerrain(file, plgSize);
    }
    /*a
     public void paint(Graphics grphcs) {
     for (int i = 0; i < square.length; i++) {
     for (int j = 0; j < square.length; j++) {
     if (square[i][j] == -1) {
     g.setColor(Color.white);
     } else if (square[i][j] > 255 || square[i][j] < 0) {
     g.setColor(Color.red);
     } else {
     g.setColor(new Color((int) square[i][j], (int) square[i][j], (int) square[i][j]));
     }
     g.fillRect(i * rectSize + offsetX, j * rectSize + offsetY, rectSize, rectSize);
     }

     }
     }
     */

    
    private final void initSquare(int size) {
        //initialize height of each coordinate at each corner of terrain heightmap
        setSize(size * rectSize + offsetX * 2, size * rectSize + offsetX + offsetY);
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();

        square = new double[size][size];
        int k = size - 1;
        double t = Math.log(k) / Math.log(2);
        if (t != (int) t) {
            System.err.println("size must be of type 2^n + 1");
            return;
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                square[i][j] = 0;
            }
        }
        if (depth != 0) {
            square[0][0] = Math.random() * depth;
            square[0][k] = Math.random() * depth;
            square[k][0] = Math.random() * depth;
            square[k][k] = Math.random() * depth;
            calcMids();
        }
        /*
         try {
         BufferedImage dest = image.getSubimage(0, 0, (int) Math.pow(2, tSize) + 1, (int) Math.pow(2, tSize) + 1);
         ImageIO.write(dest, "bmp", new File("C:\\imageTest.bmp"));
         } catch (IOException ex) {
         Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        long stop = System.currentTimeMillis();
    }//end of initSquare()

    
    private void calcMids() {
        //repeatedly calculate height of a midpoint out of neighbor coordinates
        iter++;
        int offset = (int) ((square.length - 1) / Math.pow(2, iter));

        for (int i = offset; i < square.length; i += 2 * offset) {
            for (int j = offset; j < square.length; j += 2 * offset) {
                square[i][j] = (square[i - offset][j - offset]
                        + square[i + offset][j - offset]
                        + square[i + offset][j + offset]
                        + square[i - offset][j + offset]) / 4 + 0;//randomDisplacement();
                if (sleep != 0) {
                    try {
                        Thread.sleep(sleep);
                        //repaint();
                    } catch (InterruptedException ex) {
                    }
                }

            }
        }
        calcHalfs();
    }//end of calcMids()
    

    private double randomDisplacement() {
        double t = (Math.random() / iter - 1 / (2 * iter)) * 64;
        return t;
    }//end of randomDisplacement()

    
    private void helperHalfs(int i, int j, int offset, int M) {
        double s = ((i != 0 ? square[i - offset][j] : 0)
                + (j != 0 ? square[i][j - offset] : 0)
                + (i != M ? square[i + offset][j] : 0)
                + (j != M ? square[i][j + offset] : 0));
        square[i][j] = s / ((i == 0 || j == 0 || j == M || i == M) ? 3 : 4) + randomDisplacement();
    }//end of helperHalfs()

    
    private void calcHalfs() {
        int offset = (int) ((square.length - 1) / Math.pow(2, iter));
        int M = square.length - 1;
        for (int i = 0; i < square.length; i += 2 * offset) {
            for (int j = 0; j < square.length; j += 2 * offset) {
                i += offset;
                if (i < square.length) {
                    helperHalfs(i, j, offset, M);
                }
                i -= offset;
                j += offset;
                if (j < square.length) {
                    helperHalfs(i, j, offset, M);
                }
                j -= offset;

                if (sleep != 0) {
                    try {
                        Thread.sleep(sleep);
                        repaint();
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }

        if (offset != 1) {
            calcMids();
        }
    }//end of calcHalfs()
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        org.jdesktop.layout.GroupLayout layout;
        layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(0, 300, Short.MAX_VALUE));

        pack();
    }// </editor-fold>                        

    // Variables declaration - do not modify                    
    // End of variables declaration         
    private boolean loadTerrain(String path, int dist) {
        
        //load the generated coordinate file to SketchUp and start reddering a terrain model
        lib.AU3_Send("terrain.readFile(\"" + path + "\", " + dist + ") {ENTER}", 0);
        lib.AU3_Sleep(3000);

        skp.getDisplay();
        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        lib.AU3_Sleep(1000);

        lib.AU3_WinActivate(EDITOR_TITLE, "");
        return true;
    }//end of loadTerrain()

    
    public boolean texture() throws IOException {

        //create transition level between each texture level and sending command to SketchUp to start texturing the terrain as defined by method setTexture()
        int height;
        for (int i = 1; i < tex.length - 1; i += 2) {
            tex[i] = blendTex(tex[i - 1], tex[i + 1]);
            height = (this.height[i - 1] / 2 + this.height[i + 1] / 2) / 2;
            setTexture((float) (i + 2) / 2, tex[i], height);

        }
        long startTime = System.currentTimeMillis();
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");

        lib.AU3_Send("terrain.texture(){ENTER}", 0);
        lib.AU3_Sleep((int) (1000 * Math.sqrt(getResolution() / polygonSize)));
        skp.getDisplay();

        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display
        lib.AU3_WinActivate(EDITOR_TITLE, "");
        long stopTime = System.currentTimeMillis();
        System.out.println("Delay = " + (stopTime - startTime));
        return true;
    }//end of texture() 

    
    public boolean setLevel(int level) {
        //set the number of texture level, not including the transition levels
        this.level = level;
        if (level <= 0) {
            try {
                throw new TerrainException("Texture level must be greater than zero");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int hidLevel = level - 1;
        level += hidLevel;
        tex = new String[level];
        height = new int[level];
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");

        lib.AU3_Send("terrain.setLevel(" + level + "){ENTER}", 0);
        lib.AU3_Sleep(1000);
        skp.getDisplay();
        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        lib.AU3_Sleep(1000);

        lib.AU3_WinActivate(EDITOR_TITLE, "");

        return true;
    }//end of setLevel()

    
    public boolean setTexture(float num, String texName, int height) throws IOException {
        //define texture of each texture level of the terrain
        if (num <= 0) {
            try {
                throw new TerrainException("Texture level must be greater than zero");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (num > level) {
            try {
                throw new TerrainException("Texture level is out of range");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int index = (int) (num * 2 - 2);
        this.height[index] = height;
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
        lib.AU3_Send("terrain.setTexture(" + index + ", \"" + texName + "\"," + height + "){ENTER}", 0);

        skp.getDisplay();
        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        tex[index] = texName;


        lib.AU3_WinActivate(EDITOR_TITLE, "");
        return true;
    }//end of setTexture()

    
    public boolean setTexture(float num, String texName) throws IOException {
        //set texture of a flat terrain with no texture levels
        int height = 1;
        if (num <= 0) {
            try {
                throw new TerrainException("Texture level must be greater than zero");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (num > level) {
            try {
                throw new TerrainException("Texture level is out of range");
            } catch (TerrainException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int index = (int) (num * 2 - 2);
        this.height[index] = height;
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
        lib.AU3_Send("terrain.setTexture(" + index + ", \"" + texName + "\"," + height + "){ENTER}", 0);

        skp.getDisplay();
        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        tex[index] = texName;

        lib.AU3_WinActivate(EDITOR_TITLE, "");
        return true;
    }//end of setTexture()

    
    public String blendTex(String tex1, String tex2) throws IOException {
        
        //create and write a merged texture file out of two given texture files
        File path = new File("C:/Program Files (x86)/SketchUp/SketchUp 2013/Plugins");
        BufferedImage image = ImageIO.read(new File(path, tex1));
        BufferedImage overlay = ImageIO.read(new File(path, tex2));

        int w = Math.min(image.getWidth(), overlay.getWidth());
        int h = Math.min(image.getHeight(), overlay.getHeight());
        image = resize(image, w, h);
        overlay = resize(overlay, w, h);
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        float alpha = (float) 0.5;
        Graphics2D g = (Graphics2D) combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g.drawImage(overlay, 0, 0, null);

        String name = new String("combined" + texIndex + ".png");
        ImageIO.write(combined, "PNG", new File(path, name));
        System.out.println("success");
        texIndex++;
        return name;
    }//end of blendTex()
    

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        //resize a texture image (to be at the same size before merging) 
        int w = img.getWidth();
        int h = img.getHeight();
        if (newW == w && newH == h) {
            return img;
        }
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }//end of resize()


    public boolean addModel(Model model) {
        //add a Model object to a 3D scene on SketchUp
        String[] disp;
        int width, length;
        Point2f position = model.getPosition();
        float z = getHeight(position.x, position.y) + model.getOffsetZ();
        model.setZ(z);
        String modelName = "\"" + model.getName() + "\"";
        String file = "\"" + model.getFile() + "\"";
        float scale = model.getScale();

        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
        lib.AU3_Send("anim.add_model(" + modelName + ", " + file + "," + scale + ", " + model.getRotation() + ", " + position.x + ", " + position.y + ", " + z + ", " + model.isMovable() + "){ENTER}", 0);
        lib.AU3_Sleep(1000);
        disp = skp.getDisplay()[1].split(", ");
        width = Integer.parseInt(disp[0].trim());
        length = Integer.parseInt(disp[1].trim());
        model.setWidth(width);
        model.setLength(length);

        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        lib.AU3_Sleep(1000);
        if (!model.isMovable()) {
            this.models.add(model);
        } else {
            this.movModels.add(model);
        }
        lib.AU3_WinActivate(EDITOR_TITLE, "");
        return true;
    }//end of addModel()

    
    public boolean addUserModel(UserWrapper user) {

        //add the user model, which can be attahced by the camera, to a 3D scene
        String[] disp;
        int width = 0, length = 0;
        String name = "\"" + user.getName() + "\"";
        String buff = "\"" + user.getFile() + "\"";
        Point2f position = user.getPosition();
        float z = getHeight(position.x, position.y) + user.getOffsetZ();
        user.setZ(z);
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
        lib.AU3_Send("anim.add_model(" + name + ", " + buff + "," + user.getScale() + ", " + user.getRotation() + ", " + position.x + ", " + position.y + ", " + z + ", " + user.isMovable() + "){ENTER}", 0);

        disp = skp.getDisplay()[1].split(", ");
        width = Integer.parseInt(disp[0].trim());
        length = Integer.parseInt(disp[1].trim());
        user.setWidth(width);
        user.setLength(length);
        this.movModels.add(user);

        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        lib.AU3_Sleep(1000);
        lib.AU3_WinActivate(EDITOR_TITLE, "");

        return true;
    }//end of addUserModel()

    
    private boolean saveTerrain(String file, int size) throws IOException {
        
        //write generated coordinates stored in square[][] to a text file
        BufferedWriter out = new BufferedWriter(new FileWriter(file));

        out.write("l," + size + "");
        out.newLine();
        for (int i = 0; i < square.length; i++) {
            for (int j = 0; j < square.length; j++) {
                out.write(i + "," + j + "," + (int) square[i][j]);
                out.newLine();
            }
        }

        out.close();
        /*   
         try {
         BufferedImage dest = image.getSubimage(0, 0, (int) Math.pow(2, tSize) + 1, (int) Math.pow(2, tSize) + 1);
         ImageIO.write(dest, "bmp", new File(file));
         } catch (IOException ex) {
         Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        return true;
    }//end of saveTerrain()

    
    private void createPolygon() {
        //create an array of polygons in the terrain for Barycentric approach
        int i = 0;
        int j = 0;
        int dist = 30;
        float x, y, z;
        Point3f p0, p1, p2, p3;
        QuadArray polygon, polygon2;
        for (i = 0; i < square.length - 1; i++) {

            for (j = 0; j < square.length - 1; j++) {

                polygon = new QuadArray(4, QuadArray.COORDINATES);
                polygon2 = new QuadArray(4, QuadArray.COORDINATES);
                x = i;
                y = j;
                z = (float) square[i][j];

                p0 = new Point3f(x * dist, y * dist, z);
                p1 = new Point3f(x * dist + dist, y * dist, (float) square[i + 1][j]);
                p2 = new Point3f(x * dist, y * dist + dist, (float) square[i][j + 1]);
                p3 = new Point3f(x * dist + dist, y * dist + dist, (float) square[i + 1][j + 1]);

                polygon.setCoordinate(0, p0);
                polygon.setCoordinate(1, p2);
                polygon.setCoordinate(2, p3);

                polygon2.setCoordinate(0, p0);
                polygon2.setCoordinate(1, p1);
                polygon2.setCoordinate(2, p3);

                polygonAr.add(polygon);
                polygonAr.add(polygon2);

            }
        }
    }//end of createPolygon()

    
    public float getHeight(float x, float y) {
        //find height of a particular position on the terrain by finding which polygon the coordinate is on and then use Bary centric approach to calculate the height
        Point3f c0;
        Point3f c1;
        Point3f c2;
        float height = 0;
        int i = 0;
        while (i < polygonAr.size()) {
            c0 = new Point3f();
            c1 = new Point3f();
            c2 = new Point3f();

            polygonAr.get(i).getCoordinate(0, c0);
            polygonAr.get(i).getCoordinate(1, c1);
            polygonAr.get(i).getCoordinate(2, c2);

            height = calcZ(c0, c1, c2, x, y);
            if (height > -1) {
                index = i;
                return height;
            }
            i++;
        }
        return -1;
    }//end of getHeight()

    
    private float calcZ(Point3f v1, Point3f v2, Point3f v3, float x, float y) {
        //use Barycentric theory to calculate height of a given coordinate on a specific polygon
        float det = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
        float b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / det;
        float b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / det;
        float b3 = 0;

        if ((b1 >= 0) && (b2 >= 0) && (b1 + b2 <= 1)) {
            b3 = 1.0f - b1 - b2;
            return (b1 * v1.z) + (b2 * v2.z) + (b3 * v3.z);
        } else {
            //System.out.println("coordinates is not inside the triangle");
            return -1;
        }
    }//end of calcZ()

    public boolean addShape(Shape shape) {
        //add Shape object to the scene including block and cylinder; a different command is sent to SketchUp accordingly to type of the shape
        Point2f position = shape.getPosition();
        float z = getHeight(position.x, position.y) + shape.getOffsetZ();
        shape.setZ(z);
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");

        if (shape.getType().equals("block")) {

            int width = shape.getWidth();
            int length = shape.getLength();
            int height = shape.getHeight();
            lib.AU3_Send("shape.createBlock(\"" + shape.getName() + "\", " + position.x + ", " + position.y + ", " + z + ", " + width + "," + length + "," + height + "){ENTER}", 0);
        } else {
            int height = shape.getHeight() + shape.getOffsetZ();
            int radius = shape.getRadius();
            lib.AU3_Send("shape.createCylinder(\"" + shape.getName() + "\", " + +position.x + ", " + position.y + ", " + z + ", " + radius + ", " + height + "){ENTER}", 0);
        }
        this.shape.add(shape);
        lib.AU3_Sleep(1000);
        skp.getDisplay();
        lib.AU3_ControlSetText(RC_TITLE, "", "Edit2", "");
        // clear the display

        lib.AU3_Sleep(1000);

        lib.AU3_WinActivate(EDITOR_TITLE, "");
        return true;

    }//end of addShape()

    
    public ArrayList<Shape> getShapes() {
        return shape;
    }//end of getShapes()

    
    public ArrayList<Model> getModels() {
        return models;
    }//end of getModels()

    
    public ArrayList<Model> getDynamicModels() {
        return movModels;
    }//end of getDynamicModels()

    
    public double getResolution() {
        return resolution;
    }// end of getResolution()

    
    public boolean startAnimation() {
        //always start animations from time = 0
        startAnimationAt(0);
        return true;
    }//end of startAnimation()

    
    public boolean startAnimationAt(int time) {
        //start animations at specific time
        for (int i = 0; i < movModels.size(); i++) {

            movModels.get(i).loadAnimation();

        }
        lib.AU3_WinActivate(SKP_TITLE, "");
        lib.AU3_ControlFocus(RC_TITLE, "", "Edit1");
        lib.AU3_Send("anim.animateModels(" + time + "){ENTER}", 0);
        return true;
    }//end of startAnimationAt()

    
    public boolean isCollision(Step step, int index) {
        //detect whether there is a collision with static models along the path of the movement steps 
        float x0 = step.getX();
        float y0 = step.getY();
        float x1, y1;
        int width = 0, length = 0;

        for (int i = 0; i < movModels.size(); i++) {

            if (i != index) {
                x1 = movModels.get(i).getPosition().x;
                y1 = movModels.get(i).getPosition().y;
                width = movModels.get(i).getWidth();
                length = movModels.get(i).getLength();

                if (x0 >= x1 - width && x0 <= x1 + width) {
                    if (y0 >= y1 - length && y0 <= y1 + length) {
                        System.out.println("collision detected");
                        return true;
                    }
                }
            }
        }

        return false;
    }//end of isCollision()


    public void setListener(AnimationListener lis) {
        this.lis = lis;
    }//end of setListener()


    public Step[] genStepsArray(Point2f start, Point2f dest, double sAngle, double dAngle, int startTime, int stopTime) {
       
        //generate movement steps array from start, destination, start angle, destination angle, start time, and stop time of the animation
        try {
            return new Movements().genStepsArray(this, start, dest, sAngle, dAngle, startTime, stopTime);
        } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MovementException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }//end of genStepsArray()

    
    public Step[] genStepsArray(Point2f start, Point2f dest, int startTime, int stopTime) {
        //generate steps array only from start, destination, start time and stop time
        try {

            return new Movements().genStepsArray(this, start, dest, 0, 0, startTime, stopTime);
        } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MovementException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }//end of genStepsArray()
   
    
    public Step[] genStepsArray(Point2f start, Point2f dest, double dAngle, int startTime, int stopTime) {
        //generate steps array without a start angle 
        try {

            return new Movements().genStepsArray(this, start, dest, 0, dAngle, startTime, stopTime);
        } catch (IOException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MovementException ex) {
            Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }//end of genStepsArray()

    
    public Model getModel(String name) {
        for (Model model : movModels) {
            if (model.getName().equals(name)) {
                return model;
            }
        }
        return null;
    }//end of getModel()

    
    public boolean enableShadow() {
        lib.AU3_Send("Sketchup.send_action 10602{ENTER}", 0);
        return true;
    }// end of enableShadow()

    
    public void loadStatics(String fnm) 
     /* The format of the input lines are:
     b block-name x y width length height rotation texture-fnm [roof-texture-fnm ]
     c cylinder-name x y radius height rotation texture-fnm
     m model-fnm model-name x y scale rotation
     and blank lines and comment lines.
     */ {
        System.out.println("Reading file: " + fnm);
        try {
            BufferedReader br = new BufferedReader(new FileReader(fnm));
            String line;
            String[] toks;
            StaticShape ss = null;
            int shapeLineCount = 1;     // only counts lines with shape data on them
            while ((line = br.readLine()) != null) {
                if (line.length() == 0) // blank line
                {
                    continue;
                }
                if (line.startsWith("//")) // comment
                {
                    continue;
                }

                // System.out.println("Line: " + line);
                toks = line.split("\\s+");
                if (toks == null) {
                    System.out.println("Could not tokenize shape " + shapeLineCount + ": \"" + line + "\"");
                    shapeLineCount++;
                    continue;
                }

                char staticCh = toks[0].toLowerCase().charAt(0);
                ss = null;
                if (staticCh == 'b') {
                    createBlock(toks, shapeLineCount);
                } else if (staticCh == 'c') {
                    createCylinder(toks, shapeLineCount);
                } else if (staticCh == 'm') {

                    createModel(toks, shapeLineCount);

                } else {
                    System.out.println("Did not recognize static type for shape " + shapeLineCount
                            + ": \"" + line + "\"");
                }
                shapeLineCount++;
                if (ss != null) {
                    statics.add(ss);
                }
            }
            br.close();
            System.out.println("Number of static shapes created: " + statics.size());
        } catch (IOException e) {
            System.out.println("Error reading file: " + fnm);
        }
    }  // end of loadInfo()

    
    private void createBlock(String[] toks, int slc) /* Extract data for toks of the form:
     b block-name x y width length height rotation texture-fnm [roof-texture-fnm ]
     */ {
        if ((toks.length < 9) || (toks.length > 10)) {
            System.out.println("Wrong number of block toks; skipping shape " + slc);
        }

        String roofFnm = ((toks.length == 10) ? toks[9] : null);
        StaticBlock blockInfo = new StaticBlock(toks[1], getD(toks[2]), getD(toks[3]),
                getD(toks[4]), getD(toks[5]), getD(toks[6]),
                getD(toks[7]), toks[8], roofFnm);
        Block block = new Block(new Point2f(blockInfo.getX(), blockInfo.getY()), (int) blockInfo.getWidth(), (int) blockInfo.getLength(), (int) blockInfo.getHeight());
        addShape(block);
        if (toks.length == 10) {
            block.texture(blockInfo.getTextureFnm(), blockInfo.getRoofFnm());
        } else {
            block.texture(blockInfo.getTextureFnm());
        }
    }  // end of getBlockInfo()

    
    private void createCylinder(String[] toks, int slc) /* Extract data for toks of the form:
     c cylinder-name x y radius height rotation texture-fnm
     */ {
        if (toks.length != 8) {
            System.out.println("Wrong number of cylinder toks; skipping shape " + slc);

        } else {
            StaticCylinder cylinderInfo = new StaticCylinder(toks[1], getD(toks[2]), getD(toks[3]),
                    getD(toks[4]), getD(toks[5]),
                    getD(toks[6]), toks[7]);
            Cylinder cylinder = new Cylinder(new Point2f(cylinderInfo.getX(), cylinderInfo.getY()), (int) cylinderInfo.getRadius(), (int) cylinderInfo.getHeight());
            addShape(cylinder);
            cylinder.texture(cylinderInfo.getTextureFnm());
        }
    }  // end of getCylinderInfo()

    
    private void createModel(String[] toks, int slc) /* Extract data for toks of the form:
     m model-fnm model-name x y scale rotation
     */ {
        if (toks.length != 7) {
            System.out.println("Wrong number of model toks; skipping shape " + slc);
        } else {

            StaticModel modelInfo = new StaticModel(toks[2], getD(toks[3]), getD(toks[4]),
                    getD(toks[5]), getD(toks[6]),
                    toks[1]);

            Model model = new Model(modelInfo.getModelFnm(), new Point2f(modelInfo.getX(), modelInfo.getY()), (float) modelInfo.getScale(), modelInfo.getRotation(), false);
            addModel(model);
        }
    }  // end of getModelInfo()

    
    private float getD(String token) {
        float num = 0;
        try {
            num = Float.parseFloat(token);
        } catch (NumberFormatException ex) {
            System.out.println("Incorrect format for " + token);
        }
        return num;
    }  // end of getD()
    
    
    public ArrayList<QuadArray> getPolygonArray(){
        return polygonAr;
    }
}
