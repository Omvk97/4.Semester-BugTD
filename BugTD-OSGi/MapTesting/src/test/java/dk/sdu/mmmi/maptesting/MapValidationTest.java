package dk.sdu.mmmi.maptesting;

import dk.sdu.mmmi.commonmap.MapSPI;
import org.junit.Before;
import org.junit.Test;

public class MapValidationTest {

    MapSPI mapSpi;

    @Before
    public void setup() {
        for (int i = 0; i < 10; i++) {
            System.out.println("@");
        }
    }

    @Test
    public void validateMapSpi() {
        System.out.println("Called!!");
        System.out.println(mapSpi);
        assert(mapSpi != null);
    }


    public void setMapSPI(MapSPI mapSpi) {
        System.out.println("Called");
        this.mapSpi = mapSpi;
    }
}
