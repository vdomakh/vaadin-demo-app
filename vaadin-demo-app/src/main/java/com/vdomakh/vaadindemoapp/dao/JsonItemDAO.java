package com.vdomakh.vaadindemoapp.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vdomakh.vaadindemoapp.domain.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonItemDAO implements ItemDAO {

    @Value("${storageFile.path}")
    private String storage;
    

    @Override
    public List<Item> getAllItems() {
    	  List<Item> result = getListFromFile(storage);
          
          return result;
    }
    
    @Override
    public List<Item> getItemsByFilter(String filter) {
    	List<Item> items = getListFromFile(storage);
    	List<Item> result = new ArrayList<>();
      	for (Item i : items) {
    		String nameDescriptionConcat = i.getName() + " " + i.getDescription();
    		if (nameDescriptionConcat.contains(filter)) {
    			result.add(i);
    		}
      	}  
            return result;
    }

   

    @Override
    public void updateItems(List<Item> newList) {
    	ObjectMapper mapper = new ObjectMapper();
    	JsonNode newJsonArray = mapper.valueToTree(newList);
    	ObjectNode parentNode = (ObjectNode)readFileIntoJsonNode(storage);
    	parentNode.set("data", newJsonArray);
    	try 
    		(BufferedWriter bw = new BufferedWriter(new FileWriter(storage)))
    	{
    		bw.write(parentNode.toString());
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
		

    }
   

    private boolean fileIsJson(String fileName) {
        boolean result = false;
        if (fileName.contains("json")) {
            result = true;
        }
        return result;
    }
    

    private List<Item> getListFromFile(String fileName) {
    	List<Item> resultList = new ArrayList<>();	
    	
    	if (!fileIsJson(fileName)) {
            return resultList;
        }
    	
    	JsonNode jsonNode = readFileIntoJsonNode(fileName);
    	if (jsonNode==null) {
            return resultList;
        }
    ObjectMapper mapper =  new ObjectMapper();
	 mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);	
     JsonNode arrayNode = jsonNode.get("data");
     try {
     resultList = mapper.readerFor(new TypeReference<List<Item>>(){}).readValue(arrayNode);
     }
     catch (IOException ioe) {
    	 ioe.printStackTrace();
     }
   
  return resultList;
}
    
    private JsonNode readFileIntoJsonNode(String fileName) {
    	JsonNode jsonNode = null;
    	   try {
    		   File initialFile = new File(fileName);
    		       InputStream inputStream= new FileInputStream(initialFile);
    		       StringBuilder resultStringBuilder = new StringBuilder();
    		        try (
    		    BufferedReader br
    		                  = new BufferedReader(new InputStreamReader(inputStream))) {
    		        String line;
    		        while ((line = br.readLine()) != null) {
    		            resultStringBuilder.append(line).append("\n");
    		        }
    		    }
    			
    			 ObjectMapper mapper =  new ObjectMapper();
    		     mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    		     jsonNode = mapper.readTree(resultStringBuilder.toString());
    		    }
    		    catch (Exception e) {
    		    	e.printStackTrace();
    		    }
    	   return jsonNode;
    }

}