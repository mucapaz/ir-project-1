package classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URLEncoder;

import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SparseInstance;

public class ClassifierWrapper{

	private Classifier classifier;
	private Instances instances;
	private String[] attributes;

	public ClassifierWrapper(Classifier classifier, Instances instances, String[] attributes){
		this.classifier = classifier;
		this.instances = instances;
		this.attributes = attributes;
	}

	/* Metodo retorna se uma pagina pertence `a classe positiva
	 */
	public boolean classify(String page) throws Exception{
		boolean relevant = false;
		double[] values = getValues(page);
		SparseInstance instanceWeka = new SparseInstance(1, values);
		instanceWeka.setDataset(instances);
		double classificationResult = classifier.classifyInstance(instanceWeka);
		if (classificationResult == 0) {
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

	private double[] getValues(String pagina) {
		double[] values;
		//Implementar a extracao da pagina dos termos usados como features pelo classificador e criar um vetor de double com a frequencia desses termos na pagina

		return values;
	}	

	public static void main(String[] args){
		//local do modelo de classificacao criado
		String localModelo = args[0];
		//features do classificador
		String[] attributes = args[1].split(" ");
		InputStream is = new FileInputStream(localModelo);
		ObjectInputStream objectInputStream = new ObjectInputStream(is);
		Classifier classifier = (Classifier) objectInputStream.readObject();
		weka.core.FastVector vectorAtt = new weka.core.FastVector();
		for (int i = 0; i < attributes.length; i++) {
			vectorAtt.addElement(new weka.core.Attribute(attributes[i]));
		}
		String[] classValues = config.getParam("CLASS_VALUES", " ");
		weka.core.FastVector classAtt = new weka.core.FastVector();
		for (int i = 0; i < classValues.length; i++) {
			classAtt.addElement(classValues[i]);
		}
		vectorAtt.addElement(new weka.core.Attribute("class", classAtt));
		Instances insts = new Instances("classification", vectorAtt, 1);
		insts.setClassIndex(attributes.length);
		Classifier classificador = new Classifier(classifier, insts, attributes);
	}

}