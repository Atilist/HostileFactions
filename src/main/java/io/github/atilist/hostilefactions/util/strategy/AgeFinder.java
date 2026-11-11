package io.github.atilist.hostilefactions.util.strategy;

public class AgeFinder {
    public static Age getAge(int ageOrdinal) {
        return switch (ageOrdinal) {
            case 1 -> Age.WOOD;
            case 2 -> Age.STONE;
            default -> Age.DARK;
        };
    }

    public static Age getNextAge(Age age) {
        Age nextAge = getAge(age.ordinal() + 1);
        if (nextAge.ordinal() < age.ordinal()) {
            return age;
        }
        return nextAge;
    }
}
