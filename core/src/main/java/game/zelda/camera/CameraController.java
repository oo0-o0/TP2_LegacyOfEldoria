package game.zelda.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class CameraController 
{
    private OrthographicCamera mainCamera;
    private OrthographicCamera miniMapCamera;
    private OrthographicCamera uiCamera;

    private Vector2 targetPosition;
    private float lerpFactor;

    public CameraController(float viewportWidth, float viewportHeight) 
    {
        this.mainCamera = new OrthographicCamera(viewportWidth, viewportHeight);
        this.mainCamera.zoom = 0.65f; 
        this.mainCamera.update();

        this.miniMapCamera = new OrthographicCamera(viewportWidth * 2, viewportHeight * 2);
        this.miniMapCamera.update();

        this.targetPosition = new Vector2(mainCamera.position.x, mainCamera.position.y);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false);
    }

    public void update(Vector2 playerPosition) 
    {
        updateMainCamera(playerPosition);
        updateMiniMapCamera(playerPosition, lerpFactor, lerpFactor);

        uiCamera.update();
    }

    public OrthographicCamera getUICamera() 
    {
        return uiCamera;
    }

    private void updateMainCamera(Vector2 target) 
    {
        mainCamera.position.x = target.x;
        mainCamera.position.y = target.y;
    
        mainCamera.update();
    }    

    private void updateMiniMapCamera(Vector2 target, float worldWidth, float worldHeight) 
    {
        miniMapCamera.position.set(target.x, target.y, 0);

        float halfWidth = miniMapCamera.viewportWidth / 2f;
        float halfHeight = miniMapCamera.viewportHeight / 2f;

        miniMapCamera.position.x = MathUtils.clamp(miniMapCamera.position.x, halfWidth, worldWidth - halfWidth);
        miniMapCamera.position.y = MathUtils.clamp(miniMapCamera.position.y, halfHeight, worldHeight - halfHeight);

        miniMapCamera.update();
    }

    public OrthographicCamera getMainCamera() 
    {
        return mainCamera;
    }

    public OrthographicCamera getMiniMapCamera() 
    {
        return miniMapCamera;
    }
}