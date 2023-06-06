
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage; 
import jade.lang.acl.MessageTemplate;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author amir
 */
public class SellerAgent extends Agent{
    private Product product;
    private double currentPrice=0;    
    private double temp;
    private int numberOfBids;
    private double highestBid; 
    private AID highestBidder;
    private int N;
    private ArrayList<AID> Buyers;
    @Override
    public void setup(){
        
       // Récupérez les arguments (produit, prix de départ) et initialisez les variables
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            product = (Product) args[0];
            System.out.println(""+product);
            N = (int) args[1];
            numberOfBids = 0;
            highestBid = currentPrice;
            highestBidder = null;
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.setContent(""+highestBid);
            for (int i=0;i<N;i++){
                msg.addReceiver(new AID("buyer"+i, AID.ISLOCALNAME)); 
            }
            send(msg);
            Buyers = new ArrayList<AID>();
            addBehaviour(new AuctionBehaviour());
        } else {
            System.out.println("Erreur: les arguments nécessaires n'ont pas été fournis.");
            doDelete();
        }
    }
    @Override
    public void takeDown(){
       System.out.println("i'm dead :"+getAID().getLocalName());
    }
    private class AuctionBehaviour extends CyclicBehaviour {
        public void action() {
            ACLMessage msg = receive();
            
            if (msg != null) { 
                numberOfBids++;
                temp = Double.parseDouble(msg.getContent());
                if(temp>0){
                    Buyers.add(msg.getSender());
                    if (highestBid<temp){
                        highestBid=temp;
                        highestBidder=msg.getSender();
                    }
                }
                if(numberOfBids==N){  
                    if(Buyers.size()>1){ 
                        ACLMessage msg_to_send = new ACLMessage(ACLMessage.CFP);
                        msg_to_send.setContent(""+highestBid);
                        N=Buyers.size();
                        for (AID i:Buyers){
                            msg_to_send.addReceiver(i); 
                        }
                        numberOfBids=0;
                        Buyers = new ArrayList<AID>();
                        send(msg_to_send);
                        }
                        else{
                            try {
                                sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SellerAgent.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (highestBid>=product.getReservePrice()){
                                System.out.println("The winner is "+highestBidder.getLocalName()+" with "+highestBid);
                            }else{
                                System.out.println("no winner ");
                            }

                        }
                }    
            } else { 
                block(); 
            }
        }
    }
    
}

