package engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Parser {

     private TreeMap<Long, List<Revision>> parser;

     public Parser(){
          this.parser = new TreeMap<>();
     }

     public static long countWords(String text) {
          if (text == null || text.isEmpty()) {
               return 0;
         }

         StringTokenizer token = new StringTokenizer(text);
         return token.countTokens();
     }

     public static int testRev(Revision rev, List<Revision> list){
     	 int i = 0;
     	 for(Revision r: list){
     	 	if(rev.equals(r)){
     	 		return i;
     	 	}
     	 	i++;
     	 }

     	 return -1;
     }

     public TreeMap<Long, List<Revision>> parsing (String[] args, int nsnaps) throws FileNotFoundException, XMLStreamException {

          if (args.length < 1)
               throw new RuntimeException("The name of the XML file is required!");

          List<Revision> articles = null;
          Revision rev = null;
          String text = null;
          int i = 0, teste;

          XMLInputFactory factory = XMLInputFactory.newInstance();
          factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

          for(int j=0; j<nsnaps; j++){
	          XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(
	                                                  new File(args[j])));

	          while (reader.hasNext()) {
	               int Event = reader.next();

	               switch (Event) {
	                    case XMLStreamConstants.START_ELEMENT: {
	                         if ("page".equals(reader.getLocalName())) {
	                              rev = new Revision();
	                         }

	                         break;
	                    }
	                    case XMLStreamConstants.END_DOCUMENT: {
	                    	reader.close();
	                    	break;
	                    }
	                    case XMLStreamConstants.CHARACTERS: {
	                         text = reader.getText();
	                         break;
	                    }
	                    case XMLStreamConstants.END_ELEMENT: {
	                         switch (reader.getLocalName()) {
	                              case "page": {
	                              		i=0;
	                                   if(parser.size() == 0 || !parser.containsKey(rev.getArtId())) {
	                                        List<Revision> list = new ArrayList<>();
	                                        list.add(rev);
	                                        parser.put(rev.getArtId(), list);
	                                   }
	                                   else{
	                                   	    teste = testRev(rev, parser.get(rev.getArtId()));
	                                   		if(teste == -1)
	                                        	parser.get(rev.getArtId()).add(rev);
	                                        else{
	                                        	parser.get(rev.getArtId()).get(teste).addNumber();
	                                        }
	                                   }                                  
	                                   break;
	                              }
	                              case "title": {
	                                   rev.setTitle(text);
	                                   break;
	                              }
	                              case "ip": {
	                              	   rev.setContId((long) -1);
	                              	   rev.setUsername(text);
	                              	   break;
	                              }
	                              case "id": {
	                                   if(i==0){
	                                   rev.setArtId(Long.parseLong(text));
	                                   i++;
	                                           }
	                                   
	                                   else if(i==1){
	                                   rev.setRevId(Long.parseLong(text));
	                                   i++;
	                                           }
	                                   
	                                   else if(i==2){
	                                   rev.setContId(Long.parseLong(text));
	                                           }
	                                   break;
	                                        }
	                              case "timestamp": {
	                                   rev.setTime(text);
	                                   break;
	                              }
	                              case "username" : {
	                                   rev.setUsername(text);
	                                   break;
	                              }
	                              case "text" : {

	                                   rev.setWords(countWords(text.trim()));
	                                   rev.setBytes((long)text.getBytes().length); 
	                                   /*try{rev.setBytes((long)text.getBytes("UTF-8").length);}
	                                   catch(UnsupportedEncodingException e){
  										e.printStackTrace();}*/
	                                   break;
	                              }
	                         }
	                         break;
	                    }
	               }
	            }

	        }
          return parser;
     }
}