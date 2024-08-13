package org.projects;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
public class MowerSimulatorTest {

    @Test
    public void testReadFileWithValidContent() throws IOException {

        String fileName = "src/test/resources/instructions.txt";
        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        List<Mower> mowers = mowerSimulator.executeInstructionsFromFile();

        assertEquals(1, mowers.get(0).getPosX());
        assertEquals(3, mowers.get(0).getPosY());
        assertEquals('N', mowers.get(0).getDirection());

        assertEquals(5, mowers.get(1).getPosX());
        assertEquals(1, mowers.get(1).getPosY());
        assertEquals('E', mowers.get(1).getDirection());
    }
}
