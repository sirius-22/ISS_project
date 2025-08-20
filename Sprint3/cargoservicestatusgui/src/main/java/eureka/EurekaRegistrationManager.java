//package main.java.eureka;
//import com.netflix.appinfo.ApplicationInfoManager;
//import com.netflix.appinfo.EurekaInstanceConfig;
//import com.netflix.appinfo.InstanceInfo;
//import com.netflix.appinfo.InstanceInfo.InstanceStatus;
//import com.netflix.appinfo.providers.EurekaConfigBasedInstanceInfoProvider;
//import com.netflix.discovery.DefaultEurekaClientConfig;
//import com.netflix.discovery.DiscoveryClient;
//import com.netflix.discovery.EurekaClient;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import org.springframework.stereotype.Component;
//import unibo.basicomm23.utils.CommUtils;
//
//@Component
//public class EurekaRegistrationManager {
//
//    private EurekaClient eurekaClient;
//
//    @PostConstruct // Si esegue all'avvio dell'applicazione
//    public void registerWithEureka() {
//        CommUtils.outblue("EUREKA | Starting registration for CARGOSERVICE-GUI...");
//        try {
//            // 1. Crea la configurazione dell'istanza usando la nostra classe custom
//            EurekaInstanceConfig instanceConfig = new EurekaGuiConfig();
//            
//            // 2. Crea le informazioni sull'istanza basate sulla configurazione
//            InstanceInfo instanceInfo = new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get();
//
//            // 3. Crea il client Eureka, che gestirà gli heartbeat
//            eurekaClient = new DiscoveryClient(new ApplicationInfoManager(instanceConfig, instanceInfo), new DefaultEurekaClientConfig());
//            
//            // 4. Notifica a Eureka che il servizio è UP
//            ApplicationInfoManager.getInstance().setInstanceStatus(InstanceStatus.UP);
//            
//            CommUtils.outgreen("EUREKA | CARGOSERVICE-GUI registered successfully!");
//
//        } catch (Exception e) {
//            CommUtils.outred("EUREKA | Registration FAILED: " + e.getMessage());
//        }
//    }
//
//    @PreDestroy // Si esegue prima che l'applicazione si spenga
//    public void unregisterFromEureka() {
//        if (eurekaClient != null) {
//            CommUtils.outblue("EUREKA | Unregistering CARGOSERVICE-GUI...");
//            eurekaClient.shutdown(); // Questo notifica a Eureka la de-registrazione
//            CommUtils.outgreen("EUREKA | Unregistered successfully.");
//        }
//    }
//}