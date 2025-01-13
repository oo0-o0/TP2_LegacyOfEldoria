package game.zelda.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class CameraController 
{
    private OrthographicCamera mainCamera;
    private OrthographicCamera miniMapCamera;

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
    }

    public void update(Vector2 playerPosition, float worldWidth, float worldHeight) 
    {
        updateMainCamera(playerPosition, worldWidth, worldHeight);
        updateMiniMapCamera(playerPosition, worldWidth, worldHeight);
    }

    private void updateMainCamera(Vector2 target, float worldWidth, float worldHeight) 
    {
        float smoothFactor = Math.min(lerpFactor * Gdx.graphics.getDeltaTime() * 60, 1f);

        mainCamera.position.x = MathUtils.lerp(mainCamera.position.x, target.x, smoothFactor);
        mainCamera.position.y = MathUtils.lerp(mainCamera.position.y, target.y, smoothFactor);

        float halfWidth = mainCamera.viewportWidth * mainCamera.zoom / 2f;
        float halfHeight = mainCamera.viewportHeight * mainCamera.zoom / 2f;

        mainCamera.position.x = MathUtils.clamp(mainCamera.position.x, halfWidth, worldWidth - halfWidth);
        mainCamera.position.y = MathUtils.clamp(mainCamera.position.y, halfHeight, worldHeight - halfHeight);

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