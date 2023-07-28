package model;

public enum Weather {
    SUNNY("SUNNY", 4),
    RAINY("RAINY", 6),
    SNOWY("SNOWY", 9),
    TORNADO("TORNADO", 15);


    private final int damageValue;
    private final String stringValue;

    public int getDamageValue() {
        return damageValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    Weather(String stringWeather, int damageIncrease) {
        stringValue = stringWeather;
        damageValue = damageIncrease;
    }

    @Override
    public String toString() {
        return "Hava durumu: " + stringValue;
    }
}
