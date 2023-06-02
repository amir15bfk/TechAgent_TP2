
import java.io.Serializable;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author amir
 */
public class Product implements Serializable {
    private String name;
    private double reservePrice;

    public Product(String name, double reservePrice) {
        this.name = name;
        this.reservePrice = reservePrice;
    }

    public String getName() {
        return name;
    }

    public double getReservePrice() {
        return reservePrice;
    }
}