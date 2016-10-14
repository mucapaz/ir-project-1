package extractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import manager.Manager;

public class Extractor implements Runnable{

	private boolean useGeneric;
	private Manager manager;

	String sites[] = { "www.icarros.com.br", "www.mercadolivre.com.br",
			"www.olx.com.br/veiculos/carros", "www.jbsveiculos.com.br",
			"www.diariodepernambuco.vrum.com.br", "www.autoline.com.br",
			"www.socarrao.com.br", "www.sodresantoro.com.br",
			"bolsadeautomoveisrj.com.br", "estadodeminas.vrum.com.br" };

	public Extractor(boolean useGeneric, Manager manager) {
		this.useGeneric = useGeneric; 
		this.manager = manager;

	}

	@Override
	public void run() {
		while(true){
			try{
				Document document = manager.removeExtractorDocument();
				processDocument(document);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void processDocument(Document document) {

		String tittleTag = "";
		String priceTag = "";
		String attrsTag = "";
		String attrsElementTag = "";


		String line = document.html();
		if (line.contains(sites[0])) {
			tittleTag = "h1.titulo-sm";
			priceTag = "h2.preco";
			attrsTag = "div.card-informacoes-basicas";
			attrsElementTag = "li";
		} else if (line.contains(sites[1])) {
			tittleTag = "h1[itemprop='name']";
			priceTag = "dd.placePrice";
			attrsTag = "ul.technical-details";
			attrsElementTag = "li";
		} else if (line.contains(sites[2])) {
			tittleTag = "h1#ad_title";
			priceTag = "span.OLXad-price";
			attrsTag = "div.atributes";
			attrsElementTag = "li";
		} else if (line.contains(sites[3])) {
			tittleTag = "h1.car-details__title";
			priceTag = "h2.car-details__price";
			attrsTag = "ul.car-details__features";
			attrsElementTag = "li";
		} else if (line.contains(sites[4])) {
			tittleTag = "h1.resultados-da-busca-descricao-dos-itens";
			priceTag = "li.item-valor";
			attrsTag = "ul.dados-item";
			attrsElementTag = "li";
		} else if (line.contains(sites[5])) {
			tittleTag = "div.rotate-banner-heding";
			priceTag = "div#divProposta header";
			attrsTag = "div.car-detail ul";
			attrsElementTag = "li";
		} else if (line.contains(sites[6])) {
			tittleTag = "div.titulo-principal-detalhe";
			priceTag = "div.modal-lateral-box-preco";
			attrsTag = "div.aba-dados-veiculo-dados";
			attrsElementTag = "li";
		} else if (line.contains(sites[7])) {
			tittleTag = "div.online_lance-tit-esq";
			priceTag = "span.valor";
			attrsTag = "ul.divisao";
			attrsElementTag = "li";
		} else if (line.contains(sites[8])) {
			tittleTag = "h2.post-title";
			priceTag = "span.price";
			attrsTag = "div.dados_anuncio ul";
			attrsElementTag = "li";
		} else if (line.contains(sites[9])) {
			tittleTag = "h1.resultados-da-busca-descricao-dos-itens";
			priceTag = "li.item-valor";
			attrsTag = "ul.dados-item";
			attrsElementTag = "li";
		}


		if(useGeneric) extractFromGenericDocument(document);
		else extractFromDocument(document,tittleTag,
				priceTag, attrsTag, attrsElementTag);

	}

	public void extractFromGenericDocument(Document doc){
		
		String tittle = "", price = "";
		ArrayList<String> ar = new ArrayList<String>();
		
		boolean foundTittle = false;
		Elements tittleElements = doc.body().getElementsByTag("h1");
		for (Element e : tittleElements) {
			if (checkTittle(e.text())) {
				tittle = e.text();
				foundTittle = true;
				break;
			}
		}
		if (!foundTittle) {
			tittleElements = doc.body().getElementsByTag("h2");
			for (Element e : tittleElements) {
				if (checkTittle(e.text())) {
					tittle = e.text();
					break;
				}
			}
		}

		Elements priceElements = doc.body().getElementsContainingText("R$");
		for (Element e : priceElements) {
			if (e.className().contains("valor")
					|| e.className().contains("preco")
					|| e.className().contains("price")) {
				price = e.text();
				break;
			}
		}

		Elements listElements = doc.body().select("ul");
		for (Element e : listElements) {
			if (e.text().contains("Ano") || e.text().contains("ANO")
					|| e.text().contains("KM")) {
				ar.add(e.text());
				break;
			}

		}	
		
		if(tittle.length() > 0  && price.length() > 0){
			printResult(doc, tittle, price, ar);
		}
		
	}

	public void extractFromDocument(Document doc, String tittleTag,
			String priceTag, String attrsTag, String attrsElementTag) {

		String tittle, price;
		ArrayList<String> ar = new ArrayList<String>();

		try {
			tittle = doc.select(tittleTag).first().text();		
			price = doc.select(priceTag).first().text();

			Element conteudo = doc.select(attrsTag).first();
			Elements attr = conteudo.getElementsByTag(attrsElementTag);
			for (Element e : attr) {
				ar.add(e.text());
			}
			
			if(tittle.length() > 0  && price.length() > 0){
				printResult(doc, tittle, price, ar);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void printResult(Document doc,String tittle, String price, ArrayList<String> ar){
		System.out.println("\n\n Site: " + doc.baseUri());
		System.out.println(tittle);
		System.out.println(price);

		for(String str : ar){
			System.out.println(str);
		}
	}
	
	private boolean checkTittle(String tit) {
		String marcasCarro[] = { "Fiat", "Jeep", "Suzuki", "Volkswagen",
				"Nissan", "Ford", "Citroën", "Honda", "Bmw", "Audi", "Citroen",
				"Chevrolet", "Hyundai", "Peugeot", "Renault", "Kia", "Jac" };

		for (String s : marcasCarro) {
			if (tit.contains(s) || tit.contains(s.toUpperCase()))
				return true;
		}

		return false;
	}


}
