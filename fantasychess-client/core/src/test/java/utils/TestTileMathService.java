package utils;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.bteam.common.models.Vector2D;
import com.bteam.fantasychess_client.utils.TileMathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class TestTileMathService {

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

        assertEquals(tileMathService.getMapCenter().getX(), 0);
        assertEquals(tileMathService.getMapCenter().getY(), 0);

        Vector2 result = tileMathService.gridToWorld(0,0);
        assertEquals(0,result.x,0.1);
        assertEquals(200,result.y,0.1);

        result = tileMathService.gridToWorld(8,8);
        assertEquals(0,result.x,0.1);
        assertEquals(-200,result.y,0.1);

        result = tileMathService.gridToWorld(8,0);
        assertEquals(400,result.x,0.1);
        assertEquals(0,result.y,0.1);

        result = tileMathService.gridToWorld(0,8);
        assertEquals(-400,result.x,0.1);
        assertEquals(0,result.y,0.1);
    }

    @Test
    void testWorldToGrid(){
        TileMathService tileMathService = new TileMathService(9,9, basicTiledMap,32,16);

        Vector2 mouse = new Vector2(144,0);
        Vector2D result = tileMathService.worldToGrid(mouse.x, mouse.y);
        assertEquals(8, result.getX(),0.1);
        assertEquals(0, result.getY(),0.1);

        mouse = new Vector2(-1233,10);
        result = tileMathService.worldToGrid(mouse.x,mouse.y);
        assertNull(result);
    }


    @Test
    void testLineIntersection() {
        TileMathService tileMathService = new TileMathService(9,9, basicTiledMap,32,16);

        //normal case
        Vector2 point1 = new Vector2(2, 8);
        Vector2 orientation1 = point1.cpy().sub(new Vector2(8, 2));
        Vector2 point2 = new Vector2(2, 2);
        Vector2 orientation2 = point2.cpy().sub(new Vector2(6, 8));
        Vector2 result1 = tileMathService.lineIntersection(point1, orientation1, point2, orientation2);
        Vector2 expected1 = new Vector2(4.4f,5.6f);
        assertEquals(expected1, result1);

        // first test but direction vertices in other direction
        point1 = new Vector2(2, 8);
        orientation1 = new Vector2(8, 2).sub(point1.cpy());
        point2 = new Vector2(2, 2);
        orientation2 = new Vector2(6, 8).sub(point2.cpy());
        Vector2 result2 = tileMathService.lineIntersection(point1, orientation1, point2, orientation2);
        Vector2 expected2 = new Vector2(4.4f,5.6f);
        assertEquals(expected2, result2);

        // first line has both points before intersection
        point1 = new Vector2(2, 8);
        orientation1 = point1.cpy().sub(new Vector2(4, 6));
        point2 = new Vector2(2, 2);
        orientation2 = point2.cpy().sub(new Vector2(12, 4));
        Vector2 result3 = tileMathService.lineIntersection(point1, orientation1, point2, orientation2);
        Vector2 expected3 = new Vector2(7,3);
        assertEquals(expected3, result3);


        // parallel lines
        point1 = new Vector2(2, 8);
        orientation1 = point1.cpy().sub(new Vector2(4, 6));
        point2 = new Vector2(6, 8);
        orientation2 = point2.cpy().sub(new Vector2(8, 6));
        Vector2 result4 = tileMathService.lineIntersection(point1, orientation1, point2, orientation2);
        assertNull(result4);
    }

    @Test
    void testPercentOnLine() {
        TileMathService tileMathService = new TileMathService(9,9, basicTiledMap,32,16);

        Vector2 start1 = new Vector2(10,30);
        Vector2 end1 = new Vector2(50,10);
        Vector2 point1 = new Vector2(30,20);
        float result1 = tileMathService.percentOnLine(point1, start1, end1);
        assertEquals(0.5f,result1,0.01);

        Vector2 start2 = new Vector2(10,30);
        Vector2 end2 = new Vector2(50,10);
        Vector2 point2 = new Vector2(-10,40);
        float result2 = tileMathService.percentOnLine(point2, start2, end2);
        assertEquals(0f,result2,0.01);

        Vector2 start3 = new Vector2(10,30);
        Vector2 end3 = new Vector2(50,10);
        Vector2 point3 = new Vector2(70,0);
        float result3 = tileMathService.percentOnLine(point3, start3, end3);
        assertEquals(1.0f,result3,0.01);

        Vector2 start4 = new Vector2(10,20);
        Vector2 end4 = new Vector2(60,20);
        Vector2 point4 = new Vector2(20,20);
        float result4 = tileMathService.percentOnLine(point4, start4, end4);
        assertEquals(0.2f,result4,0.01);
    }

    @Test
    void testGetMapCenter() {
        TileMathService tileMathService1 = new TileMathService(9,9,basicTiledMap,32,16);

        assertEquals(0, tileMathService1.getMapCenter().getX());
        assertEquals(0, tileMathService1.getMapCenter().getY());

    }
}
