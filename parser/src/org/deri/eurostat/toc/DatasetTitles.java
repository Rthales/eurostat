package org.deri.eurostat.toc;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.deri.eurostat.dsdparser.ParserUtil;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * @author Aftab Iqbal
 *
 */

public class DatasetTitles {

	private static String serialization = "TURTLE";
	private static String fileExt = ".ttl";
	private static String inputFilePath = "";
	private static String outputFilePath = "";
	ParseToC obj;
	
	public void getDS_Titles()
	{
		obj = new ParseToC();
		if(inputFilePath.equals("")) {
		obj.getDatasetTitles();
		}
		else {
			obj.getDatasetTitles(inputFilePath);
		}
		
		buildRDF();
	}
	
	public void buildRDF()
	{
		Model model = ParserUtil.getModelProperties();
		HashMap<String, String> hsh = new HashMap<String, String>();
		Iterator it = obj.toc.entrySet().iterator();
		String lang = "";
		String title = "";
	    while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Resource dsd = model.createResource(ParserUtil.titleURI + pairs.getKey());
	        hsh = (HashMap<String,String>)pairs.getValue();
	        Iterator iter = hsh.entrySet().iterator();
	        while(iter.hasNext())
	        {
	        	Map.Entry titles = (Map.Entry)iter.next();
	        	lang = (String) titles.getKey();
	        	title = hsh.get(lang);
	        	model.add(dsd,ParserUtil.dcTitle,model.createLiteral(title,lang));
	        }
	    }

		writeRDFToFile("title", model);
	}
	
	public void writeRDFToFile(String fileName, Model model)
	{
		if(serialization.equalsIgnoreCase("RDF/XML"))
			fileExt = ".rdf";
		else if(serialization.equalsIgnoreCase("TURTLE"))
			fileExt = ".ttl";
		else if(serialization.equalsIgnoreCase("N-TRIPLES"))
			fileExt = ".nt";
		try
	   	{
			OutputStream output = new FileOutputStream(outputFilePath + fileName + fileExt,false);
			model.write(output,serialization.toUpperCase());
			
	   	}catch(Exception e)
	   	{
	   		System.out.println("Error while creating file ..." + e.getMessage());
	   	}
	}
	
	private static void usage()
	{
		System.out.println("usage: DatasetTitles [parameters]");
		System.out.println();
		System.out.println("	-o outputFilePath	Output directory path to generate the file.");
		System.out.println("	(optional)-f 	format	RDF format for serialization (RDF/XML, TURTLE, N-TRIPLES).");
	}
	
	public static void main(String[] args) throws Exception
	{
		DatasetTitles obj = new DatasetTitles();
		
		CommandLineParser parser = new BasicParser( );
		Options options = new Options( );
		options.addOption("h", "help", false, "Print this usage information.");
		options.addOption("i", "inputFilepath", true, "Local ToC file.");
		options.addOption("o", "outputFilePath", true, "Output directory path to generate the file.");
		options.addOption("f", "format", true, "RDF format for serialization (RDF/XML, TURTLE, N-TRIPLES).");

		CommandLine commandLine = parser.parse( options, args );
		
		if( commandLine.hasOption('h') ) {
		    usage();
		    return;
		 }
		
		if(commandLine.hasOption('i'))
			inputFilePath = commandLine.getOptionValue('i');
		if(commandLine.hasOption('o'))
			outputFilePath = commandLine.getOptionValue('o');
		
		if(commandLine.hasOption('f'))
			serialization = commandLine.getOptionValue('f');
		
		if(serialization.equals("") || outputFilePath.equals(""))
		{
			usage();
			return;
		}
		else
		{
			obj.getDS_Titles();
		}
	}
	
}
