package dk.sdu.mmmi.commontower;

import dk.sdu.mmmi.commonweapon.WeaponPart.Color;

public class QueenStats {
    public float x;
    public float y;
    public float life;
    public float damage;
    public float range;
    public float attackSpeed;
    public Color weaponColor;

    @Override
    public String toString() {
        return "QueenStats{" +
                "x=" + x +
                ", y=" + y +
                ", life=" + life +
                ", damage=" + damage +
                ", range=" + range +
                ", attackSpeed=" + attackSpeed +
                ", weaponColor=" + weaponColor +
                '}';
    }
}