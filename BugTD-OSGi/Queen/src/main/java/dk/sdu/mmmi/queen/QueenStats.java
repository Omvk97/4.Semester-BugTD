package dk.sdu.mmmi.queen;

public class QueenStats {
    public float x;
    public float y;
    public int life;
    public float damage;
    public float range;
    public float attackSpeed;

    @Override
    public String toString() {
        return "QueenStats{" +
                "x=" + x +
                ", y=" + y +
                ", life=" + life +
                ", damage=" + damage +
                ", range=" + range +
                ", attackSpeed=" + attackSpeed +
                '}';
    }
}