package classifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import manager.Manager;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffLoader.ArffReader;

public class ClassifierWrapper implements Runnable{

	private Manager manager;

	private Classifier classifier;
	private Instances instances;
	private ArrayList<String> attributes;

	public static void main(String[] args) throws Exception{
		ClassifierWrapper cw = new ClassifierWrapper("classifier.model", 
				"attributes", null);
		
		Document doc1 = Jsoup.connect("http://carro.mercadolivre.com.br/MLB-802823205-ds" +
				"5-16-16v-turbo-2014-prata-top-de-linha-unico-dono-_JM").get();
		System.out.println(cw.classify(doc1.text()));
		
		Document doc2 = Jsoup.connect("http://carros.mercadolivre.com.br/" +
				"carros-e-caminhonetes/carros#D[RC:MLB1744,P:2,Q:5]").get();
		System.out.println(cw.classify(doc2.text()));
		
		Document doc3 = Jsoup.connect("https://www.google.com.br/?gfe_rd=cr&ei=oKL-V_3TEpKF8QfInKxI&gws_rd=ssl").get();
		System.out.println(cw.classify(doc3.text()));
		
		Document doc4 = Jsoup.connect("http://www.olx.com.br/veiculos/carros").get();
		System.out.println(cw.classify(doc4.text()));
		
		System.out.println(" ---------------------- ");
		
		Document doc5 = Jsoup.connect("http://diariodepernambuco.vrum.com.br/seguros/").get();
		System.out.println(cw.classify(doc5.text()));
		
		
		Document doc6 = Jsoup.connect("http://www.bolsadeautomoveisrj.com.br/Veiculo/captiva-2.4-sfi-ecotec-fwd-16v-gasolina-4p-automatico-gasolina-2011/62328/detalhes").get();
		System.out.println(cw.classify(doc6.text()));
//		
//		Document doc7 = Jsoup.connect("https://www.google.com.br/?gfe_rd=cr&ei=oKL-V_3TEpKF8QfInKxI&gws_rd=ssl").get();
//		System.out.println(cw.classify(doc3.text()));
//		
//		Document doc8 = Jsoup.connect("http://www.olx.com.br/veiculos/carros").get();
//		System.out.println(cw.classify(doc3.text()));
		
	}
	
	public ClassifierWrapper(String classifierModelLocation, String attributesLocation, Manager manager)
			throws FileNotFoundException, IOException, ClassNotFoundException{

		this.manager = manager;

		ObjectInputStream obs = new ObjectInputStream(new FileInputStream(classifierModelLocation));
		this.classifier = (Classifier) obs.readObject();
		obs.close();

//		FastVector classVec = new FastVector<>();
//		classVec.addElement("pos");
//		classVec.addElement("neg");
//
//		FastVector attVec = new FastVector();
//		attVec.add(classVec);

		attributes = new ArrayList<String>();

		String line;	
		BufferedReader br = new BufferedReader(new FileReader(attributesLocation));
		while((line = br.readLine()) != null){
//			attVec.addElement(new Attribute(line));
			attributes.add(line);
		}

		String lol = "final.arff";
		BufferedReader reader =
				new BufferedReader(new FileReader(lol));
		ArffReader arff = new ArffReader(reader);
		Instances data = arff.getData();
		data.setClassIndex(data.numAttributes() - 1);
		this.instances = data;
		//this.instances = new Instances("classification", attVec, 1);
		
		
		instances.setClassIndex(attributes.size());

	}

	/* Metodo retorna se uma pagina pertence `a classe positiva
	 */
	public boolean classify(String page) throws Exception{
		boolean relevant = false;
		double[] values = getValues(page);
//		for(int x=0;x<values.length;x++){
//			System.out.print(values[x] + " :");
//		}
//		System.out.println();
		SparseInstance instanceWeka = new SparseInstance(1, values);
		instanceWeka.setDataset(instances);
		double classificationResult = classifier.classifyInstance(instanceWeka);
//		System.out.println(classificationResult);
		if (classificationResult != 0) {
			relevant = true;
		}
		else {
			relevant = false;
		}
		return relevant;
	}

	/* Metodo retorna as probabilidades da pagina pertencer `as classes
       positiva e negativa
	 */
	public double[] distributionForInstance(String page) throws Exception{
		double[] result = null;
		double[] values = getValues(page);
		SparseInstance instanceWeka = new SparseInstance(1, values);
		instanceWeka.setDataset(instances);
		result = classifier.distributionForInstance(instanceWeka);
		return result;
	}

	private double[] getValues(String page) {


		Map<String, Integer> hash = new HashMap<String, Integer>();

		page = page.replaceAll("[^a-zA-Z]", " ");
		String[] pageSplit = page.split(" ");

		for(int x=0;x<pageSplit.length;x++){
			if(hash.containsKey(pageSplit[x])){
				hash.put(pageSplit[x], hash.get(pageSplit[x]) +1 );
			}else{
				hash.put(pageSplit[x], 1 );
			}
		}

		double[] values = new double[attributes.size()];

		for(int x=0;x<attributes.size();x++){
			if(hash.containsKey(attributes.get(x)))
				values[x] = hash.get(attributes.get(x));
		}

		return values;
	}	

	@Override
	public void run() {

		while(true){

			Document document = manager.removeClassifierDocument();
			String page = document.text().replaceAll("[^a-zA-Z]", " ");

			try {
				boolean include = classify(page);
				if(include){
					manager.addExtractorElement(document);
					
				}
				
				System.out.println(include + "  " + document.baseUri());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}