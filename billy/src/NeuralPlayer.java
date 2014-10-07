import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class NeuralPlayer extends Player {

	NeuralNet net;
	
	public NeuralPlayer(){
		this.name = "Neural Player";
		net = new NeuralNet();
	}
	
	public NeuralPlayer(String name){
		this.name = name;
		net = new NeuralNet();
	}
	
	public NeuralPlayer(String name, String fileName, boolean loadWeightsFromFile){
		this.name = name;
		if(loadWeightsFromFile){
			//initialize the neural net with them
			try{
				
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream reader = new ObjectInputStream(file);
			this.net = (NeuralNet) reader.readObject();
			}catch(IOException e){
				System.out.println("there was an error opening the file");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("there was an error reading the class");
				e.printStackTrace();
			}
		}
	}
	
	public void exportNetwork(String fileName){
		try{
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream writer = new ObjectOutputStream(fos);
			writer.writeObject(this.net);
		}catch(Exception e){
			System.out.println("There was an error writing the file.");
			e.printStackTrace();
		}

	}
	
	@Override
	public Move makeMove(Board board) {
		// TODO Auto-generated method stub
		return null;
	}
}
