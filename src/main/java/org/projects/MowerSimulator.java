package org.projects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MowerSimulator {

    private final String fileName;


    public MowerSimulator(String fileName) {
        this.fileName = fileName;
    }
    //String fileName = "src/main/resources/instructions.txt";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java -jar MowerSimulator.jar <filename>");
            System.exit(1);
        }

        MowerSimulator simulator = new MowerSimulator(args[0]);
        List<Mower> mowers = simulator.executeInstructionsFromFile();

        for (Mower mower : mowers) {
            System.out.println(mower.getPosX() + " " + mower.getPosY() + " " + mower.getDirection());
        }
    }

    public List<Mower> executeInstructionsFromFile() throws IOException {
        List<Mower> mowers = new ArrayList<>();
        Area area = new Area();
        Path path = Paths.get(this.fileName);
        if (!Files.exists(path) || Files.size(path) == 0) {
            throw new IOException("File " + this.fileName + " does not exist or is empty");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) {
            String line;
            String[] firstLine = br.readLine().split(" ");

            if (firstLine.length != 2) {
                throw new IllegalArgumentException("File " + this.fileName + " has invalid area dimensions.");
            }

            try {
                area.setWidth(Integer.parseInt(firstLine[0]) + 1);
                area.setLength(Integer.parseInt(firstLine[1]) + 1);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("File " + this.fileName + " contains invalid area dimensions.");
            }

            while ((line = br.readLine()) != null) {
                String[] position = line.split(" ");
                if (position.length != 3) {
                    throw new IllegalArgumentException("File " + this.fileName + " has invalid mower position.");
                }

                int posX, posY;

                try {
                    posX = Integer.parseInt(position[0]);
                    posY = Integer.parseInt(position[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("File " + this.fileName + " contains invalid mower position.");
                }

                String direction = position[2];
                if (direction.length() != 1) {
                    throw new IllegalArgumentException("File " + this.fileName + " has invalid mower direction.");
                }

                String instructions = br.readLine();
                if (instructions == null) {
                    throw new IllegalArgumentException("File " + this.fileName + " missing instructions for mower.");
                }

                Mower mower = new Mower(area, posX, posY, direction.charAt(0));
                mower.move(instructions);
                mowers.add(mower);
            }
            return mowers;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mowers;
    }
}

