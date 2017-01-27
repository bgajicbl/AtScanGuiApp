package at.mtel.denza.alfresco.json;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import at.mtel.denza.alfresco.util.DateUtil;

public class JsonDateDeserializer extends JsonDeserializer<Date> {
	
	  @Override
	    public Date deserialize(JsonParser jsonparser,
	            DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

	        String date = jsonparser.getText();
	        try {
	            return DateUtil.DATE_FORMAT.parse(date);
		}
		catch (ParseException e) {
	            throw new RuntimeException(e);
	        }

	    }
}
