//package main.java.eureka;
//
//import com.netflix.appinfo.MyDataCenterInstanceConfig;
//
//public class EurekaGuiConfig extends MyDataCenterInstanceConfig {
//
//    private final int guiPort = 8001; // La porta del contesto Qak della GUI
//
//    @Override
//    public String getAppname() {
//        // Nome univoco con cui il servizio si registra su Eureka
//        return "CARGOSERVICE-GUI";
//    }
//
//    @Override
//    public String getHostName(boolean refresh) {
//		String ip ="";
//		String serviceshost = System.getenv("SERVICE_HOST") ;
// 		if( serviceshost != null) {
// 			ip = serviceshost;
// 		}else ip = super.getHostName(refresh);
// 		return ip;
//    }
//
//    @Override
//    public String getIpAddress() {
//        String ipAddress = System.getenv("SERVICE_IP");
//        if (ipAddress != null ) {
//        	//CommUtils.outmagenta("		EurekaServiceConfig getIpAddress=" + ipAddress);
//            return ipAddress;
//         } 
//        return super.getIpAddress();
//    }
//
//    @Override
//    public int getNonSecurePort() {
//        // Questa è la porta su cui il contesto Qak della GUI è in ascolto
//        return this.guiPort;
//    }
//
//    // Puoi mantenere i default per lease e heartbeat o personalizzarli.
//    // Per un servizio GUI, heartbeat più frequenti possono essere utili.
//    @Override
//    public int getLeaseRenewalIntervalInSeconds() {
//        return 10; // Heartbeat ogni 10 secondi
//    }
//    
//    @Override
//	public int getLeaseExpirationDurationInSeconds(){
//		return 30; // Scade dopo 30 secondi senza heartbeat
//	}
//}