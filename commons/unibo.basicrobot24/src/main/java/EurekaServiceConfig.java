package main.java;
import java.util.UUID;

import com.netflix.appinfo.MyDataCenterInstanceConfig;

import unibo.basicomm23.utils.CommUtils;

/*
 * Usato da ServiceRegistred.java
 */
public class EurekaServiceConfig extends MyDataCenterInstanceConfig{
	@Override
	public String getAppname( ) {
		return "ctxbasicrobot";
	}
	
	@Override
	public String getHostName(boolean refresh) {
		String ip ="";
		String serviceshost = System.getenv("SERVICE_HOST") ;
 		if( serviceshost != null) {
 			ip = serviceshost;
 		}else ip = super.getHostName(refresh);
 		return ip;
	}
	
	
	/*
	 * IP con cui si registra il servizio
	 */
    @Override
    public String getIpAddress() {
        String ipAddress = System.getenv("SERVICE_IP");
        if (ipAddress != null ) {
        	//CommUtils.outmagenta("		EurekaServiceConfig getIpAddress=" + ipAddress);
            return ipAddress;
         } 
        return super.getIpAddress();
    }


	@Override
	public int getNonSecurePort() {
		return 8020;
	}

    /*
    Indicates the time in seconds that the eureka server waits since it received
    the last heartbeat before it can remove this instance from its view
    and there by disallowing traffic to this instance.
    
    DEFAULT: 90sec
    */
	@Override
	public int getLeaseExpirationDurationInSeconds(){
		return 60*10*6;
	}
    /*
    Indicates how often (in seconds) the eureka client needs to send heartbeats
    to eureka server to indicate that it is still alive.
    
    DEFAULT: 30 sec
    */ 
	@Override
	public int getLeaseRenewalIntervalInSeconds() {
		return 60*10*6;
	}
	
	@Override 
	public String getInstanceId() {
	    // Genera un ID univoco
	    // Puoi usare l'IP, la porta, e un identificatore casuale
	    // Oppure semplicemente il nome dell'app + un UUID
	    return getAppname() + ":" + UUID.randomUUID().toString();
	    // O se preferisci host:app:port come Spring fa per default:
	    // return super.getHostName(false) + ":" + getAppname() + ":" + getNonSecurePort();
	}
}
