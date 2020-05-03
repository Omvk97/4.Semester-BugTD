package dk.sdu.mmmi.commonmap;

public class MapWave {
    private String enemyType;
    private int enemyAmount;
    private int waveNumber;
    private int enemyLife;

    public  MapWave(String enemyType, int enemyAmount, int waveNumber, int enemyLife) {
        this.enemyType = enemyType;
        this.enemyAmount = enemyAmount;
        this.waveNumber = waveNumber;
        this.enemyLife = enemyLife;
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

    public int getEnemyLife() { return enemyLife; }

    @Override
    public String toString() {
        return "MapWave{" +
                "enemyType='" + enemyType + '\'' +
                ", enemyAmount=" + enemyAmount +
                ", waveNumber=" + waveNumber +
                '}';
    }
}
