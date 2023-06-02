
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
                                new Winner(highestBidder.getLocalName(),product.getName(),highestBid);
                            }else{
                                new Winner();
                            }

                        }
                }    
            } else { 
                block(); 
            }
        }
    }
    public class Winner extends JFrame implements MouseListener {
    private JLabel label;
    private JLabel label2;
    private String name;    
    private String product;
    private boolean revealed;
    
    public Winner(String name,String product,double value) {
        super("The Winner Is...!");
        this.name = name;
        this.product = product;
        
        label = new JLabel("the "+product+" is sold to ...");
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.YELLOW);
        label.addMouseListener(this);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.darkGray);
        panel.add(label);

        add(panel, BorderLayout.CENTER);
        label2 = new JLabel("with "+String.format("%.02f", value)+" $");
        label2.setFont(new Font("Arial", Font.BOLD, 20));
        label2.setForeground(Color.YELLOW);
        label2.setVisible(false);
        panel = new JPanel();
        panel.setBackground(Color.darkGray);
        panel.add(label2);

        add(panel, BorderLayout.SOUTH);
        pack();
        
        setSize(700, 200);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);     
    }
    public Winner() {
        super("no one won");
        
        label = new JLabel("the sell is cancel");
        label.setFont(new Font("Arial", Font.BOLD, 50));
        label.setForeground(Color.RED);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.darkGray);
        panel.add(label);

        add(panel, BorderLayout.CENTER);

        pack();
        
        setSize(700, 200);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);     
    }
	
    public void mouseClicked(MouseEvent e) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) {	
        if (!revealed) {
        	reveal();
        }
    }
	
    private void reveal() {
        revealed = true;
        
        
        label.setText("the "+product+" \nis sold to "+name);
        label2.setVisible(true);
        
    }
	
    public void mouseEntered(MouseEvent e) {
        if (!revealed) {
            label.setText("the "+product+" \nis sold to "+name);
        }
    }

    public void mouseExited(MouseEvent e) {
        if (!revealed) {
            label.setText("the "+product+" \nis sold to ...");
        }
    }
}
}



