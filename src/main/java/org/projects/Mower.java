package org.projects;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@Getter
@Setter
@ToString
public class Mower {

    private static final Logger logger = LoggerFactory.getLogger(Mower.class);

    Area area;
    int posX, posY;  // Mower position
    char direction;

    public Mower(Area area, int posX, int posY, char direction) throws IOException {
        if (posX < 0 || posX >= area.getWidth() || posY < 0 || posY >= area.getLength()) {
            logger.error("Initial position is ({},{},{}) is outside area", posX, posY, direction);
            throw new IllegalArgumentException("Initial position is outside the area ");
        }
        this.area = area;
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
    }

    public void setPosX(int posX) {
        if (posX < 0 || posX >= area.getWidth()) {
            throw new IllegalArgumentException("Can't put mower outside area");
        }
        this.posX = posX;
    }

    public void setPosY(int posY) {
        if (posY < 0 || posY >= area.getWidth()) {
            throw new IllegalArgumentException("Can't put mower outside area");
        }
        this.posY = posY;
    }

    public void move(String instructions) {
        logger.debug("Start position is x={}, y={}, direction={}", posX, posY, direction);

        if (instructions == null) {
            logger.warn("No instructions provided for the current mower ! No mvt will be applied ");
            return;
        }
        for (int i = 0; i < instructions.length(); i++) {
            moveOneStep(instructions.charAt(i));
        }
    }

    public void moveOneStep(char instruction) {
        int nextX = posX;
        int nextY = posY;

        switch (instruction) {
            case 'A':
                switch (direction) {
                    case 'N':
                        nextY++;
                        break;
                    case 'S':
                        nextY--;
                        break;
                    case 'E':
                        nextX++;
                        break;
                    case 'W':
                        nextX--;
                        break;
                    default:
                        logger.warn("Invalid Direction");
                }

                if (nextX >= 0 && nextX < this.area.getWidth() && nextY >= 0 && nextY < this.area.getLength()) {
                    posX = nextX;
                    posY = nextY;
                } else {
                    logger.warn("Instruction to move ({},{}) has been aborted : Mower can't leave its area !", nextX, nextY);
                }
                break;

            case 'G':
                switch (direction) {
                    case 'N':
                        direction = 'W';
                        break;
                    case 'W':
                        direction = 'S';
                        break;
                    case 'S':
                        direction = 'E';
                        break;
                    case 'E':
                        direction = 'N';
                        break;
                    default:
                        logger.warn("Invalid Direction");
                }
                break;
            case 'D':
                switch (this.direction) {
                    case 'N':
                        this.direction = 'E';
                        break;
                    case 'E':
                        this.direction = 'S';
                        break;
                    case 'S':
                        this.direction = 'W';
                        break;
                    case 'W':
                        this.direction = 'N';
                        break;
                    default:
                        logger.warn("Invalid Direction");
                }
                break;
            default:
                logger.warn("invalid Instruction");

        }

        logger.debug("Instruction {} executed: current pos is ({}, {}, {}).", instruction, getPosX(), getPosY(), getDirection());

    }

}
