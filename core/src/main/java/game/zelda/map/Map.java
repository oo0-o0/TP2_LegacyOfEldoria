package game.zelda.map;

import java.awt.Polygon;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Map {

	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera camera;
	
	
	public Map() {
		map = new TmxMapLoader().load("../assets/MapAssets/LegacyOfEldoriaMap.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1.6f);
		 
		camera = new OrthographicCamera();
	    camera.setToOrtho(false, 1900, 1100); // Ajuste a largura e altura conforme a tela
	    camera.position.set(800, 400, 0); // Câmera na posição (0, 0)
	    camera.update();     
	}
	
	public void renderMapOnScreen(){
		mapRenderer.render();
		mapRenderer.setView(camera);
	}

	public MapLayer getLayers() {
		return map.getLayers().get(5);
	}
	
	
}
