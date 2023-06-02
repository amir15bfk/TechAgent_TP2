
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author amir
 */

public class BuyerAgent extends Agent{
    private double maxBid;
    private double current;
    private double mybid;
    @Override
    public void setup(){
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            maxBid = (double) args[0];
            addBehaviour(new BidBehaviour());
        } else {
            System.out.println("Erreur: les arguments nécessaires n'ont pas été fournis.");
            doDelete();
        }
            
    }
    @Override
    public void takeDown(){
       System.out.println("i'm dead :"+getAID().getLocalName());
    }
    private class BidBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                
                // Nouvelle offre reçue
                current =Double.parseDouble(msg.getContent());
                ACLMessage reply = msg.createReply();
                if (current<=maxBid){
                    mybid =(current+Math.random()*5);
                    if (mybid>maxBid)
                        mybid =maxBid;
                reply.setContent(""+mybid);

                
                System.out.println("i'm "+getAID().getLocalName()+" i give "+mybid);
                }
                else{
                    reply.setContent("-1.0");
                    System.out.println("i'm "+getAID().getLocalName()+" and  i'm out ");
                }
                send(reply);
                
                
            } else {
                block();
            }
        }
    }
}
