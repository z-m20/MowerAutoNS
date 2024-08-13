package org.projects;


import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Mower {

    private static final Logger logger = LoggerFactory.getLogger(Mower.class);

    Area area;
    int posX, posY;  // Mower position
    char direction;

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
                        System.out.println("Invalid Direction");
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
                System.out.println("invalid Instruction");

        }

        logger.debug("Instruction {} executed: current pos is ({}, {}, {}).", instruction, getPosX(), getPosY(), getDirection());

    }

}
