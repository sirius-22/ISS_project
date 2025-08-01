package main.java.domain;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unibo.basicomm23.utils.CommUtils;

public class Product{
	
	  private static final Logger logger = LoggerFactory.getLogger(Product.class);
	  private  int productId;
	  private  String name;
	  private  int weight;
	  
	  /*
	   * RQUIRED BY Spring !!!
	   */
	  private Product() {}



	private static int getJsonInt(String jsonStr, String key)  {
		try {
			JSONObject j = (JSONObject) new JSONParser().parse(jsonStr);
			long jpd = (long) j.get(key);
			return Long.valueOf(jpd).intValue();
		}catch(ParseException e) {
			return 0;
		}
	}
 	private static String getJsonString(String jsonStr, String key)   {
		try {
			JSONObject j = (JSONObject) new JSONParser().parse(jsonStr);
			return (String) j.get(key);
		}catch(ParseException e) {
			return "error";
		}
	}

 	private void setupErrorroduct( String cause) {
		productId = 0;
		weight    = 0;
		name      = "error-"+cause;
		//logger.info( "Product | createErrorroduct:" + productId   ); 		
	
 	}
 	
	/*
	 * Costructor based on a Json String
    */
	public Product(String jsonStr)   {
		this( getJsonInt(jsonStr, "productId"), getJsonString(jsonStr,"name"), getJsonInt(jsonStr,"weight") );
		//CommUtils.outmagenta("Product | creation josn : " + jsonStr);
		logger.info( "Product | created json:"+ this.toString() + " in tread:" + Thread.currentThread().getId());
	}
 	
	/*
	 * Constructor based on an id (to be removed ...
	 */	
	public Product( int id ) {
		this( ""+id,"unknown","unknown");
	}

	/*
	 * Constructor based on String values
	 */	
	public Product(String pId, String pname, String pweight) {		
		try {
			productId = Integer.parseInt(pId);
			name      = pname;
			weight    = Integer.parseInt(pweight);
		}catch(Exception e) {
			//CommUtils.outred("Product create 3 error  " + pId + " name=" + pname + " weight=" + pweight );
			setupErrorroduct( "parseInt" );
		}
		//CommUtils.outblue("Product | created Strings:" +this);
		logger.info( "Product | created Strings:"+ this.toString() + " in tread:" + Thread.currentThread().getId() );
	}
	
	/*
	 * Constructor based on 2 int and 1 String value
	 */	
	public Product(int productId, String name, int weight) {
		if( productId == 0 ) {
			setupErrorroduct( "idzero" );
		}
		if( productId < 0 || weight < 0 ) {
			setupErrorroduct( "negativevalue" );
		}else {
		    this.productId = productId;
		    this.name      = name;
		    this.weight    = weight;
			//CommUtils.outblue("Product | created 3:" +this);
		}
		logger.info(  "Product | created 3 args:" + this.toString() + " in tread:" + Thread.currentThread().getId() );
 	}

	/*
	 * SELETTORI
	 */
	public int getProductId() {
		    return productId;
    }

	public String getName() {
		    return name;
	}

	public int getWeight() {
		    return weight;
	}
	
	@Override
	public String toString() {
		return "{\"productId\":ID,\"name\":NAME,\"weight\":W}"
				.replace("ID", ""+productId).replace("NAME", "\""+name+"\"").replace("W",""+weight);
	}
	
	 /*
	  * Main method to test the class (and check the log using gradlew run)
	  * Better to use a test unit (and check the app_cargoproductTest.log using gradlew test)
	  */

	 public static void main(String[] args) {
		 Product p1 = new Product(22, "p2",  22 );
	 }

 
}
