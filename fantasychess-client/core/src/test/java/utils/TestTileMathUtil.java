package utils;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.bteam.common.models.Vector2D;
import com.bteam.fantasychess_client.utils.TileMathService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TestTileMathUtil {

    TiledMap basicTiledMap;
    TiledMap nullTiledMap;

    @BeforeEach
    void setup() {
        TiledMap mockedTiledMap = mock(TiledMap.class);
        MapLayers mockLayers = mock(MapLayers.class);
        TiledMapTileLayer mockLayer = mock(TiledMapTileLayer.class);
        when(mockLayer.getWidth()).thenReturn(9);
        when(mockLayer.getHeight()).thenReturn(9);
        when(mockLayer.getTileWidth()).thenReturn(32);
        when(mockLayer.getTileHeight()).thenReturn(16);

        when(mockLayers.getCount()).thenReturn(1);
        when(mockLayers.get(0)).thenReturn(mockLayer);

        when(mockedTiledMap.getLayers()).thenReturn(mockLayers);

        basicTiledMap = mockedTiledMap;

        TiledMap emptyTiledMap = mock(TiledMap.class);
        MapLayers emptyMockLayers = mock(MapLayers.class);
        TiledMapTileLayer emptyMockLayer = mock(TiledMapTileLayer.class);
        when(emptyMockLayer.getWidth()).thenReturn(9);
        when(emptyMockLayer.getHeight()).thenReturn(9);
        when(emptyMockLayer.getTileWidth()).thenReturn(0);
        when(emptyMockLayer.getTileHeight()).thenReturn(0);

        when(emptyMockLayers.getCount()).thenReturn(1);
        when(emptyMockLayers.get(0)).thenReturn(emptyMockLayer);

        when(mockedTiledMap.getLayers()).thenReturn(emptyMockLayers);

        nullTiledMap = emptyTiledMap;

    }

    @Test
    void testGridToWorld(){
        TileMathService tileMathService = new TileMathService(
            9,9, basicTiledMap,100,50
        );

        Assertions.assertEquals(tileMathService.getMapCenter().getX(), 0);
        Assertions.assertEquals(tileMathService.getMapCenter().getY(), 0);

        Vector2 result = tileMathService.gridToWorld(0,0);
        Assertions.assertEquals(0,result.x,0.1);
        Assertions.assertEquals(200,result.y,0.1);

        result = tileMathService.gridToWorld(8,8);
        Assertions.assertEquals(0,result.x,0.1);
        Assertions.assertEquals(-200,result.y,0.1);

        result = tileMathService.gridToWorld(8,0);
        Assertions.assertEquals(400,result.x,0.1);
        Assertions.assertEquals(0,result.y,0.1);

        result = tileMathService.gridToWorld(0,8);
        Assertions.assertEquals(-400,result.x,0.1);
        Assertions.assertEquals(0,result.y,0.1);
    }

    @Test
    void testWorldToGrid(){
        TileMathService tileMathService = new TileMathService(9,9, basicTiledMap,32,16);

        Vector2 mouse = new Vector2(144,18);
        Vector2D result = tileMathService.worldToGrid(mouse.x, mouse.y);
        Assertions.assertEquals(7, result.getX(),0.1);
        Assertions.assertEquals(0, result.getY(),0.1);
    }

    @Test
    void testPointLineProjection() {
        TileMathService tileMathService = new TileMathService(9,9, basicTiledMap,32,16);

        Vector2 start1 = new Vector2(10,40);
        Vector2 end1 = new Vector2(30,0);
        Vector2 project1 = new Vector2(10,20);
        Vector2 result1 = tileMathService.pointLineProjection(start1, end1, project1);
        Vector2 expected1 = new Vector2(18,24);
        Assertions.assertEquals(result1, expected1);

        Vector2 start2 = new Vector2(10,40);
        Vector2 end2 = new Vector2(30,0);
        Vector2 project2 = new Vector2(-10,-10);
        Vector2 result2 = tileMathService.pointLineProjection(start2, end2, project2);
        Vector2 expected2 = new Vector2(26,8);
        Assertions.assertEquals(result2, expected2);

        Vector2 start4 = new Vector2(-40,-10);
        Vector2 end4 = new Vector2(-20,-20);
        Vector2 project4 = new Vector2(10,10);
        Vector2 result4 = tileMathService.pointLineProjection(start4, end4, project4);
        Vector2 expected4 = new Vector2(-8,-26);
        Assertions.assertEquals(result4, expected4);
    }

    @Test
    void testPercentOnLine() {
        TileMathService tileMathService = new TileMathService(9,9, basicTiledMap,32,16);

        Vector2 start1 = new Vector2(10,30);
        Vector2 end1 = new Vector2(50,10);
        Vector2 point1 = new Vector2(30,20);
        float result1 = tileMathService.percentOnLine(point1, start1, end1);
        Assertions.assertEquals(0.5f,result1,0.01);

        Vector2 start2 = new Vector2(10,30);
        Vector2 end2 = new Vector2(50,10);
        Vector2 point2 = new Vector2(-10,40);
        float result2 = tileMathService.percentOnLine(point2, start2, end2);
        Assertions.assertEquals(0f,result2,0.01);

        Vector2 start3 = new Vector2(10,30);
        Vector2 end3 = new Vector2(50,10);
        Vector2 point3 = new Vector2(70,0);
        float result3 = tileMathService.percentOnLine(point3, start3, end3);
        Assertions.assertEquals(1.0f,result3,0.01);

        Vector2 start4 = new Vector2(10,20);
        Vector2 end4 = new Vector2(60,20);
        Vector2 point4 = new Vector2(20,20);
        float result4 = tileMathService.percentOnLine(point4, start4, end4);
        Assertions.assertEquals(0.2f,result4,0.01);
    }

    @Test
    void testGetMapCenter() {
        TileMathService tileMathService1 = new TileMathService(9,9,basicTiledMap,32,16);

        Assertions.assertEquals(tileMathService1.getMapCenter().getX(), 0);
        Assertions.assertEquals(tileMathService1.getMapCenter().getY(), 0);

    }
}
