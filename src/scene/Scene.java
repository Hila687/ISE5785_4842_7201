package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    private final String sceneName;
    public Geometries geometries= new Geometries();
    private Color BackgroundColor= Color.BLACK;
    private AmbientLight ambientLight= AmbientLight.NONE;

    public  Scene(String sceneName) {
        this.sceneName = sceneName;
    }

    public Scene setBackground(Color backgroundColor) {
        this.BackgroundColor= backgroundColor;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight= ambientLight;
        return this;
    }
}
