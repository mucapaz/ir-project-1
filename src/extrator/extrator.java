import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class extrator {
	public static void main(String[] args) {

		String sites[] = { "www.icarros.com.br", "www.mercadolivre.com.br",
				"www.olx.com.br/veiculos/carros", "www.jbsveiculos.com.br",
				"www.diariodepernambuco.vrum.com.br", "www.autoline.com.br",
				"www.socarrao.com.br", "www.sodresantoro.com.br",
				"bolsadeautomoveisrj.com.br", "estadodeminas.vrum.com.br" };

		String site = "";
		String tagTitulo = "";
		String tagPreco = "";
		String tagAttrs = "";
		String tagAttrsElemento = "";

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
						tagTitulo = "h1.titulo-sm";
						tagPreco = "h2.preco";
						tagAttrs = "div.card-informacoes-basicas";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[1])) {
						site = "MercadoLivre";
						tagTitulo = "h1[itemprop='name']";
						tagPreco = "dd.placePrice";
						tagAttrs = "ul.technical-details";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[2])) {
						site = "olx";
						tagTitulo = "h1#ad_title";
						tagPreco = "span.OLXad-price";
						tagAttrs = "div.atributes";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[3])) {
						site = "jbsveiculos";
						tagTitulo = "h1.car-details__title";
						tagPreco = "h2.car-details__price";
						tagAttrs = "ul.car-details__features";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[4])) {
						site = "diariodepernambucovrum";
						tagTitulo = "h1.resultados-da-busca-descricao-dos-itens";
						tagPreco = "li.item-valor";
						tagAttrs = "ul.dados-item";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[5])) {
						site = "autoline";
						tagTitulo = "div.rotate-banner-heding";
						tagPreco = "div#divProposta header";
						tagAttrs = "div.car-detail ul";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[6])) {
						site = "socarrao";
						tagTitulo = "div.titulo-principal-detalhe";
						tagPreco = "div.modal-lateral-box-preco";
						tagAttrs = "div.aba-dados-veiculo-dados";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[7])) {
						site = "sodresantoro";
						tagTitulo = "div.online_lance-tit-esq";
						tagPreco = "span.valor";
						tagAttrs = "ul.divisao";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[8])) {
						site = "bolsadeautomoveisrj";
						tagTitulo = "h2.post-title";
						tagPreco = "span.price";
						tagAttrs = "div.dados_anuncio ul";
						tagAttrsElemento = "li";
						break;
					} else if (line.contains(sites[9])) {
						site = "estadodeminas";
						tagTitulo = "h1.resultados-da-busca-descricao-dos-itens";
						tagPreco = "li.item-valor";
						tagAttrs = "ul.dados-item";
						tagAttrsElemento = "li";
						break;
					}
				}
				scanner.close();

				// inicia o jsoup
				Document doc = Jsoup.parse(input, "UTF-8",
						"http://example.com/");

				// print os resultados
				System.out.println("\n----\n" + site);
				// descricao
				Element titulo = doc.select(tagTitulo).first();
				System.out.println(titulo.text());

				// preco
				Element preco = doc.select(tagPreco).first();
				System.out.println(preco.text());

				Element conteudo = doc.select(tagAttrs).first();
				Elements attr = conteudo.getElementsByTag(tagAttrsElemento);

				for (Element e : attr) {
					System.out.println(e.text());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
