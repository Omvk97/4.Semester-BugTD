package dk.sdu.mmmi.commonmap;

public class MapWave {
    private String enemyType;
    private int enemyAmount;
    private int waveNumber;

    public MapWave(String enemyType, int enemyAmount, int waveNumber) {
        this.enemyType = enemyType;
        this.enemyAmount = enemyAmount;
        this.waveNumber = waveNumber;
    }

    public String getEnemyType() {
        return enemyType;
    }

    public int getEnemyAmount() {
        return enemyAmount;
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    @Override
    public String toString() {
        return "MapWave{" +
                "enemyType='" + enemyType + '\'' +
                ", enemyAmount=" + enemyAmount +
                ", waveNumber=" + waveNumber +
                '}';
    }
}
