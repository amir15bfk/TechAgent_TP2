
import jade.core.Agent;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author amir
 */
public class Test extends Agent {
    @Override
    public void setup(){
        System.out.println("hi"+getAID().getLocalName());
    }
}
