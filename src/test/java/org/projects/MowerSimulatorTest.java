package org.projects;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for simple App.
 */
public class MowerSimulatorTest {

    private final String fileName = "src/test/resources/instructions.txt";
    private final String folder = fileName.substring(0, fileName.lastIndexOf("/"));
    private final File testFile = new File(fileName);


    @BeforeEach
    public void setUpTestFile() throws IOException {
        File testFolder = new File(folder);
        if (!testFolder.exists()) {
            testFolder.mkdir();
        }

        if (testFile.exists()) {
            FileWriter writer = new FileWriter(testFile);
            writer.write("");
            writer.close();
        } else {
            testFile.createNewFile();
        }
    }

    @AfterEach
    public void tearDownTestFile() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }


    @Test
    public void testReadFileWithValidContent() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n1 2 N\nGAGAGAGAA\n3 3 E\nAADAADADDA\n");
        }

        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        List<Mower> mowers = mowerSimulator.executeInstructionsFromFile();

        assertEquals(1, mowers.get(0).getPosX());
        assertEquals(3, mowers.get(0).getPosY());
        assertEquals('N', mowers.get(0).getDirection());

        assertEquals(5, mowers.get(1).getPosX());
        assertEquals(1, mowers.get(1).getPosY());
        assertEquals('E', mowers.get(1).getDirection());
    }


    @Test
    public void testCorruptedSurfaceArguments() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("55A\n6 6 N\n");
        }
        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        assertThrows(IllegalArgumentException.class, () -> mowerSimulator.executeInstructionsFromFile());
    }

    @Test
    public void testCorruptedInitialPosition() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("55A\n6 N N\n");
        }

        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        assertThrows(IllegalArgumentException.class, () -> mowerSimulator.executeInstructionsFromFile());
    }

    @Test
    public void testInitialPositionOutOfSurface() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n6 6 N\n");
        }
        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        assertThrows(IllegalArgumentException.class, () -> mowerSimulator.executeInstructionsFromFile());
    }

    @Test
    public void testMoveBeyondTheSurfaceBoundaries() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n1 5 N\nA");
        }
        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        List<Mower> mowers = mowerSimulator.executeInstructionsFromFile();

        assertEquals(1, mowers.get(0).getPosX());
        assertEquals(5, mowers.get(0).getPosY());
        assertEquals('N', mowers.get(0).getDirection());
    }


    @Test
    public void testIgnoreInvalidDirectionInstructions() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n1 4 N\nTA");
        }
        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        List<Mower> mowers = mowerSimulator.executeInstructionsFromFile();

        assertEquals(1, mowers.get(0).getPosX());
        assertEquals(5, mowers.get(0).getPosY());
        assertEquals('N', mowers.get(0).getDirection());
    }

    @Test
    public void testWithNoInstructions() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n1 4 D\n");
        }
        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        assertThrows(IllegalArgumentException.class, () -> mowerSimulator.executeInstructionsFromFile());
    }

    @Test
    public void testMissingMowerInitialLine() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n");
        }

        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        assertThrows(IllegalArgumentException.class, () -> mowerSimulator.executeInstructionsFromFile());
    }

    @Test
    public void testEmptyInputFile() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("");
        }

        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        assertThrows(IOException.class, () -> mowerSimulator.executeInstructionsFromFile());
    }


    @Test
    public void testLoadANonExistentFile() throws IOException {
        if (testFile.exists()) {
            testFile.delete();
        }
        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        assertThrows(IOException.class, () -> mowerSimulator.executeInstructionsFromFile());
    }

    // we suppose that multiple mower can be located at the same position at the same time
    @Test
    public void testLargeInputData() throws IOException {
        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n");
            for (int i = 0; i < 10000; i++) {
                writer.write("1 2 N\nGAGAGAGAA\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        List<Mower> mowers = mowerSimulator.executeInstructionsFromFile();

        assertEquals(10000, mowers.size());
        assertEquals(1, mowers.get(9999).getPosX());
        assertEquals(3, mowers.get(9999).getPosY());
        assertEquals('N', mowers.get(9999).getDirection());
    }

    @Test
    public void testDuplicateInputData() throws IOException {

        try (FileWriter writer = new FileWriter(testFile, true)) {
            writer.write("5 5\n1 2 N\nGAGAGAGAA\n1 2 N\nGAGAGAGAA\n");
        }

        MowerSimulator mowerSimulator = new MowerSimulator(fileName);
        List<Mower> mowers = mowerSimulator.executeInstructionsFromFile();

        assertEquals(1, mowers.get(0).getPosX());
        assertEquals(3, mowers.get(0).getPosY());
        assertEquals('N', mowers.get(0).getDirection());

        assertEquals(1, mowers.get(1).getPosX());
        assertEquals(3, mowers.get(1).getPosY());
        assertEquals('N', mowers.get(1).getDirection());
    }

}
