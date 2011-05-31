package splash;

/** This code snippet was taken from freecodesnippets.com*/



/*
 *      LocalEyeApp.java 1.0 99/04/12
 *
 * Copyright (c) 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

/*
 * This program demonstrates the difference between Local Eye Lighting
 * and Infinite Eye Lighting.  To see the difference, you need to
 * comment out the appropriate line in the code, recompile and rerun
 * the application.
 *
 * This version of the program is different than the one used to
 * generate images for the tutorial document.  This this version
 * both a directional and point light are included in the scene.
 * the directional light is red, the point light is white.  The spheres
 * are blue (diffuse) with white specular material.
 *
 * The red specular reflections are from the directional light.
 * The white specular reflections are from the point light.
 * The blue diffuse reflection is from the directional light.
 * 
 * This application (or a version of it) generated one or more
 * of the images in Chapter 6 of Getting Started with the Java 3D API.
 * The Java 3D Turtorial.
 *
 * See <a href="http://www.sun.com/desktop/java3d/collateral" title="http://www.sun.com/desktop/java3d/collateral">http://www.sun.com/desktop/java3d/collateral</a> for more information.
 *
 */


import java.applet.Applet;
import java.awt.BorderLayout;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * LocalEyeApp creates a single plane with texture mapping.
 */
public class LocalEyeApp extends Applet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	TransformGroup createTG(float x, float y, float z){
        Vector3f position = new Vector3f(x, y, z);
        Transform3D translate = new Transform3D();
        translate.set(position);
        TransformGroup trans1 = new TransformGroup(translate);
        return trans1;
    }

    Appearance createMatAppear(Color3f dColor, Color3f sColor, float shine) {

        Appearance appear = new Appearance();
        Material material = new Material();
        material.setDiffuseColor(dColor);
        material.setSpecularColor(sColor);
        material.setShininess(shine);
        appear.setMaterial(material);

        return appear;
    }


  public LocalEyeApp (){
    setLayout(new BorderLayout());
    Canvas3D c = new Canvas3D(null);
    add("Center", c);

    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
    Color3f red   = new Color3f(1.0f, 0.0f, 0.0f);
    Color3f blue  = new Color3f(0.0f, 0.0f, 1.0f);
 
    BranchGroup scene = new BranchGroup();

    TransformGroup trans11 = createTG(-0.7f, 0.7f, -0.5f);
    scene.addChild(trans11);
    trans11.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    TransformGroup trans12 = createTG(0.0f, 0.7f, -0.5f);
    scene.addChild(trans12);
    trans12.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    TransformGroup trans13 = createTG(0.7f, 0.7f, -0.5f);
    scene.addChild(trans13);
    trans13.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    TransformGroup trans21 = createTG(-0.7f, 0.0f, -0.5f);
    scene.addChild(trans21);
    trans21.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    TransformGroup trans22 = createTG(0.0f, 0.0f, -0.5f);
    scene.addChild(trans22);
    trans22.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    TransformGroup trans23 = createTG(0.7f, 0.0f, -0.5f);
    scene.addChild(trans23);
    trans23.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));


    TransformGroup trans31 = createTG(-0.7f, -0.7f, -0.5f);
    scene.addChild(trans31);
    trans31.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    TransformGroup trans32 = createTG(0.0f, -0.7f, -0.5f);
    scene.addChild(trans32);
    trans32.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    TransformGroup trans33 = createTG(0.7f, -0.7f, -0.5f);
    scene.addChild(trans33);
    trans33.addChild(new Sphere(0.3f, Sphere.GENERATE_NORMALS, 60,
                    createMatAppear(blue, white, 1000.0f)));

    AmbientLight lightA = new AmbientLight();
    lightA.setInfluencingBounds(new BoundingSphere());
    scene.addChild(lightA);

    DirectionalLight lightD1 = new DirectionalLight();
    lightD1.setInfluencingBounds(new BoundingSphere());
    Vector3f direction = new Vector3f(0.0f, 0.0f, -1.0f);
    direction.normalize();
    lightD1.setDirection(direction);
    lightD1.setColor(red);
    scene.addChild(lightD1);

    PointLight lightP1 = new PointLight();
    lightP1.setInfluencingBounds(new BoundingSphere());
    Point3f position = new Point3f(0.0f, 0.0f, 1.0f);
    lightP1.setPosition(position);
    scene.addChild(lightP1);

    Background background = new Background();
    background.setApplicationBounds(new BoundingSphere());
    background.setColor(white);
    scene.addChild(background);

    SimpleUniverse u = new SimpleUniverse(c);

    // This will move the ViewPlatform back a bit so the
    // objects in the scene can be viewed.
    u.getViewingPlatform().setNominalViewingTransform();

    //Enable Local Eye Lighting
    u.getViewer().getView().setLocalEyeLightingEnable(true);

    u.addBranchGraph(scene);
  }
  
  public static void main(String argv[])
  {
    System.out.println("This scene has a red directional light and white point light.");
    System.out.println("The spheres are blue (diffuse) with white specular material.");
    System.out.println("The red specular reflections are from the directional light.");
    System.out.println("The white specular reflections are from the point light.");
    System.out.println("The blue diffuse reflection is from the point light.");
    new MainFrame(new LocalEyeApp(), 256, 256);
  }
}



