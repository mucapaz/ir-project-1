package extrator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import manager.Manager;

public class Extrator{

	private boolean useGeneric;
	private Manager manager;

	String site = "";
	String tittleTag = "";
	String priceTag = "";
	String attrsTag = "";
	String attrsElementTag = "";


	String sites[] = { "www.icarros.com.br", "www.mercadolivre.com.br",
			"www.olx.com.br/veiculos/carros", "www.jbsveiculos.com.br",
			"www.diariodepernambuco.vrum.com.br", "www.autoline.com.br",
			"www.socarrao.com.br", "www.sodresantoro.com.br",
			"bolsadeautomoveisrj.com.br", "estadodeminas.vrum.com.br" };

	/*
	public static void main(String[] args) {
		extrator e = new extrator(false);
		e.varreArquivos();
	}
	 */
	public Extrator(boolean useGeneric, Manager manager) {
		this.useGeneric = useGeneric; 
		this.manager = manager;



	}


	public void varreArquivos(Document document) {

		try {
			// pasta dos htmls
			File dir = new File("src/sites");
			File[] directoryListing = dir.listFiles();

			// varre todo arquivo html da pasta
			for (File input : directoryListing) {

				Scanner scanner = new Scanner(input);

				// varre o html identificando de que site se trata
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if (line.contains(sites[0])) {
						site = "iCarros";
						tittleTag = "h1.titulo-sm";
						priceTag = "h2.preco";
						attrsTag = "div.card-informacoes-basicas";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[1])) {
						site = "MercadoLivre";
						tittleTag = "h1[itemprop='name']";
						priceTag = "dd.placePrice";
						attrsTag = "ul.technical-details";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[2])) {
						site = "olx";
						tittleTag = "h1#ad_title";
						priceTag = "span.OLXad-price";
						attrsTag = "div.atributes";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[3])) {
						site = "jbsveiculos";
						tittleTag = "h1.car-details__title";
						priceTag = "h2.car-details__price";
						attrsTag = "ul.car-details__features";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[4])) {
						site = "diariodepernambucovrum";
						tittleTag = "h1.resultados-da-busca-descricao-dos-itens";
						priceTag = "li.item-valor";
						attrsTag = "ul.dados-item";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[5])) {
						site = "autoline";
						tittleTag = "div.rotate-banner-heding";
						priceTag = "div#divProposta header";
						attrsTag = "div.car-detail ul";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[6])) {
						site = "socarrao";
						tittleTag = "div.titulo-principal-detalhe";
						priceTag = "div.modal-lateral-box-preco";
						attrsTag = "div.aba-dados-veiculo-dados";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[7])) {
						site = "sodresantoro";
						tittleTag = "div.online_lance-tit-esq";
						priceTag = "span.valor";
						attrsTag = "ul.divisao";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[8])) {
						site = "bolsadeautomoveisrj";
						tittleTag = "h2.post-title";
						priceTag = "span.price";
						attrsTag = "div.dados_anuncio ul";
						attrsElementTag = "li";
						break;
					} else if (line.contains(sites[9])) {
						site = "estadodeminas";
						tittleTag = "h1.resultados-da-busca-descricao-dos-itens";
						priceTag = "li.item-valor";
						attrsTag = "ul.dados-item";
						attrsElementTag = "li";
						break;
					}
				}
				scanner.close();
//
//				// inicia o jsoup
//				Document documento = Jsoup.parse(input, "UTF-8",
//						"http://example.com/");
				
				if(useGeneric) extractFromGenericDocument(document);
				else extractFromDocument(document);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void extractFromGenericDocument(Document doc){
		System.out.println("\n===== " + site);

		boolean foundTittle = false;
		Elements tittleElements = doc.body().getElementsByTag("h1");
		for (Element e : tittleElements) {
			if (verificaTitulo(e.text())) {
				System.out.println(e.text());
				foundTittle = true;
				break;
			}
		}
		if (!foundTittle) {
			tittleElements = doc.body().getElementsByTag("h2");
			for (Element e : tittleElements) {
				if (verificaTitulo(e.text())) {
					System.out.println(e.text());
					break;
				}
			}
		}

		Elements priceElements = doc.body().getElementsContainingText("R$");
		for (Element e : priceElements) {
			if (e.className().contains("valor")
					|| e.className().contains("preco")
					|| e.className().contains("price")) {
				System.out.println(e.text());
				break;
			}
		}

		Elements listElements = doc.body().select("ul");
		for (Element e : listElements) {
			if (e.text().contains("Ano") || e.text().contains("ANO")
					|| e.text().contains("KM")) {
				System.out.println(e.text());
				break;
			}

		}	
		
	}
	
	public void extractFromDocument(Document doc) {
		String title, price;
		ArrayList<String> ar = new ArrayList<String>();
		
		try {
			title = doc.select(tittleTag).first().text();		
			price = doc.select(priceTag).first().text();
			
			Element conteudo = doc.select(attrsTag).first();
			Elements attr = conteudo.getElementsByTag(attrsElementTag);
			for (Element e : attr) {
				ar.add(e.text());
			}
			
			System.out.println("\n\n=== " + site);
			System.out.println(title);
			System.out.println(price);
			
			for(String str : ar){
				System.out.println(str);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	private boolean verificaTitulo(String tit) {
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
