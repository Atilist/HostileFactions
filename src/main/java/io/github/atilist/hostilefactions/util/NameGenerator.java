package io.github.atilist.hostilefactions.util;

import java.util.Random;

public class NameGenerator {
    private static final Random RANDOM = new Random();

    private static final String[] DRONE_NAMES = new String[]{
            "Ardian", "Leon", "Elon", "Donald", "Ronald", "Jason", "Andrea", "Gerwin", "Nika", "Simon",
            "Ralf", "Sabine", "Jeffrey", "Meo", "Tim", "Julian", "Lennart", "Friedrich", "Armin", "Pitt",
            "Jens", "Fabian", "Jonas", "Ole", "Jeanette", "Werner", "Angela", "Vladimir", "Marie", "Emmanuel",
            "Alice", "Olaf", "Helmut", "Erich", "Matthew", "Alexander", "Evgenij", "Thomas"
    };

    public static String generateDroneName() {
        return getRandomDroneFirstName() + " " + getRandomDroneFirstName() + " " + getRandomDroneFirstName();
    }

    private static String getRandomDroneFirstName() {
        return DRONE_NAMES[RANDOM.nextInt(DRONE_NAMES.length)];
    }
}
