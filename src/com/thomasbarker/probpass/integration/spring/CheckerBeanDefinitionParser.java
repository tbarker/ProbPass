package com.thomasbarker.probpass.integration.spring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.parsing.Location;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.thomasbarker.probpass.algo.Population;
import com.thomasbarker.probpass.io.LoadPopulation;
import com.thomasbarker.probpass.reference.Populations;

public class CheckerBeanDefinitionParser
	extends AbstractSingleBeanDefinitionParser
{

	protected Class<PasswordCheckerSpring> getBeanClass( Element element ) {
		return PasswordCheckerSpring.class;
	}

	protected void doParse( Element element, ParserContext parserContext, BeanDefinitionBuilder bean ) {

		// Is 133tification protection on?
		String l33t = element.getAttribute( "l33t" );
		bean.addPropertyValue(
			"l33t",
			null == l33t || l33t.equals( "false" ) ? false : true
		);

		// Get strictness
		int strictness = Integer.parseInt( element.getAttribute( "strictness" ) );
		bean.addPropertyValue( "strictness", strictness );

		// Read all the populations
		Map<Population, Double> weightedBasePopulations = new HashMap<Population, Double>();
		NodeList populationElements = element.getChildNodes();
		for ( int populationIndex = 0; populationIndex != populationElements.getLength(); populationIndex++ ) {
			Node populationElement = populationElements.item( populationIndex );
			if ( Element.TEXT_NODE == populationElement.getNodeType() ) { continue; }

			// Weight
			double weight = Double.parseDouble( populationElement.getAttributes().getNamedItem( "weight" ).getNodeValue() );

			List<String> samples = new ArrayList<String>();
			// Read list
			NodeList samplesNodes = populationElement.getChildNodes();
			for ( int sampleIndex = 0; sampleIndex != samplesNodes.getLength(); sampleIndex++ ) {
				Node samplesNode = samplesNodes.item( sampleIndex );
				if ( Element.TEXT_NODE == samplesNode.getNodeType() ) { continue; }
				samples.add( samplesNode.getTextContent() );
			}
			Population population = new Population( samples );
			// Load file ..
			Node fileNode = populationElement.getAttributes().getNamedItem( "file" );
			if ( null != fileNode ) {
				File file = new File( fileNode.getTextContent() );
				try {
					population = LoadPopulation.fromFile( file );
				} catch ( IOException ioe ) {
					throw new BeanDefinitionParsingException(
						new Problem(
							"Cannot read password population file: " + file.getAbsolutePath(),
							new Location( parserContext.getReaderContext().getResource() )
						)
					);
				}
			}
			// Handle default
			if ( "defaultPopulation".equals( populationElement.getLocalName() ) ) {
				population = Populations.getDefault();
			}

			weightedBasePopulations.put( population, weight );
		}

		// If nothing there, fill 95% with default
		if ( weightedBasePopulations.isEmpty() ) {
			weightedBasePopulations.put( Populations.getDefault(), 0.95 );
		}

		bean.addPropertyValue( "basePopulations", weightedBasePopulations );
	}
	
}
