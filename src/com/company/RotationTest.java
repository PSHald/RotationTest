package com.company;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class RotationTest extends Applet implements ActionListener, KeyListener {
    private static Point3d GazePoint = new Point3d(0,1,2);
    private static Point3d ViewerLocation = new Point3d(0,0,0);
    private Button upBtn = new Button("Up");
    private Button downBtn = new Button("down");
    private Button leftBtn = new Button("left");
    private Button rightBtn = new Button("right");

    private double xLoc = 0.0;
    private double yLoc = 0.0;
    private double zLoc = 0.0;

    private TransformGroup transformGroup = new TransformGroup();
    private Transform3D transform = new Transform3D();



    public RotationTest() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        //step 1 Create a Canvas3D object and add it to the Applet panel.
        add(canvas);
        canvas.addKeyListener(this);

        /*JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(upBtn);
        panel.add(downBtn);
        panel.add(leftBtn);
        panel.add(rightBtn);

        upBtn.addActionListener(this);
        downBtn.addActionListener(this);
        leftBtn.addActionListener(this);
        rightBtn.addActionListener(this);
        upBtn.addKeyListener(this);
        downBtn.addKeyListener(this);
        leftBtn.addKeyListener(this);
        rightBtn.addKeyListener(this);*/

        //step 2 Create a BranchGroup as the root of the scene branch graph.
        BranchGroup scene = CreateScene();

        //Step 5 Call the universe builder utility function
        SimpleUniverse simpleUniverse = new SimpleUniverse(canvas);
        simpleUniverse.getViewingPlatform().setNominalViewingTransform();
        transform.lookAt(GazePoint, ViewerLocation, new Vector3d( 2,1,0));
        transformGroup = simpleUniverse.getViewingPlatform().getMultiTransformGroup().getTransformGroup(0);
        //step 6 Insert the scene branch graph into the universe builder's Locale.
        simpleUniverse.addBranchGraph(scene);


        //Vindue for applikation
        MainFrame frame = new MainFrame(this, 1280, 960);
        frame.setTitle("Rotation Test");
        frame.setVisible(true);
    }
    //Næste skridt, sæt X, Y og Z til at være kordinater som rotere bolden, ikke universet.
    private void rotate(){
        Transform3D delta = new Transform3D();
        Vector3d vector = new Vector3d(xLoc, yLoc, zLoc);
        transform.lookAt(GazePoint, ViewerLocation, new Vector3d( 2,1,0));
        delta.setEuler(vector);
        transform.mul(delta);
        transform.invert();
        transformGroup.setTransform(transform);
    }

    private BranchGroup CreateScene(){
        //step 2 Create a BranchGroup as the root of the scene branch graph.
        BranchGroup scene = new BranchGroup();

        Background background = new Background(new Color3f(0.2f,0.2f,1));
        BoundingSphere Bsphere = new BoundingSphere(new Point3d(0, 0, 0),1000);
        background.setApplicationBounds(Bsphere);
        scene.addChild(background);

        String localPath = new File("").getAbsolutePath();
        TextureLoader loader = new TextureLoader(localPath+"/src/images/6d6bb0f697c68b30634cf4b38769784838de1c32_full.jpg", "RGB" , new Container());
        //step 3  Construct a Shape3D node with a TransformGroup node above it.
        Appearance ap = new Appearance();
        ap.setTexture(loader.getTexture());
        Sphere sphere = new Sphere(0.2f, Primitive.GENERATE_TEXTURE_COORDS, ap);
        transformGroup.addChild(sphere);


        //Step 4 Attach a RotationInterpolator behavior to the TransformGroup
        // har ikke rigtigt noget formål her og virker stadig uden.

        Transform3D yAxis = new Transform3D();
        Alpha timing = new Alpha(0, 0);
        RotationInterpolator nodeRotator = new RotationInterpolator(timing, transformGroup, yAxis, 0.0f, 0.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0),0);
        nodeRotator.setSchedulingBounds(bounds);
        transformGroup.addChild(nodeRotator);

        scene.addChild(transformGroup);

        return scene;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case("up"):
                yLoc += 0.1;
                break;
            case("down"):
                yLoc -= 0.1;
                break;
            case("left"):
                xLoc -= 0.1;
                break;
            case("right"):
                xLoc += 0.1;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode){
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                yLoc += 0.1;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                yLoc -= 0.1;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                xLoc -= 0.1;
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                xLoc += 0.1;
                break;
            case KeyEvent.VK_Q:
                zLoc -= 0.1;
                break;
            case KeyEvent.VK_E:
                zLoc += 0.1;
                break;
            default:
                break;
        }

        rotate();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
