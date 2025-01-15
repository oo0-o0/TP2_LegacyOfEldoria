package game.zelda.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Map 
{
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	
	
	public Map() {
		map = new TmxMapLoader().load("../assets/MapAssets/LegacyOfEldoriaMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1.7f);
		 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 2000, 1200); 
	   	camera.position.set(980, 610, 0); 
	    camera.update();     
	}
	
	public void renderMapOnScreen()
	{
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
	
	
    public MapLayers getLayers() {
        return map.getLayers();
    }
}
