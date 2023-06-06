import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;


/**
 *
 * @author amir
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JavaApplication1 a = new JavaApplication1();
        a.test(100);
    }
    public void test(int N){
        Runtime r= Runtime.instance();
        Properties pr = new ExtendedProperties();
        pr.setProperty("gui", "true");
        Profile p = new ProfileImpl(pr);
        ContainerController mainc = r.createMainContainer(p);
        Profile p2 = new ProfileImpl();
        ContainerController cc = r.createAgentContainer(p2);
        Profile p3 = new ProfileImpl();
        ContainerController bc = r.createAgentContainer(p3);

        try{
            AgentController []agents = new AgentController[N];
            for (int i=0;i<N;i++){
            agents[i] = bc.createNewAgent("buyer"+i, "BuyerAgent", new Object []{Math.random()*50});
            agents[i].start();}
            AgentController seller = cc.createNewAgent("seller", "SellerAgent", new Object[]{new Product("ball",90.0),N});
            seller.start();
            
        }catch (Exception  e){
            e.printStackTrace();
        }
    }
    
}

